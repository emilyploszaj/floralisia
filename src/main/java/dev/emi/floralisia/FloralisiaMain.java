package dev.emi.floralisia;

import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.registry.FloralisiaBlocks;
import dev.emi.floralisia.registry.FloralisiaEntities;
import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.floralisia.registry.FloralisiaRecipes;
import dev.emi.floralisia.registry.FloralisiaScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.state.property.BooleanProperty;

public class FloralisiaMain implements ModInitializer {
	public static final BooleanProperty WILT = BooleanProperty.of("floralisia_wilt");
	public static final DamageSource SACRIFICE = new SacrificeDamageSource("floralisia_sacrifice");
	@Override
	public void onInitialize() {
		if (!EnchantmentTarget.BREAKABLE.getClass().toString().equals("class net.minecraft.enchantment.EnchantmentTarget$2")) {
			throw new RuntimeException("EnchantmentTarget enums have changed, mixin ordinal needs to be adjusted");
		}

		FloralisiaBlocks.init();
		FloralisiaItems.init();
		FloralisiaBlockEntities.init();
		FloralisiaScreenHandlers.init();
		FloralisiaEntities.init();
		FloralisiaRecipes.init();
	}

	static class SacrificeDamageSource extends DamageSource {

		protected SacrificeDamageSource(String name) {
			super(name);
			this.setBypassesArmor();
		}
	}
}