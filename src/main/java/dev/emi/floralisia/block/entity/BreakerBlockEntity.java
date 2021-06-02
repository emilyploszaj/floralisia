package dev.emi.floralisia.block.entity;

import dev.emi.floralisia.FloralisiaMain;
import dev.emi.floralisia.block.BreakerBlock;
import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BreakerBlockEntity extends BlockEntity implements SidedInventory {
	private static final int[] ALL_SLOTS = new int[] { 0 };
	public DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
	public ItemStack lastStack;
	public BlockState lastState;
	public float delta;
	public float breakProgress;

	public BreakerBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.BREAKER, pos, state);
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.put("stacks", Inventories.writeNbt(new NbtCompound(), stacks));
		return tag;
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		Inventories.readNbt(tag.getCompound("stacks"), stacks);
	}

	public static void tick(World world, BlockPos pos, BlockState state, BreakerBlockEntity be) {
		be.serverTick(world, pos, state);
	}

	private void serverTick(World world, BlockPos pos, BlockState state) {
		BlockPos interactPos = pos.offset(state.get(BreakerBlock.FACING));
		BlockState mineState = world.getBlockState(interactPos);
		ItemStack tool = stacks.get(0);
		if (!mineState.isAir() && !tool.isEmpty()) {
			if (!mineState.equals(lastState) || !tool.equals(lastStack)) {
				lastState = mineState;
				lastStack = tool;
				breakProgress = 0f;
				float f = mineState.getHardness(world, pos);
				if (f != -1.0F) {
					float speed = tool.getMiningSpeedMultiplier(world.getBlockState(interactPos));
					if (speed > 1.0F) {
						int eff = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool);
						if (eff > 0 && !tool.isEmpty()) {
							speed += eff * eff + 1;
						}
					}
					int i = !mineState.isToolRequired() || tool.isSuitableFor(mineState) ? 30 : 100;
					delta = speed / f / i;
				} else {
					delta = 0f;
				}
			}
			breakProgress += delta;
			if (breakProgress >= 1f) {
				breakProgress = 0f;
				world.breakBlock(interactPos, false);
				if (!mineState.isToolRequired() || tool.isSuitableFor(mineState)) {
					BlockEntity blockEntity = mineState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
					Block.dropStacks(mineState, world, interactPos, blockEntity, null, tool);
				}
				EnchantedGrass.spreadFlowers(this.getWorld(), interactPos, EssenceType.DESTRUCTION, 0.05f, 0);
				tool.damage(FloralisiaMain.config.breakerDamageMultiplier, world.random, null);
				if (tool.getDamage() > tool.getMaxDamage()) {
					stacks.set(0, ItemStack.EMPTY);
					EnchantedGrass.spreadFlowers(this.getWorld(), interactPos, EssenceType.RESILIENCE, 0.8f, 0);
				}
			}
			world.setBlockBreakingInfo(-1, interactPos, (int) (breakProgress * 10) - 1);
		}
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return stacks.get(0).isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return stacks.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.stacks, 0, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.stacks, 0);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		stacks.set(0, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		setStack(0, ItemStack.EMPTY);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return ALL_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return stack.getItem() instanceof ToolItem;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}
}
