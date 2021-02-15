package dev.emi.floralisia.screen.handler;

import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class BreakerScreenHandler extends ScreenHandler {
	public ScreenHandlerContext context;
	public PropertyDelegate propertyDelegate;
	public World world;

	public BreakerScreenHandler(int syncId, PlayerInventory inv) {
		this(syncId, inv, new SimpleInventory(1), ScreenHandlerContext.EMPTY);
	}

	public BreakerScreenHandler(int syncId, PlayerInventory inv, Inventory blockInv, ScreenHandlerContext context) {
		super(FloralisiaScreenHandlers.BREAKER, syncId);
		this.context = context;
		this.world = inv.player.world;

		this.addSlot(new ToolSlot(blockInv, 0, 80, 20));
		
		int m, l;
		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(inv, l + m * 9 + 9, 8 + l * 18, 51 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
			this.addSlot(new Slot(inv, m, 8 + m * 18, 109));
		}
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 1 && index < 38) {
				if (itemStack2.getItem() instanceof ToolItem) {
					if (!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			if (index >= 1 && index < 28) {
				if (!this.insertItem(itemStack2, 28, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 28 && index < 37) {
				if (!this.insertItem(itemStack2, 1, 28, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 1, 28, false)) {
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

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public class ToolSlot extends Slot {
	
		public ToolSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}
	
		public boolean canInsert(ItemStack stack) {
			return stack.getItem() instanceof ToolItem;
		}
	}
}