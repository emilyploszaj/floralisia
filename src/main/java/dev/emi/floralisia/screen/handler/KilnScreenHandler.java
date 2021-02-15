package dev.emi.floralisia.screen.handler;

import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class KilnScreenHandler extends ScreenHandler {
	public ScreenHandlerContext context;
	public PropertyDelegate propertyDelegate;

	public KilnScreenHandler(int syncId, PlayerInventory inv) {
		this(syncId, inv, new SimpleInventory(7), new ArrayPropertyDelegate(4), ScreenHandlerContext.EMPTY);
	}

	public KilnScreenHandler(int syncId, PlayerInventory inv, Inventory blockInv, PropertyDelegate delegate,
			ScreenHandlerContext context) {
		super(FloralisiaScreenHandlers.KILN, syncId);
		this.context = context;
		propertyDelegate = delegate;
		this.addProperties(propertyDelegate);

		//this.addSlot(new Slot(blockInv, 0, 56, 17));
		//this.addSlot(new FuelSlot(blockInv, 1, 56, 53));
		//this.addSlot(new BottleSlot(blockInv, 2, 83, 53));
		//this.addSlot(new OutputSlot(blockInv, 3, 116, 17));
		//this.addSlot(new OutputSlot(blockInv, 4, 116, 53));

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
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
