package dev.emi.floralisia.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class EnchantedGrassBlockEntity extends BlockEntity {
	private static final Identifier PURITY_TAG = new Identifier("floralisia", "purity");
	private static final Identifier ORDER_TAG = new Identifier("floralisia", "order");
	private final Box consumptionBox;
	private List<Item> previouslyConsumed = new ArrayList<>();
	private BlockState flowerState;
	private EssenceType flowerType;
	private int consumptionCooldown;

	public EnchantedGrassBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.ENCHANTED_GRASS, pos, state);
		consumptionBox = new Box(pos.up());
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt = super.writeNbt(nbt);
		nbt.putInt("ConsumptionCooldown", consumptionCooldown);
		NbtList list = new NbtList();
		for (Item item : previouslyConsumed) {
			list.add(NbtString.of(Registry.ITEM.getId(item).toString()));
		}
		nbt.put("PreviouslyConsumed", list);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		consumptionCooldown = nbt.getInt("ConsumptionCooldown");
		if (nbt.contains("PreviouslyConsumed")) {
			NbtList list = nbt.getList("PreviouslyConsumed", 8);
			for (int i = 0; i < list.size(); i++) {
				Item item = Registry.ITEM.get(new Identifier(list.getString(i)));
				if (item != null) {
					previouslyConsumed.add(item);
				}
			}
		}
	}

	public void updateFlower() {
		flowerState = world.getBlockState(this.getPos().offset(Direction.UP));
		Block block = flowerState.getBlock();
		for (EssenceType type : EssenceType.values()) {
			if (type.contains(block)) {
				flowerType = type;
				return;
			}
		}
		flowerType = null;
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnchantedGrassBlockEntity be) {
		be.serverTick(world, pos, state);
	}

	public void serverTick(World world, BlockPos pos, BlockState state) {
		if (flowerState == null) {
			updateFlower();
		}
		if (flowerType == EssenceType.CHAOS) {
			consumeItems(20, stack -> !stack.isEmpty());
		} else if (flowerType == EssenceType.ORDER) {
			consumeItems(8, stack -> ItemTags.getTagGroup().getTag(ORDER_TAG).contains(stack.getItem()));
		} else if (flowerType == EssenceType.PURITY) {
			consumeItems(8, stack -> ItemTags.getTagGroup().getTag(PURITY_TAG).contains(stack.getItem()));
		}
	}

	public void consumeItems(int desired, Predicate<ItemStack> accepted) {
		if (consumptionCooldown > 0) {
			consumptionCooldown--;
			return;
		}
		List<ItemEntity> entities = world.getEntitiesByClass(ItemEntity.class, consumptionBox, item -> accepted.test(item.getStack()));
		if (!entities.isEmpty()) {
			consumptionCooldown = 20;
			ItemEntity entity = entities.get(0);
			ItemStack stack = entity.getStack();
			Item item = stack.getItem();
			int unique = 0;
			for (int i = previouslyConsumed.size() - 1; i >= 0; i--) {
				if (item == previouslyConsumed.get(i)) {
					break;
				}
				unique++;
			}
			if (unique >= desired) {
				spreadFlower(this.getWorld(), this.getPos(), flowerType, 1f, 0);
			} else {
				spreadFlower(this.getWorld(), this.getPos(), flowerType, ((float) unique) / desired, 0);
			}
			previouslyConsumed.add(stack.getItem());
			if (previouslyConsumed.size() > 20) {
				previouslyConsumed.remove(0);
			}
			stack.decrement(1);
		}
	}

	public void spreadFlower(World world, BlockPos grassPos, EssenceType type, float baseChance, int variance) {
		if (world.getRandom().nextFloat() > baseChance) {
			return;
		}
		flowerState = world.getBlockState(grassPos.offset(Direction.UP));
		if (flowerState != null && type.contains(flowerState.getBlock())) {
			FlowerBlock block = ((FlowerBlock) flowerState.getBlock());
			List<BlockPos> validSpaces = new ArrayList<>();
			List<BlockPos> dislikedValidSpaces = new ArrayList<>();
			for (int x = -2; x < 3; x++) {
				for (int y = 0; y < 2; y++) {
					for (int z = -2; z < 3; z++) {
						BlockPos pos = grassPos.add(x, y, z);
						if (world.isAir(pos) && block.canPlaceAt(flowerState, world, pos)) {
							if (world.getBlockState(pos.down()).getBlock() == Blocks.COARSE_DIRT) {
								dislikedValidSpaces.add(pos);
							} else {
								validSpaces.add(pos);
							}
						}
					}
				}
			}
			if (validSpaces.isEmpty()) {
				validSpaces = dislikedValidSpaces;
			}
			if (validSpaces.size() > 0) {
				int i = world.getRandom().nextInt(validSpaces.size());
				world.setBlockState(validSpaces.get(i), flowerState);
			}
		}
	}
}
