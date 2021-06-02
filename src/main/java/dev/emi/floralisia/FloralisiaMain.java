package dev.emi.floralisia;

import dev.emi.floralisia.config.FloralisiaConfig;
import dev.emi.floralisia.config.FloralisiaConfigLoader;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.registry.FloralisiaBlocks;
import dev.emi.floralisia.registry.FloralisiaEntities;
import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.floralisia.registry.FloralisiaPackets;
import dev.emi.floralisia.registry.FloralisiaRecipes;
import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.BooleanProperty;

public class FloralisiaMain implements ModInitializer {
	public static final BooleanProperty WILT = BooleanProperty.of("floralisia_wilt");
	public static final DamageSource SACRIFICE = new SacrificeDamageSource("floralisia_sacrifice");
	public static FloralisiaConfig config;

	@Override
	public void onInitialize() {
		if (!EnchantmentTarget.BREAKABLE.getClass().toString().equals("class net.minecraft.enchantment.EnchantmentTarget$2")) {
			throw new RuntimeException("[floralisia] EnchantmentTarget enums have changed, mixin ordinal needs to be adjusted");
		}

		config = FloralisiaConfigLoader.load();

		FloralisiaBlocks.init();
		FloralisiaItems.init();
		FloralisiaBlockEntities.init();
		FloralisiaScreenHandlers.init();
		FloralisiaEntities.init();
		FloralisiaRecipes.init();
		FloralisiaPackets.init();

		CompostingChanceRegistry.INSTANCE.add(FloralisiaItems.FLAX_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaItems.FLAX, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaItems.FLAX_FLOWER, 0.65f);

		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.AGAPANTHUS, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.ANASTASIA, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.CALLA_LILY, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.CROCUS, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.CYAN_ROSE, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.CYMBIDIUM, 0.65f);
		CompostingChanceRegistry.INSTANCE.add(FloralisiaBlocks.GLADIOLUS, 0.65f);

		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.AGAPANTHUS, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.ANASTASIA, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.CALLA_LILY, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.CROCUS, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.CYAN_ROSE, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.CYMBIDIUM, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(FloralisiaBlocks.GLADIOLUS, 60, 100);

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			for (ServerPlayerEntity player : world.getPlayers()) {
				TrinketComponent comp = TrinketsApi.getTrinketComponent(player).get();
				if (comp.isEquipped(FloralisiaItems.AMETHYST_MONOCLE)) {
					((PlayerEntityWrapper) player).updateAmethystMonocle(player);
				}
			}
		});
	}

	static class SacrificeDamageSource extends DamageSource {

		protected SacrificeDamageSource(String name) {
			super(name);
			this.setBypassesArmor();
		}
	}
}