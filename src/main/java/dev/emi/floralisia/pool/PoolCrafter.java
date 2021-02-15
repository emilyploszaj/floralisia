package dev.emi.floralisia.pool;

import java.util.ArrayList;
import java.util.List;

import dev.emi.floralisia.entity.PoolCraftingEntity;
import dev.emi.floralisia.recipe.FloralisiaIngredient;
import dev.emi.floralisia.recipe.FloralisiaRecipe;
import dev.emi.floralisia.recipe.FloralisiaRecipeType;
import dev.emi.floralisia.registry.FloralisiaEntities;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// TODO: remove this class and put the logic somewhere else
public class PoolCrafter {

	public static boolean craft(World world, BlockPos pos) {
		List<Block> flowers = new ArrayList<>();
		if (world.getFluidState(pos).isEmpty()) {
			pos = pos.offset(Direction.DOWN);
		}
		int minX = pos.getX(), maxX = pos.getX(), minZ = pos.getZ(), maxZ = pos.getZ();
		for (; maxX - minX < 8; minX--) {
			if (world.getFluidState(new BlockPos(minX, pos.getY(), pos.getZ())).isEmpty()) {
				break;
			}
		}
		for (; maxX - minX < 8; maxX++) {
			if (world.getFluidState(new BlockPos(maxX, pos.getY(), pos.getZ())).isEmpty()) {
				break;
			}
		}
		for (; maxZ - minZ < 8; minZ--) {
			if (world.getFluidState(new BlockPos(pos.getX(), pos.getY(), minZ)).isEmpty()) {
				break;
			}
		}
		for (; maxZ - minZ < 8; maxZ++) {
			if (world.getFluidState(new BlockPos(pos.getX(), pos.getY(), maxZ)).isEmpty()) {
				break;
			}
		}
		if (maxX == minX || maxZ == minZ) {
			return false;
		}
		for (int x = minX + 1; x < maxX; x++) {
			for (int z = minZ + 1; z < maxZ; z++) {
				if (!world.getFluidState(new BlockPos(x, pos.getY(), z)).isIn(FluidTags.WATER)) {
					return false;
				}
			}
		}
		for (int x = minX + 1; x < maxX; x++) {
			Block block = world.getBlockState(new BlockPos(x, pos.getY() + 1, minZ)).getBlock();
			if (!(block instanceof FlowerBlock)) {
				return false;
			} else {
				if (!flowers.contains(block)) {
					flowers.add(block);
				}
			}
			block = world.getBlockState(new BlockPos(x, pos.getY() + 1, maxZ)).getBlock();
			if (!(block instanceof FlowerBlock)) {
				return false;
			} else {
				if (!flowers.contains(block)) {
					flowers.add(block);
				}
			}
		}
		for (int z = minZ + 1; z < maxZ; z++) {
			Block block = world.getBlockState(new BlockPos(minX, pos.getY() + 1, z)).getBlock();
			if (!(block instanceof FlowerBlock)) {
				return false;
			} else {
				if (!flowers.contains(block)) {
					flowers.add(block);
				}
			}
			block = world.getBlockState(new BlockPos(maxX, pos.getY() + 1, z)).getBlock();
			if (!(block instanceof FlowerBlock)) {
				return false;
			} else {
				if (!flowers.contains(block)) {
					flowers.add(block);
				}
			}
		}
		Box box = new Box(new BlockPos(minX, pos.getY(), minZ), new BlockPos(minZ, pos.getY() + 1.0F, maxZ));
		List<ItemEntity> ingredients = world.getEntitiesByClass(ItemEntity.class, box, stack -> true);
		outer: for (FloralisiaRecipe recipe : world.getRecipeManager().listAllOfType(FloralisiaRecipeType.POOL)) {
			if (!flowers.containsAll(recipe.flowers)) {
				continue;
			}
			if (flowers.size() < recipe.minimumFlowers) {
				continue;
			}
			List<ItemEntity> used = new ArrayList<>();
			List<Integer> usedCount = new ArrayList<>();
			mid: for (FloralisiaIngredient ingredient : recipe.ingredients) {
				for (ItemEntity entity : ingredients) {
					if (ingredient.test(entity.getStack())) {
						used.add(entity);
						usedCount.add(ingredient.getInputCount(entity.getStack()));
						continue mid;
					}
				}
				continue outer;
			}
			if (!world.isClient) {
				for (int i = 0; i < used.size(); i++) {
					used.get(i).getStack().decrement(usedCount.get(i));
				}
				PoolCraftingEntity entity = new PoolCraftingEntity(FloralisiaEntities.POOL_CRAFTING_ENTITY, world);
				entity.setPos(minX + (maxX - minX) / 2.0F + 0.5F, pos.getY() + 1.5F, minZ + (maxZ - minZ) / 2.0F + 0.5F);
				entity.duration = 20;
				entity.results = recipe.outputs;
				world.spawnEntity(entity);
			}
			return true;
		}
		return false;
	}
}