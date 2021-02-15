package dev.emi.floralisia.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class FloralisiaRenderLayers {

	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlock(FloralisiaBlocks.ANASTASIA, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FloralisiaBlocks.CYAN_ROSE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FloralisiaBlocks.GLADIOLUS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FloralisiaBlocks.CYMBIDIUM, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(FloralisiaBlocks.AMETHYST_REVIBRATOR, RenderLayer.getCutout());
	}
}