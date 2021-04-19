package dev.emi.floralisia.block.entity;

import dev.emi.floralisia.block.PipeBlock;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PipeBlockEntity extends BlockEntity implements SidedInventory {
	private static final int[] SLOTS = new int[]{0};
	public boolean end = true;
	public BlockPos pair = null;

	public PipeBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.PIPE, pos, state);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt = super.writeNbt(nbt);
		nbt.putBoolean("End", end);
		if (pair != null) {
			NbtCompound p = new NbtCompound();
			p.putInt("x", pair.getX());
			p.putInt("y", pair.getY());
			p.putInt("z", pair.getZ());
			nbt.put("Pair", p);
		}
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		end = nbt.getBoolean("End");
		if (nbt.contains("Pair")) {
			NbtCompound p = nbt.getCompound("Pair");
			int x = p.getInt("x");
			int y = p.getInt("y");
			int z = p.getInt("z");
			pair = new BlockPos(x, y, z);
		}
	}

	public boolean canTakeInput(ItemStack stack) {
		BlockEntity be = this.getWorld().getBlockEntity(this.getPos().down());
		if (be != null && be instanceof Inventory && !(be instanceof PipeBlockEntity)) {
			Inventory inv = (Inventory) be;
			for (int i = 0; i < inv.size(); i++) {
				ItemStack slot = inv.getStack(i);
				if (slot.isEmpty()) {
					return true;
				}
				if (ItemStack.canCombine(slot, stack) && slot.getCount() + stack.getCount() <= slot.getMaxCount()) {
					return true;
				}
			}
		}
		return false;
	}

	public void acceptInput(ItemStack stack) {
		BlockEntity be = this.getWorld().getBlockEntity(this.getPos().down());
		if (be != null && be instanceof Inventory && !(be instanceof PipeBlockEntity)) {
			Inventory inv = (Inventory) be;
			for (int i = 0; i < inv.size(); i++) {
				ItemStack slot = inv.getStack(i);
				if (slot.isEmpty()) {
					inv.setStack(i, stack);
					break;
				}
				if (ItemStack.canCombine(slot, stack) && slot.getCount() + stack.getCount() <= slot.getMaxCount()) {
					slot.setCount(slot.getCount() + stack.getCount());
					inv.setStack(i, slot);
					break;
				}
			}
		}
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
		PipeBlockEntity be = PipeBlock.getPipe(this.getWorld(), pair);
		be.acceptInput(stack);
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		if (end && pair != null) {
			PipeBlockEntity be = PipeBlock.getPipe(this.getWorld(), pair);
			return be != null && be.canTakeInput(stack);
		}
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}
}
