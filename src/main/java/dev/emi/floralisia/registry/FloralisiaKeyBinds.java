package dev.emi.floralisia.registry;

import org.lwjgl.glfw.GLFW;

import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public class FloralisiaKeyBinds {
	private static boolean proxiedGloveDown = false;
	public static KeyBinding PROXIED_GLOVE;
	
	public static void init() {
		PROXIED_GLOVE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.floralisia.proxied_glove", GLFW.GLFW_KEY_R, "Floralisia"));
		ClientTickEvents.END_CLIENT_TICK.register(e -> {
			if (PROXIED_GLOVE.isPressed() != proxiedGloveDown) {
				proxiedGloveDown = PROXIED_GLOVE.isPressed();
				((PlayerEntityWrapper) e.player).setProxiedGlove(proxiedGloveDown);
				PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
				buf.writeBoolean(proxiedGloveDown);
				CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(FloralisiaPackets.PROXIED_GLOVE, buf);
				MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
			}
		});
	}
}
