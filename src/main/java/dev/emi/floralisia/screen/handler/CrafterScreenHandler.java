package dev.emi.floralisia.screen.handler;

import dev.emi.floralisia.block.entity.CrafterBlockEntity;
import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
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
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.world.World;

public class CrafterScreenHandler extends ScreenHandler {
	public CrafterBlockEntity be;
	public PropertyDelegate propertyDelegate;
	public ScreenHandlerContext context;
	public World world;

	public CrafterScreenHandler(int syncId, PlayerInventory inv) {
		this(syncId, inv, new SimpleInventory(10), new ArrayPropertyDelegate(9), ScreenHandlerContext.EMPTY);
	}

	public CrafterScreenHandler(int syncId, PlayerInventory inv, Inventory blockInv, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
		super(FloralisiaScreenHandlers.CRAFTER, syncId);
		if (blockInv instanceof CrafterBlockEntity) {
			be = (CrafterBlockEntity) blockInv;
		}
		this.propertyDelegate = propertyDelegate;
		this.addProperties(propertyDelegate);
		this.context = context;
		this.world = inv.player.world;

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
			   this.addSlot(new DisabledSlot(blockInv, x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}
		this.addSlot(new DisabledSlot(blockInv, 9, 124, 35));

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

	@Override
	public ItemStack onSlotClick(int slotIndex, int clickData, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex < 9 && slotIndex >= 0) {
			if (be != null) {
				be.lockedSlot[slotIndex] = !be.lockedSlot[slotIndex];
				be.updateInsertSlot();
			}
			return slots.get(slotIndex).getStack();
		} else {
			return super.onSlotClick(slotIndex, clickData, actionType, player);
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	class DisabledSlot extends Slot {

		public DisabledSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}
		
		@Override
		public boolean canTakeItems(PlayerEntity playerEntity) {
			return false;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false;
		}
	}
}
