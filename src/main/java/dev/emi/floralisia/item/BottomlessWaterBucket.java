package dev.emi.floralisia.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;

public class BottomlessWaterBucket extends BucketItem {

	public BottomlessWaterBucket(Settings settings) {
		super(Fluids.WATER, settings);
	}
	
	public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
		return stack;
	}
}