package dev.emi.floralisia.registry;

import dev.emi.floralisia.recipe.FloralisiaRecipe;
import dev.emi.floralisia.recipe.FloralisiaRecipeSerializer;
import dev.emi.floralisia.recipe.FloralisiaRecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FloralisiaRecipes {
	public static final FloralisiaRecipeSerializer POOL = register("pool", FloralisiaRecipeType.POOL, FloralisiaRecipe.class);
	public static final FloralisiaRecipeSerializer OVEN = register("oven", FloralisiaRecipeType.OVEN, FloralisiaRecipe.class);

    public static void init(){
    }

    public static FloralisiaRecipeSerializer register(String name, FloralisiaRecipeType<? extends FloralisiaRecipe> type, Class<? extends FloralisiaRecipe> c) {
        Identifier identifier = new Identifier("floralisia", name);
        FloralisiaRecipeSerializer serializer = new FloralisiaRecipeSerializer(c, type);
        Registry.register(Registry.RECIPE_SERIALIZER, identifier, serializer);
        return serializer;
    }
}