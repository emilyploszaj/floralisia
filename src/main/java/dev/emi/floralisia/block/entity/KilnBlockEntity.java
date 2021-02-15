package dev.emi.floralisia.block.entity;

import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class KilnBlockEntity extends BlockEntity implements SidedInventory {
	public DefaultedList<ItemStack> inv = DefaultedList.ofSize(7, ItemStack.EMPTY);
	public PropertyDelegate propertyDelegate;

	public KilnBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.KILN, pos, state);
	}

	@Override
	public int size() {
		return 7;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < inv.size(); i++) {
			if (!inv.get(i).isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return inv.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inv, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inv, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inv.set(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inv.size(); i++) {
			setStack(i, ItemStack.EMPTY);
		}
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[0];
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}
}