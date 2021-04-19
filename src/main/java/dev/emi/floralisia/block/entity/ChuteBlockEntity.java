package dev.emi.floralisia.block.entity;

import dev.emi.floralisia.block.ChuteBlock;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ChuteBlockEntity extends BlockEntity implements Inventory {

	public ChuteBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.CHUTE, pos, state);
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public ItemStack removeStack(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		BlockPos pos = this.getPos();
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.3;
		double z = pos.getZ() + 0.5;
		Direction dir = this.getCachedState().get(ChuteBlock.FACING);
		if (dir == Direction.DOWN) {
			y -= 0.6;
		} else if (dir == Direction.NORTH) {
			z -= 0.8;
		} else if (dir == Direction.SOUTH) {
			z += 0.8;
		} else if (dir == Direction.WEST) {
			x -= 0.8;
		} else if (dir == Direction.EAST) {
			x += 0.8;
		}
		ItemEntity entity = new ItemEntity(this.getWorld(), x, y, z, stack);
		entity.setVelocity(0, 0, 0);
		world.spawnEntity(entity);
	}

	@Override
	public int size() {
		return 1;
	}
}
