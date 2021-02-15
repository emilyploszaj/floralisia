package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.entity.player.PlayerInventory;
import vazkii.patchouli.client.handler.TooltipHandler;

@Mixin(TooltipHandler.class)
public class FixPatchouli {
	
	@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerEntity;inventory:Lnet/minecraft/entity/player/PlayerInventory;"),
		method = "onTooltip")
	private static PlayerInventory getInventory(ClientPlayerEntity player) {
		return player.getInventory();
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;method_1328(ILnet/minecraft/client/render/VertexFormat;)V"),
		method = "onTooltip")
	private static void begin(BufferBuilder buf, int i, VertexFormat format) {
		buf.begin(VertexFormat.DrawMode.TRIANGLE_FAN, format);
	}
}
