package dev.emi.floralisia.registry;

import dev.emi.floralisia.entity.PoolCraftingEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FloralisiaEntities {
	public static final EntityType<PoolCraftingEntity> POOL = register("pool", PoolCraftingEntity::new);

	public static void init() {
	}

	public static <E extends Entity> EntityType<E> register(String name, EntityFactory<E> factory) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier("floralisia", name), 
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.fixed(0.0F, 0.0F)).build());
	}
}