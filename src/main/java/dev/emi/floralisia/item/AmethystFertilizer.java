package dev.emi.floralisia.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AmethystFertilizer extends Item {
	public AmethystFertilizer() {
		super(new Item.Settings().group(ItemGroup.MISC));
		DispenserBlock.registerBehavior(this, new FallibleItemDispenserBehavior() {
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				World world = pointer.getWorld();
				BlockPos pos = pointer.getBlockPos()
						.offset((Direction) pointer.getBlockState().get(DispenserBlock.FACING));
				if (fertilize(world, pos, stack)) {
					this.setSuccess(true);
					if (!world.isClient) {
						// world.playLevelEvent(2005, pos, 0); TODO
					}
				} else {
					this.setSuccess(false);
				}
				return stack;
			}
		});
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (fertilize(context.getWorld(), context.getBlockPos(), context.getStack())) {
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.FAIL;
		}
	}

	public boolean fertilize(World world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof FlowerBlock) {
			List<BlockPos> possiblePoses = new ArrayList<BlockPos>();
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					for (int y = -1; y <= 1; y++) {
						BlockPos newPos = pos.add(x, y, z);
						if (!World.isValid(newPos)) continue;
						BlockState airState = world.getBlockState(newPos);
						if (airState.isAir() && state.getBlock().canPlaceAt(airState, world, newPos)) {
							possiblePoses.add(newPos);
						}
					}
				}
			}
			if (possiblePoses.size() == 0) {
				return false;
			} else {
				if (!world.isClient) {
					int i = world.getRandom().nextInt(possiblePoses.size());
					BlockPos newPos = possiblePoses.get(i);
					world.setBlockState(newPos, state);
				} else {
					BoneMealItem.createParticles(world, pos, 15);
				}
				stack.decrement(1);
				return true;
			}
		}
		return false;
	}
}