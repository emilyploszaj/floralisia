package dev.emi.floralisia.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;

@Mixin(GenerationSettings.class)
public abstract class GenerationSettingsMixin {

	@Inject(at = @At("HEAD"), method = "getFlowerFeatures", cancellable = true)
	private void getFlowerFeatures(CallbackInfoReturnable<List<ConfiguredFeature<?, ?>>> info) {
		// Inject head cancel, very scalable
		info.setReturnValue(new ArrayList<ConfiguredFeature<?, ?>>());
	}
}
