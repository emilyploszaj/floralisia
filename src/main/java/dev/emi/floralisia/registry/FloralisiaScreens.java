package dev.emi.floralisia.registry;

import dev.emi.floralisia.screen.BreakerScreen;
import dev.emi.floralisia.screen.CrafterScreen;
import dev.emi.floralisia.screen.KilnScreen;
import dev.emi.floralisia.screen.OvenScreen;
import dev.emi.floralisia.screen.VoidBagScreen;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.data.client.model.BlockStateVariantMap.TriFunction;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;

public class FloralisiaScreens {
	
	public static void init() {
		register(FloralisiaScreenHandlers.VOID_BAG, VoidBagScreen::new);
		register(FloralisiaScreenHandlers.OVEN, OvenScreen::new);
		register(FloralisiaScreenHandlers.KILN, KilnScreen::new);
		register(FloralisiaScreenHandlers.CRAFTER, CrafterScreen::new);
		register(FloralisiaScreenHandlers.BREAKER, BreakerScreen::new);
	}

	public static <H extends ScreenHandler, S extends Screen & ScreenHandlerProvider<H>> void register(ScreenHandlerType<H> type, TriFunction<H, PlayerInventory, Text, S> supplier) {
		ScreenRegistry.register(type, new ScreenRegistry.Factory<H, S>() {
			public S create(H handler, PlayerInventory inventory, Text title) {
				return supplier.apply(handler, inventory, title);
			}
		});
	}
}