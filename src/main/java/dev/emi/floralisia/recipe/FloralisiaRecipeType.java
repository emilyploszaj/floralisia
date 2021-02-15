package dev.emi.floralisia.recipe;

import net.minecraft.recipe.RecipeType;

public class FloralisiaRecipeType<T extends FloralisiaRecipe> implements RecipeType<T> {
    public static final FloralisiaRecipeType<FloralisiaRecipe> POOL = new FloralisiaRecipeType<FloralisiaRecipe>();
    public static final FloralisiaRecipeType<FloralisiaRecipe> OVEN = new FloralisiaRecipeType<FloralisiaRecipe>();

    public FloralisiaRecipeType() {
    }
}