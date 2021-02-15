package dev.emi.floralisia.entity.render;

import dev.emi.floralisia.entity.PoolCraftingEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

public class PoolRenderer extends EntityRenderer<PoolCraftingEntity> {

	public PoolRenderer(Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(PoolCraftingEntity entity) {
		return null;
	}
	
}