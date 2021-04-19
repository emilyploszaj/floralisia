package dev.emi.floralisia.mixin;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.floralisia.block.AmethystMonocleProvider;
import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
	@Shadow @Final
	private MinecraftClient client;

	@Inject(at = @At("TAIL"), method = "render")
	private void render(MatrixStack matrices, float tickDelta, CallbackInfo info) {
		TrinketComponent comp = TrinketsApi.getTrinketComponent(client.player).get();
		if (comp.isEquipped(FloralisiaItems.AMETHYST_MONOCLE)) {
			HitResult hit = client.crosshairTarget;
			if (hit.getType() == HitResult.Type.BLOCK) {
				BlockPos pos = ((BlockHitResult) hit).getBlockPos();
				BlockState state = client.world.getBlockState(pos);
				if (state.getBlock() instanceof AmethystMonocleProvider) {
					AmethystMonocleProvider provider = ((AmethystMonocleProvider) state.getBlock());
					List<Text> text;
					if (provider.requiresSync()) {
						PlayerEntityWrapper wrapper = (PlayerEntityWrapper) client.player;
						if (pos.equals(wrapper.getLastPos())) {
							text = wrapper.getLastText();
						} else {
							text = ImmutableList.of();
						}
					} else {
						text = provider.getMonocleText(client.world, pos, state);
					}
					if (!text.isEmpty()) {
						DummyScreen screen = new DummyScreen();
						screen.init(client, 9999, 9999);
						screen.drawTooltip(matrices, text, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2 + 8);
					}
				}
			}
		}
	}

	class DummyScreen extends Screen {
		private DummyScreen() {
			super(null);
		}
		
		void drawTooltip(MatrixStack matrices, List<Text> text, int x, int y) {
			this.renderTooltip(matrices, text, x, y);
		}
	}
}
