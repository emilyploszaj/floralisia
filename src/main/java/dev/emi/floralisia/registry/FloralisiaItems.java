package dev.emi.floralisia.registry;

import dev.emi.floralisia.FloralisiaMain;
import dev.emi.floralisia.item.AmethystClippers;
import dev.emi.floralisia.item.AmethystFertilizer;
import dev.emi.floralisia.item.AmethystStarter;
import dev.emi.floralisia.item.BottomlessWaterBucket;
import dev.emi.floralisia.item.CreeperCharm;
import dev.emi.floralisia.item.DustOfWilting;
import dev.emi.floralisia.item.RingOfSafeguarding;
import dev.emi.floralisia.item.VoidBag;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import vazkii.patchouli.common.item.ItemModBook;

public class FloralisiaItems {
	public static final DefaultedList<ItemStack> ITEMS = DefaultedList.of();
	public static final ItemGroup BLOCK_GROUP = FabricItemGroupBuilder.create(new Identifier("floralisia", "items"))
			.icon(() -> new ItemStack(FloralisiaItems.GUIDE_BOOK)).appendItems(consumer -> consumer.addAll(ITEMS))
			.build();

	public static final Item GUIDE_BOOK = register("guide_book", new ItemModBook());

	public static final Item DANDELION_PETALS = register("dandelion_petals", new Item(new Item.Settings()));
	public static final Item POPPY_PETALS = register("poppy_petals", new Item(new Item.Settings()));
	public static final Item BLUE_ORCHID_PETALS = register("blue_orchid_petals", new Item(new Item.Settings()));
	public static final Item ALLIUM_PETALS = register("allium_petals", new Item(new Item.Settings()));
	public static final Item AZURE_BLUET_PETALS = register("azure_bluet_petals", new Item(new Item.Settings()));
	public static final Item RED_TULIP_PETALS = register("red_tulip_petals", new Item(new Item.Settings()));
	public static final Item ORANGE_TULIP_PETALS = register("orange_tulip_petals", new Item(new Item.Settings()));
	public static final Item WHITE_TULIP_PETALS = register("white_tulip_petals", new Item(new Item.Settings()));
	public static final Item PINK_TULIP_PETALS = register("pink_tulip_petals", new Item(new Item.Settings()));
	public static final Item OXEYE_DAISY_PETALS = register("oxeye_daisy_petals", new Item(new Item.Settings()));
	public static final Item CORNFLOWER_PETALS = register("cornflower_petals", new Item(new Item.Settings()));
	public static final Item LILY_OF_THE_VALLEY_PETALS = register("lily_of_the_valley_petals",
			new Item(new Item.Settings()));
	public static final Item WITHER_ROSE_PETALS = register("wither_rose_petals", new Item(new Item.Settings()));
	public static final Item AGAPANTHUS_PETALS = register("agapanthus_petals", new Item(new Item.Settings()));
	public static final Item ANASTASIA_PETALS = register("anastasia_petals", new Item(new Item.Settings()));
	public static final Item CALLA_LILY_PETALS = register("calla_lily_petals", new Item(new Item.Settings()));
	public static final Item CROCUS_PETALS = register("crocus_petals", new Item(new Item.Settings()));
	public static final Item CYAN_ROSE_PETALS = register("cyan_rose_petals", new Item(new Item.Settings()));
	public static final Item CYMBIDIUM_PETALS = register("cymbidium_petals", new Item(new Item.Settings()));
	public static final Item GLADIOLUS_PETALS = register("gladiolus_petals", new Item(new Item.Settings()));

	public static final Item UNFIRED_CLAY_BOTTLE = register("unfired_clay_bottle", new Item(new Item.Settings()));
	public static final Item CLAY_BOTTLE = register("clay_bottle", new Item(new Item.Settings()));

