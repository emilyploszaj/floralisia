package dev.emi.floralisia.wrapper;

import dev.emi.floralisia.block.entity.SplitterBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface HopperBlockEntityWrapper {
	
	public void splitterTick(World world, BlockPos pos, BlockState state, SplitterBlockEntity be);
}
