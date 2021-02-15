package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.registry.FloralisiaItems;
import net.minecraft.item.Item;

@Mixin(targets = "net.minecraft.enchantment.EnchantmentTarget$2")
public class EnchantmentTargetBreakableMixin {
	
	@Inject(at = @At("HEAD"), method = "isAcceptableItem", cancellable = true)
	public void isAcceptableItem(Item item, CallbackInfoReturnable<Boolean> info) {
		if (item == FloralisiaItems.CREEPER_CHARM) {
			info.setReturnValue(false);
		}
	}
}
