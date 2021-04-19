package dev.emi.floralisia.screen.handler;

import java.util.HashSet;
import java.util.Set;

import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class VoidBagScreenHandler extends ScreenHandler {
	public ScreenHandlerContext context;
	public PropertyDelegate propertyDelegate;
	public Inventory config = new SimpleInventory(15);
	public int lockedSlot = 0;

	public VoidBagScreenHandler(int syncId, PlayerInventory inv) {
		this(syncId, inv, ScreenHandlerContext.EMPTY);
	}

	public VoidBagScreenHandler(int syncId, PlayerInventory inv, int lockedSlot, ScreenHandlerContext context) {
		this(syncId, inv, context);
		this.lockedSlot = lockedSlot;
		propertyDelegate.set(0, lockedSlot);
		NbtCompound tag = inv.getStack(lockedSlot).getTag();
		if (tag != null && tag.contains("voids")) {
			NbtList list = (NbtList) tag.get("voids");
			for (int i = 0; i < list.size(); i++) {
				config.setStack(i, ItemStack.fromNbt(list.getCompound(i)));
			}
		}
	}

	public VoidBagScreenHandler(int syncId, PlayerInventory inv, ScreenHandlerContext context) {
		super(FloralisiaScreenHandlers.VOID_BAG, syncId);
		this.context = context;
		propertyDelegate = new ArrayPropertyDelegate(1);
		this.addProperties(propertyDelegate);

		int m, l;

		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 5; ++l) {
				this.addSlot(new ConfigSlot(config, l + m * 5, 44 + l * 18, 18 + m * 18));
			}
		}

		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new PotentiallyLockedSlot(this, inv, l + m * 9 + 9, 8 + l * 18, 86 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
			this.addSlot(new PotentiallyLockedSlot(this, inv, m, 8 + m * 18, 144));
		}
	}

	public void close(PlayerEntity player) {
		super.close(player);
		if (!player.world.isClient) {
			ItemStack stack = player.getInventory().getStack(lockedSlot);
			NbtCompound tag = new NbtCompound();
			NbtList list = new NbtList();
			Set<Item> set = new HashSet<Item>();
			for (int i = 0; i < 15; i++) {
				if (!config.getStack(i).isEmpty() && !set.contains(config.getStack(i).getItem())) {
					set.add(config.getStack(i).getItem());
					list.add(config.getStack(i).writeNbt(new NbtCompound()));
				}
			}
			tag.put("voids", list);
			stack.setTag(tag);
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 15 && index < 42) {
				if (!this.insertItem(itemStack2, 42, 51, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 42 && index < 51) {
				if (!this.insertItem(itemStack2, 15, 42, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 15, 51, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, itemStack2);
		}

		return itemStack;
	}

	public class PotentiallyLockedSlot extends Slot {
		public VoidBagScreenHandler handler;
		public int index;

		public PotentiallyLockedSlot(VoidBagScreenHandler handler, Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
			this.handler = handler;
			this.index = index;
		}

		@Override
		public boolean canTakeItems(PlayerEntity player) {
			return handler.propertyDelegate.get(0) != index;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return handler.propertyDelegate.get(0) != index;
		}
	}

	public class ConfigSlot extends Slot {

		public ConfigSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canTakeItems(PlayerEntity player) {
			setStack(new ItemStack(player.currentScreenHandler.getCursorStack().getItem()));
			return false;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			setStack(new ItemStack(stack.getItem()));
			return false;
		}
	}
}