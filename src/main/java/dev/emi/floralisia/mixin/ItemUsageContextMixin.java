package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Direction;

@Mixin(ItemUsageContext.class)
public class ItemUsageContextMixin {
	@Shadow @Final
	private PlayerEntity player;

	@Inject(at = @At("RETURN"), method = "getSide", cancellable = true)
	public void getSide(CallbackInfoReturnable<Direction> info) {
		if (isFlipped()) {
			info.setReturnValue(info.getReturnValue().getOpposite());
		}
	}

	@Inject(at = @At("RETURN"), method = "getPlayerFacing", cancellable = true)
	public void getPlayerFacing(CallbackInfoReturnable<Direction> info) {
		if (isFlipped()) {
			info.setReturnValue(info.getReturnValue().getOpposite());
		}
	}

	@Unique
	private boolean isFlipped() {
		if (player != null) {
			TrinketComponent comp = TrinketsApi.getTrinketComponent(player).get();
			if (comp.isEquipped(FloralisiaItems.REPROXY_GLOVE) && ((PlayerEntityWrapper) player).isProxiedGlove()) {
				return true;
			}
		}
		return false;
	}
}
