package dev.emi.floralisia.block.entity;

import java.util.ArrayList;
import java.util.List;

import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EnchantedGrassBlockEntity extends BlockEntity {
	public BlockState flowerState;

	public EnchantedGrassBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.ENCHANTED_GRASS, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, EnchantedGrassBlockEntity be) {
		be.serverTick(world, pos, state);
	}

	public void serverTick(World world, BlockPos pos, BlockState state) {

	}

	public void spreadFlower(World world, BlockPos grassPos, EssenceType type, float baseChance, int variance) {
		if (world.getRandom().nextFloat() > baseChance) {
			return;
		}
		flowerState = world.getBlockState(grassPos.offset(Direction.UP));
		if (flowerState != null && type.contains(flowerState.getBlock())) {
			FlowerBlock block = ((FlowerBlock) flowerState.getBlock());
			List<BlockPos> validSpaces = new ArrayList<>();
			for (int x = -2; x < 3; x++) {
				for (int y = 0; y < 2; y++) {
					for (int z = -2; z < 3; z++) {
						BlockPos pos = grassPos.add(x, y, z);
						if (world.isAir(pos) && block.canPlaceAt(flowerState, world, pos)) {
							validSpaces.add(pos);
						}
					}
				}
			}
			if (validSpaces.size() > 0) {
				int i = world.getRandom().nextInt(validSpaces.size());
				world.setBlockState(validSpaces.get(i), flowerState);
			}
		}
	}
}
