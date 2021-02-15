package dev.emi.floralisia.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class FloralisiaRecipe implements Recipe<Inventory> {
    private Identifier name;
    public FloralisiaRecipeType<?> type;
    public FloralisiaRecipeSerializer serializer;
	public DefaultedList<FloralisiaIngredient> ingredients = DefaultedList.of();
	public DefaultedList<ItemStack> outputs = DefaultedList.of();
	public DefaultedList<Block> flowers = DefaultedList.of();
	public int minimumFlowers = 0;

    public FloralisiaRecipe(Identifier name, FloralisiaRecipeType<?> type, FloralisiaRecipeSerializer serializer) {
        this.name = name;
        this.type = type;
        this.serializer = serializer;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

	// For OvenScreenHandler ingredient checking, very gross
	// TODO change this please
    @Override
    public boolean matches(Inventory inv, World world) {
		return ingredients.get(0).test(new ItemStack(inv.getStack(0).getItem(), 64));
    }

    @Override
    public ItemStack craft(Inventory inv) { //Unused
        return null;
    }

    @Override
    public boolean fits(int var1, int var2) { //Unused
        return false;
    }

    @Override
	public ItemStack getOutput() { //Only returns the first output, fine for machines that only have a single output
		if (outputs.size() == 0) return ItemStack.EMPTY;
        return outputs.get(0);
    }

    @Override
    public Identifier getId() {
        return name;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }
}