package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Direction;

@Mixin(ItemPlacementContext.class)
public abstract class ItemPlacementContextMixin extends ItemUsageContext {
	
	private ItemPlacementContextMixin() {
		super(null, null, null);
	}

	@Inject(at = @At("RETURN"), method = "getPlayerLookDirection", cancellable = true)
	public void getPlayerLookDirection(CallbackInfoReturnable<Direction> info) {
		if (isFlipped()) {
			info.setReturnValue(info.getReturnValue().getOpposite());
		}
	}

	@Unique
	private boolean isFlipped() {
		if (getPlayer() != null) {
			TrinketComponent comp = TrinketsApi.getTrinketComponent(getPlayer()).get();
			if (comp.isEquipped(FloralisiaItems.REPROXY_GLOVE) && ((PlayerEntityWrapper) getPlayer()).isProxiedGlove()) {
				return true;
			}
		}
		return false;
	}
}
