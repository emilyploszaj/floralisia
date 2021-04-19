package dev.emi.floralisia;

import dev.emi.floralisia.registry.FloralisiaClientPackets;
import dev.emi.floralisia.registry.FloralisiaEntityRenderers;
import dev.emi.floralisia.registry.FloralisiaKeyBinds;
import dev.emi.floralisia.registry.FloralisiaRenderLayers;
import dev.emi.floralisia.registry.FloralisiaScreens;
import net.fabricmc.api.ClientModInitializer;

public class FloralisiaClient extends FloralisiaMain implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		FloralisiaRenderLayers.init();
		FloralisiaEntityRenderers.init();
		FloralisiaScreens.init();
		FloralisiaKeyBinds.init();
		FloralisiaClientPackets.init();
	}
}
