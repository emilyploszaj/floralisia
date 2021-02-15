package dev.emi.floralisia.block;

import java.util.Random;

import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AmethystRevibrator extends AmethystClusterBlock {

	public AmethystRevibrator(int height, int xzOffset, Settings settings) {
		super(height, xzOffset, settings);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Direction d = state.get(FACING).getOpposite();
		if (world.getBlockState(pos.offset(d)).getBlock() == Blocks.BUDDING_AMETHYST) {
			world.getBlockState(pos.offset(d)).randomTick(world, pos.offset(d), random);
		}
	}
}
