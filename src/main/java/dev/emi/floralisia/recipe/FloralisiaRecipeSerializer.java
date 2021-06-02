package dev.emi.floralisia.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class FloralisiaRecipeSerializer implements RecipeSerializer<FloralisiaRecipe> {
	public Function3<Identifier, FloralisiaRecipeType<? extends FloralisiaRecipe>, FloralisiaRecipeSerializer, FloralisiaRecipe> constructor;
	public FloralisiaRecipeType<? extends FloralisiaRecipe> type;

	public FloralisiaRecipeSerializer(Function3<Identifier, FloralisiaRecipeType<? extends FloralisiaRecipe>, FloralisiaRecipeSerializer,
			FloralisiaRecipe> constructor, FloralisiaRecipeType<? extends FloralisiaRecipe> type) {
		this.constructor = constructor;
		this.type = type;
	}

	@Override
	public FloralisiaRecipe read(Identifier name, JsonObject json) {
		try {
			FloralisiaRecipe recipe = constructor.apply(name, type, this);
			recipe.ingredients = deserializeInput(JsonHelper.getArray(json, "ingredients"));
			deserializeOutput(recipe, JsonHelper.getArray(json, "results"));
			if (type == FloralisiaRecipeType.POOL) {
				recipe.flowers = deserializeFlowers(JsonHelper.getArray(json, "flowers"));
				recipe.minimumFlowers = json.get("minimumflowers").getAsInt();
			}
			//System.out.println(recipe.ingredients.get(0));
			//System.out.println(recipe.outputs.get(0));
			//System.out.println(recipe.flowers.get(0));
			//System.out.println(recipe.minimumFlowers);
			return recipe;
		} catch (Exception e) {
			throw new RuntimeException("Failed to load recipe " + name, e);
		}
	}

	@Override
	public FloralisiaRecipe read(Identifier name, PacketByteBuf buf) {
		return null;
	}

	@Override
	public void write(PacketByteBuf buf, FloralisiaRecipe recipe) {
	}

	public static DefaultedList<FloralisiaIngredient> deserializeInput(JsonArray array) {
		DefaultedList<FloralisiaIngredient> ingredients = DefaultedList.of();
		for (int i = 0; i < array.size(); i++) {
			JsonObject json = array.get(i).getAsJsonObject();
			FloralisiaIngredient ingredient = FloralisiaIngredient.fromJson(json);
			ingredients.add(ingredient);
		}
		return ingredients;
	}

	public static void deserializeOutput(FloralisiaRecipe recipe, JsonArray array) {
		DefaultedList<ItemStack> stacks = DefaultedList.of();
		List<Identifier> lootTables = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			JsonObject json = array.get(i).getAsJsonObject();
			if (json.has("loot_table")) {
				lootTables.add(new Identifier(JsonHelper.getString(json, "loot_table")));
				continue;
			}
			Identifier identifier = new Identifier(JsonHelper.getString(json, "item"));
			Item item = Registry.ITEM.get(identifier);
			int count = 1;
			if (item == Items.AIR) {
				throw new IllegalStateException("Item does not exist");
			}
			if (json.has("count")) {
				count = JsonHelper.getInt(json, "count");
			}
			
			ItemStack stack = new ItemStack(item, count);
			if (json.has("nbt")) {
				stack.setTag((NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, json.get("nbt")));
			}
			stacks.add(stack);
		}
		recipe.outputs = stacks;
		recipe.lootTables = lootTables;
	}
	
	public static DefaultedList<Block> deserializeFlowers(JsonArray array) {
		DefaultedList<Block> flowers = DefaultedList.of();
		for (int i = 0; i < array.size(); i++) {
			JsonObject json = array.get(i).getAsJsonObject();
			Identifier identifier = new Identifier(JsonHelper.getString(json, "block"));
			Block block = Registry.BLOCK.get(identifier);
			if (block == Blocks.AIR) {
				throw new IllegalStateException("Block does not exist");
			}
			flowers.add(block);
		}
		return flowers;
	}
}