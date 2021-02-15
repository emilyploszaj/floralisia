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

	/*
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return world.getBlockState(pos.up()).isAir();
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos origin, BlockState state) {
		List<BlockPos> seeds = Lists.newArrayList();
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for (int x = -9; x < 10; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = -9; z < 10; z++) {
					pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
					if (world.getBlockState(pos).getBlock() instanceof FlowerBlock) {
						seeds.add(pos.mutableCopy());
					}
				}
			}
		}
		if (seeds.size() == 0) return;
		for (int i = 0; i < 32; i++) {
			pos.set(origin.getX() + random.nextInt(15) - 7, origin.getY() + random.nextInt(3), origin.getZ() + random.nextInt(15) - 7);
			if (!world.isAir(pos)) continue;
			BlockState flower = world.getBlockState(seeds.get(random.nextInt(seeds.size())));
			if (flower.canPlaceAt(world, pos) && world.getBlockState(pos.down()).getBlock() == FloralisiaBlocks.ENCHANTED_GRASS) {
				world.setBlockState(pos, flower);
			}
		}
	}*/
}
