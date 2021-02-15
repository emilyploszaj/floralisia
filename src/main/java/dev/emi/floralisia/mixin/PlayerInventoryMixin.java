package dev.emi.floralisia.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.item.VoidBag;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.Trinket.SlotReference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
	@Shadow
	@Final
	public PlayerEntity player;
	
	@Inject(at = @At("HEAD"), method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private void insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
		if (optional.isPresent()) {
			TrinketComponent comp = optional.get();
			List<Pair<SlotReference, ItemStack>> list = comp.getEquipped(s -> {
				return s.getItem() instanceof VoidBag;
			});
			for (Pair<SlotReference, ItemStack> pair : list) {
				if (VoidBag.shouldVoid(pair.getRight(), stack)) {
					stack.setCount(0);
					info.setReturnValue(true);
				}
			}
		}
	}
}