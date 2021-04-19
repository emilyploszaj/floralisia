package dev.emi.floralisia.registry;

import dev.emi.floralisia.FloralisiaMain;
import dev.emi.floralisia.block.AmethystCakeBlock;
import dev.emi.floralisia.block.AmethystRevibrator;
import dev.emi.floralisia.block.BreakerBlock;
import dev.emi.floralisia.block.ChuteBlock;
import dev.emi.floralisia.block.CrafterBlock;
import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.block.Kiln;
import dev.emi.floralisia.block.Oven;
import dev.emi.floralisia.block.PipeBlock;
import dev.emi.floralisia.block.PlanterBlock;
import dev.emi.floralisia.block.RandomizerBlock;
import dev.emi.floralisia.block.SplitterBlock;
import dev.emi.floralisia.item.CyanRoseBlockItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class FloralisiaBlocks {
	public static final DefaultedList<ItemStack> BLOCKS = DefaultedList.of();
	public static final ItemGroup BLOCK_GROUP = FabricItemGroupBuilder.create(new Identifier("floralisia", "blocks"))
		.icon(() -> new ItemStack(FloralisiaBlocks.CYMBIDIUM))
		.appendItems(consumer -> consumer.addAll(BLOCKS))
		.build();
	
	public static final Block AGAPANTHUS = register("agapanthus", new FlowerBlock(StatusEffects.RESISTANCE, 8, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	public static final Block ANASTASIA = register("anastasia", new FlowerBlock(StatusEffects.INVISIBILITY, 7, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	public static final Block CALLA_LILY = register("calla_lily", new FlowerBlock(StatusEffects.WEAKNESS, 4, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	public static final Block CROCUS = register("crocus", new FlowerBlock(StatusEffects.LEVITATION, 6, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	public static final Block CYAN_ROSE;
	public static final Block CYMBIDIUM = register("cymbidium", new FlowerBlock(StatusEffects.STRENGTH, 6, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	public static final Block GLADIOLUS = register("gladiolus", new FlowerBlock(StatusEffects.HASTE, 8, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	
	public static final Block ENCHANTED_GRASS = register("enchanted_grass", new EnchantedGrass(FabricBlockSettings.of(Material.SOLID_ORGANIC).ticksRandomly().strength(0.6F).sounds(BlockSoundGroup.GRASS)));

	public static final Block AMETHYST_REVIBRATOR = register("amethyst_revibrator", new AmethystRevibrator(4, 3, FabricBlockSettings.copy(Blocks.LARGE_AMETHYST_BUD).sounds(BlockSoundGroup.SMALL_AMETHYST_BUD).luminance(state -> 2)));

	public static final Block OVEN = register("oven", new Oven(FabricBlockSettings.of(Material.STONE)));
	public static final Block KILN = register("kiln", new Kiln(FabricBlockSettings.of(Material.STONE)));
	public static final Block CRAFTER = register("crafter", new CrafterBlock(FabricBlockSettings.copy(Blocks.CRAFTING_TABLE)));
	public static final Block BREAKER = register("breaker", new BreakerBlock(FabricBlockSettings.copy(Blocks.DISPENSER)));
	public static final Block PLANTER = register("planter", new PlanterBlock(FabricBlockSettings.copy(Blocks.DISPENSER)));
	public static final Block CHUTE = register("chute", new ChuteBlock(FabricBlockSettings.of(Material.METAL)));
	public static final Block SPLITTER = register("splitter", new SplitterBlock(FabricBlockSettings.of(Material.METAL)));
	public static final Block PIPE = FloralisiaMain.config.pipes
		? register("pipe", new PipeBlock(FabricBlockSettings.of(Material.DECORATION)))
		: null;

	public static final Block RANDOMIZER = register("randomizer", new RandomizerBlock(FabricBlockSettings.copy(Blocks.REPEATER)));

	public static final Block AMETHYST_CAKE = register("amethyst_cake", new AmethystCakeBlock(FabricBlockSettings.copy(Blocks.CAKE)));
	
	static {
		CYAN_ROSE = Registry.register(Registry.BLOCK, new Identifier("floralisia", "cyan_rose"), new FlowerBlock(StatusEffects.JUMP_BOOST, 6, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		Registry.register(Registry.ITEM, new Identifier("floralisia", "cyan_rose"), new CyanRoseBlockItem(CYAN_ROSE, new Item.Settings()));
		BLOCKS.add(new ItemStack(CYAN_ROSE));
	}

	public static void init() {
	}

	public static Block register(String name, Block block) {
		block = Registry.register(Registry.BLOCK, new Identifier("floralisia", name), block);
		Registry.register(Registry.ITEM, new Identifier("floralisia", name), new BlockItem(block, new Item.Settings()));
		BLOCKS.add(new ItemStack(block));
		return block;
	}
}