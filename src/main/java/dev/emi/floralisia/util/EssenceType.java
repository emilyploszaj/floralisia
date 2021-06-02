package dev.emi.floralisia.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.emi.floralisia.registry.FloralisiaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public enum EssenceType {
	ARCANA(Blocks.CORNFLOWER),
	ATTACHMENT(Blocks.ALLIUM),
	CHAOS(FloralisiaBlocks.CROCUS),
	DESTRUCTION(Blocks.WITHER_ROSE, FloralisiaBlocks.CALLA_LILY),
	EARTH(FloralisiaBlocks.CYMBIDIUM),
	FABRICATION(Blocks.ORANGE_TULIP),
	GROWTH(FloralisiaBlocks.GLADIOLUS),
	HEAT(Blocks.POPPY, Blocks.RED_TULIP),
	LEVITY(Blocks.PINK_TULIP),
	LIGHT(Blocks.DANDELION),
	ORDER(Blocks.WHITE_TULIP, Blocks.AZURE_BLUET, Blocks.OXEYE_DAISY),
	PURITY(Blocks.LILY_OF_THE_VALLEY),
	RESILIENCE(FloralisiaBlocks.ANASTASIA),
	STABILITY(FloralisiaBlocks.AGAPANTHUS),
	TRANSMUTATION(FloralisiaBlocks.CYAN_ROSE),
	WATER(Blocks.BLUE_ORCHID);

	private final Set<Block> blocks;

	private EssenceType(Block... blocks) {
		this.blocks = new HashSet<>(Arrays.asList(blocks));
	}

	public boolean contains(Block block) {
		return blocks.contains(block);
	}
}