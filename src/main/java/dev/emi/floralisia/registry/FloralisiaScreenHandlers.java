package dev.emi.floralisia.registry;

import java.util.function.BiFunction;

import dev.emi.floralisia.screen.handler.BreakerScreenHandler;
import dev.emi.floralisia.screen.handler.CrafterScreenHandler;
import dev.emi.floralisia.screen.handler.KilnScreenHandler;
import dev.emi.floralisia.screen.handler.OvenScreenHandler;
import dev.emi.floralisia.screen.handler.VoidBagScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class FloralisiaScreenHandlers {
	public static final ScreenHandlerType<VoidBagScreenHandler> VOID_BAG = register("void_bag", VoidBagScreenHandler::new);
	public static final ScreenHandlerType<OvenScreenHandler> OVEN = register("oven", OvenScreenHandler::new);
	public static final ScreenHandlerType<KilnScreenHandler> KILN = register("kiln", KilnScreenHandler::new);
	public static final ScreenHandlerType<CrafterScreenHandler> CRAFTER = register("crafter", CrafterScreenHandler::new);
	public static final ScreenHandlerType<BreakerScreenHandler> BREAKER = register("breaker", BreakerScreenHandler::new);

	public static void init() {
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, BiFunction<Integer, PlayerInventory, T> supplier) {
		return ScreenHandlerRegistry.registerSimple(new Identifier("floralisia", name), new ScreenHandlerRegistry.SimpleClientHandlerFactory<T>() {
				public T create(int syncId, PlayerInventory inventory) {
					return supplier.apply(syncId, inventory);
				}
		});
	}
}