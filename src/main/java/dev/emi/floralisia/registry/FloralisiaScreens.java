package dev.emi.floralisia.registry;

import dev.emi.floralisia.screen.BreakerScreen;
import dev.emi.floralisia.screen.CrafterScreen;
import dev.emi.floralisia.screen.KilnScreen;
import dev.emi.floralisia.screen.OvenScreen;
import dev.emi.floralisia.screen.VoidBagScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class FloralisiaScreens {
	
	public static void init() {
		ScreenRegistry.register(FloralisiaScreenHandlers.VOID_BAG, VoidBagScreen::new);
		ScreenRegistry.register(FloralisiaScreenHandlers.OVEN, OvenScreen::new);
		ScreenRegistry.register(FloralisiaScreenHandlers.KILN, KilnScreen::new);
		ScreenRegistry.register(FloralisiaScreenHandlers.CRAFTER, CrafterScreen::new);
		ScreenRegistry.register(FloralisiaScreenHandlers.BREAKER, BreakerScreen::new);
	}
}