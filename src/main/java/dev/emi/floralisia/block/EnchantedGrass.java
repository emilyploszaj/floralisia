package dev.emi.floralisia.block;

import java.util.Random;

import dev.emi.floralisia.block.entity.EnchantedGrassBlockEntity;
import dev.emi.floralisia.registry.FloralisiaBlocks;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnchantedGrass extends Block implements BlockEntityProvider {

	public EnchantedGrass(Settings settings) {
		super(settings);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.randomTick(state, world, pos, random);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
		if (fromPos.equals(pos.up())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof EnchantedGrassBlockEntity) {
				((EnchantedGrassBlockEntity) be).updateFlower();
			}
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantedGrassBlockEntity(pos, state);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return new BlockEntityTicker<T>() {

				@Override
				public void tick(World world, BlockPos pos, BlockState state, T be) {
					if (be instanceof EnchantedGrassBlockEntity) {
						EnchantedGrassBlockEntity.tick(world, pos, state, (EnchantedGrassBlockEntity) be);
					}
				}
			};
		}
		return null;
	}

	public static void spreadFlowers(World world, BlockPos pos, EssenceType type, float baseChance, int variance) {
		BlockPos.Mutable mut = new BlockPos.Mutable();
		for (int x = -2; x < 3; x++) {
			for (int y = -2; y < 1; y++) {
				for (int z = -2; z < 3; z++) {
					mut.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
					if (world.getBlockState(mut).getBlock() == FloralisiaBlocks.ENCHANTED_GRASS) {
						BlockEntity be = world.getBlockEntity(mut);
						if (be instanceof EnchantedGrassBlockEntity) {
							((EnchantedGrassBlockEntity) be).spreadFlower(world, mut, type, baseChance, variance);
						}
					}
				}
			}
		}
	}
}
