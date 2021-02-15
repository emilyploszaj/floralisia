package dev.emi.floralisia.registry;

import com.mojang.datafixers.util.Function3;

import dev.emi.floralisia.recipe.FloralisiaRecipe;
import dev.emi.floralisia.recipe.FloralisiaRecipeSerializer;
import dev.emi.floralisia.recipe.FloralisiaRecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FloralisiaRecipes {
	public static final FloralisiaRecipeSerializer POOL = register("pool", FloralisiaRecipeType.POOL, FloralisiaRecipe::new);
	public static final FloralisiaRecipeSerializer OVEN = register("oven", FloralisiaRecipeType.OVEN, FloralisiaRecipe::new);

	public static void init(){
	}

	public static FloralisiaRecipeSerializer register(String name, FloralisiaRecipeType<? extends FloralisiaRecipe> type,
			Function3<Identifier, FloralisiaRecipeType<? extends FloralisiaRecipe>, FloralisiaRecipeSerializer, FloralisiaRecipe> constructor) {
		Identifier identifier = new Identifier("floralisia", name);
		FloralisiaRecipeSerializer serializer = new FloralisiaRecipeSerializer(constructor, type);
		Registry.register(Registry.RECIPE_SERIALIZER, identifier, serializer);
		return serializer;
	}
}