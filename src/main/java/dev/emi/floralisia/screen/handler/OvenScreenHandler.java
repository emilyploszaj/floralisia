package dev.emi.floralisia.screen.handler;

import dev.emi.floralisia.recipe.FloralisiaRecipeType;
import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

public class OvenScreenHandler extends ScreenHandler {
	public ScreenHandlerContext context;
	public PropertyDelegate propertyDelegate;
	public World world;

	public OvenScreenHandler(int syncId, PlayerInventory inv) {
		this(syncId, inv, new SimpleInventory(5), new ArrayPropertyDelegate(4), ScreenHandlerContext.EMPTY);
	}

	public OvenScreenHandler(int syncId, PlayerInventory inv, Inventory blockInv, PropertyDelegate delegate, ScreenHandlerContext context) {
		super(FloralisiaScreenHandlers.OVEN, syncId);
		this.context = context;
		this.world = inv.player.world;
		propertyDelegate = delegate;
		this.addProperties(propertyDelegate);

		this.addSlot(new Slot(blockInv, 0, 56, 17));
		this.addSlot(new FuelSlot(blockInv, 1, 56, 53));
		this.addSlot(new BottleSlot(blockInv, 2, 83, 53));
		this.addSlot(new OutputSlot(blockInv, 3, 116, 17));
		this.addSlot(new OutputSlot(blockInv, 4, 116, 53));
		
		int m, l;
		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				this.addSlot(new Slot(inv, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
			}
		}

		for (m = 0; m < 9; ++m) {
			this.addSlot(new Slot(inv, m, 8 + m * 18, 142));
		}
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index >= 5 && index < 41) {
				if (itemStack2.getItem() == FloralisiaItems.CLAY_BOTTLE) {
					if (!this.insertItem(itemStack2, 2, 3, false)) {
						return ItemStack.EMPTY;
					}
				} else if (isSmeltable(itemStack2)) {
					if (!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			if (index >= 5 && index < 32) {
				if (!this.insertItem(itemStack2, 32, 41, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 32 && index < 41) {
				if (!this.insertItem(itemStack2, 5, 32, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 5, 41, false)) {
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

	protected boolean isSmeltable(ItemStack itemStack) {
		return this.world.getRecipeManager().getFirstMatch(FloralisiaRecipeType.OVEN, new SimpleInventory(new ItemStack[]{itemStack}), this.world).isPresent();
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public class FuelSlot extends Slot {
	
		public FuelSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}
	
		public boolean canInsert(ItemStack stack) {
			return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
		}
	}

	public class OutputSlot extends Slot {
	 
		public OutputSlot(Inventory inventory, int index, int x, int y) {
		   super(inventory, index, x, y);
		}
	 
		public boolean canInsert(ItemStack stack) {
			return false;
		}
	}

	public class BottleSlot extends Slot {
	 
		public BottleSlot(Inventory inventory, int index, int x, int y) {
		   super(inventory, index, x, y);
		}
	 
		public boolean canInsert(ItemStack stack) {
			return stack.getItem() == FloralisiaItems.CLAY_BOTTLE;
		}
	}
}