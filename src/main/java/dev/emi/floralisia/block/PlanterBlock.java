package dev.emi.floralisia.block;

import dev.emi.floralisia.block.entity.PlanterBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;

public class PlanterBlock extends DispenserBlock {
	private static final DispenserBehavior PLANT = new FallibleItemDispenserBehavior() {
		protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			this.setSuccess(false);
			if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof PlantBlock) {
				ServerWorld world = pointer.getWorld();
				BlockPos pos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				PlantBlock block = (PlantBlock) ((BlockItem) stack.getItem()).getBlock();
				BlockState state = block.getDefaultState();
				if (world.isAir(pos) && block.canPlaceAt(state, world, pos)) {
					world.setBlockState(pos, state, 3);
					stack.decrement(1);
					this.setSuccess(true);
				}
			}
			return stack;
		}
	};

	public PlanterBlock(AbstractBlock.Settings settings) {
	   super(settings);
	}
 
	protected DispenserBehavior getBehaviorForItem(ItemStack stack) {
	   return PLANT;
	}
 
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
	   return new PlanterBlockEntity(pos, state);
	}
 
	protected void dispense(ServerWorld world, BlockPos pos) {
		BlockPointerImpl pointer = new BlockPointerImpl(world, pos);
		PlanterBlockEntity planter = (PlanterBlockEntity) pointer.getBlockEntity();
		int i = planter.chooseNonEmptySlot();
		if (i < 0) {
			world.syncWorldEvent(1001, pos, 0);
		} else {
			ItemStack stack = planter.getStack(i);
			if (!stack.isEmpty()) {
				ItemStack res = PLANT.dispense(pointer, stack);
				planter.setStack(i, res);
			}
		}
	}
}