package dev.emi.floralisia.registry;

import dev.emi.floralisia.entity.PoolCraftingEntity;
import dev.emi.floralisia.entity.render.PoolRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class FloralisiaEntityRenderers {

	public static void init() {
		EntityRendererRegistry.INSTANCE.register(FloralisiaEntities.POOL, new EntityRendererFactory<PoolCraftingEntity>() {

			@Override
			public EntityRenderer<PoolCraftingEntity> create(Context ctx) {
				return new PoolRenderer(ctx);
			}
		});
	}
}