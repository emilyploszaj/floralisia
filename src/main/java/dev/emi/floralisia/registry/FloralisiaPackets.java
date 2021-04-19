package dev.emi.floralisia.registry;

import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class FloralisiaPackets {
	public static final Identifier PROXIED_GLOVE = new Identifier("floralisia", "proxied_glove");
	
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(PROXIED_GLOVE, (server, player, handler, buf, sender) -> {
			((PlayerEntityWrapper) player).setProxiedGlove(buf.readBoolean());
		});
	}
}