	// X passive: bookshelves (like enchantment tables)
	// active: brewing?
	// player: nearby enchantment
	public static final Item ESSENCE_OF_ARCANA = register("essence_of_arcana", new Item(new Item.Settings()));
	public static final Item ESSENCE_OF_ATTACHMENT = register("essence_of_attachment", new Item(new Item.Settings()));
	// consumption: "random" blocks or items, greatly losing efficiency if it gets a
	// repeat
	public static final Item ESSENCE_OF_CHAOS = register("essence_of_chaos", new Item(new Item.Settings()));
	// active: destroying blocks (this is kinda simple)
	public static final Item ESSENCE_OF_DESTRUCTION = register("essence_of_destruction", new Item(new Item.Settings()));
	// X passive: (active?) creation of stone blocks in the world (cobble, stone,
	// basalt)
	// active: processing of ores? but how does this become renewable?
	public static final Item ESSENCE_OF_EARTH = register("essence_of_earth", new Item(new Item.Settings()));
	// active: nearby smelting
	// active: nearby auto crafting
	public static final Item ESSENCE_OF_FABRICATION = register("essence_of_fabrication", new Item(new Item.Settings()));
	// passive: nearby crop growth (maybe this is active?)
	// active: bone meal usage
	// active: bee fertilization
	public static final Item ESSENCE_OF_GROWTH = register("essence_of_growth", new Item(new Item.Settings()));
	// passive: lava drip
	// passive: infinite fire
	// passive: nearyby furnace
	// X active: fire removing a block
	public static final Item ESSENCE_OF_HEAT = register("essence_of_heat", new Item(new Item.Settings()));
	// passive: floating items, bubble columns
	public static final Item ESSENCE_OF_LEVITY = register("essence_of_levity", new Item(new Item.Settings()));
	// passive: nearby light source
	// passive: lightning strikes?
	public static final Item ESSENCE_OF_LIGHT = register("essence_of_light", new Item(new Item.Settings()));
	// consumption: "orderly" blocks or items, like chiselled blocks
	public static final Item ESSENCE_OF_ORDER = register("essence_of_order", new Item(new Item.Settings()));
	// consumption: "pure" blocks or items, like glass
	public static final Item ESSENCE_OF_PURITY = register("essence_of_purity", new Item(new Item.Settings()));
	public static final Item ESSENCE_OF_RESILIENCE = register("essence_of_resilience", new Item(new Item.Settings()));
	public static final Item ESSENCE_OF_STABILITY = register("essence_of_stability", new Item(new Item.Settings()));
	// perhaps force players to "transmutate" other flowers into this one, rather
	// than following the mold
	public static final Item ESSENCE_OF_TRANSMUTATION = register("essence_of_transmutation",
			new Item(new Item.Settings()));
	// passive: dripstone
	// X active: splash water bottles
	// player: drinking?
	public static final Item ESSENCE_OF_WATER = register("essence_of_water", new Item(new Item.Settings()));

	public static final Item PETAL_ASH = register("petal_ash", new Item(new Item.Settings()));

	// Consumables
	public static final Item AMETHYST_FERTILIZER = register("amethyst_fertilizer",
			new AmethystFertilizer(new Item.Settings()));
	public static final Item DUST_OF_WILTING = register("dust_of_wilting", new DustOfWilting(new Item.Settings()));

	// Tools
	public static final Item AMETHYST_CLIPPERS = FloralisiaMain.config.amethystClipperDurability
			? register("amethyst_clippers", new AmethystClippers(new Item.Settings().maxCount(1).maxDamage(200)))
			: register("amethyst_clippers", new AmethystClippers(new Item.Settings().maxCount(1)));
	public static final Item AMETHYST_STARTER = FloralisiaMain.config.amethystStarterDurability
			? register("amethyst_starter", new AmethystStarter(new Item.Settings().maxCount(1).maxDamage(200)))
			: register("amethyst_starter", new AmethystStarter(new Item.Settings().maxCount(1)));

	public static final Item VOID_BAG = register("void_bag", new VoidBag(new Item.Settings().maxCount(1)));
	public static final Item CREEPER_CHARM = register("creeper_charm",
			new CreeperCharm(new Item.Settings().maxCount(1).maxDamage(1)));
	public static final Item RING_OF_SAFEGUARDING = register("ring_of_safeguarding",
			new RingOfSafeguarding(new Item.Settings().maxCount(1).maxDamage(1)));
	public static final Item REPROXY_GLOVE = register("reproxy_glove", new Item(new Item.Settings().maxCount(1)));
	public static final Item AMETHYST_MONOCLE = register("amethyst_monocle", new Item(new Item.Settings().maxCount(1)));

	public static final Item BOTTOMLESS_WATER_BUCKET = register("bottomless_water_bucket",
			new BottomlessWaterBucket(new Item.Settings().maxCount(1)));

	public static final Item UNFIRED_CLAY_BUCKET = register("unfired_clay_bucket",
			new Item(new Item.Settings().maxCount(16)));
	public static final Item CLAY_BUCKET = register("clay_bucket", new Item(new Item.Settings().maxCount(16)));
	public static final Item DEWMETAL_INGOT = register("dewmetal_ingot", new Item(new Item.Settings()));

	public static void init() {
	}

	public static Item register(String name, Item item) {
		ITEMS.add(new ItemStack(item));
		return Registry.register(Registry.ITEM, new Identifier("floralisia", name), item);
	}
}