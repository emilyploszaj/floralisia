package dev.emi.floralisia.patchouli;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

import dev.emi.floralisia.recipe.FloralisiaRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class PoolRecipeComponent implements ICustomComponent {
	private transient FloralisiaRecipe floralisiaRecipe;
	//private transient int componentX, componentY;
	public IVariable headertext;
	public IVariable recipe;

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		//System.out.println("Loading recipe variable: " + lookup.apply(recipe));
		MinecraftClient client = MinecraftClient.getInstance();
		Recipe<?> rec = client.world.getRecipeManager().get(new Identifier(lookup.apply(recipe).asString()))
			.orElseThrow(() -> new NoSuchElementException());
		if (rec instanceof FloralisiaRecipe) {
			floralisiaRecipe = (FloralisiaRecipe) rec;
		}
	}

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		//this.componentX = componentX;
		//this.componentY = componentY;
	}

	@Override
	public void render(MatrixStack matrices, IComponentRenderContext context, float delta, int mouseX, int mouseY) {
		if (floralisiaRecipe == null) return;
		List<ItemStack> ingredients = new ArrayList<ItemStack>();
		for (int i = 0; i < floralisiaRecipe.ingredients.size(); i++) {
			ingredients.add((ItemStack) floralisiaRecipe.ingredients.get(i).entries[0].getStacks().toArray()[0]);
		}
		List<ItemStack> flowers = new ArrayList<ItemStack>();
		for (int i = 0; i < floralisiaRecipe.flowers.size(); i++) {
			flowers.add(new ItemStack(floralisiaRecipe.flowers.get(i)));
		}
		drawSpacedItems(matrices, context, ingredients, 24, mouseX, mouseY);
		drawSpacedItems(matrices, context, flowers, 60, mouseX, mouseY);
		MinecraftClient client = MinecraftClient.getInstance();
		TextRenderer textRenderer = client.textRenderer;
		Text f = new LiteralText("Required Flowers").setStyle(context.getFont());
		textRenderer.draw(matrices, f, (58 - textRenderer.getWidth(f) / 2), 50, context.getTextColor());
		Text text = new LiteralText(floralisiaRecipe.minimumFlowers + " Unique Flowers").setStyle(context.getFont());
		textRenderer.draw(matrices, text, (58 - textRenderer.getWidth(text) / 2), 80, context.getTextColor());
		drawSpacedItems(matrices, context, floralisiaRecipe.outputs, 100, mouseX, mouseY);
	}

	void drawSpacedItems(MatrixStack matrices, IComponentRenderContext context, List<ItemStack> stacks, int y, int mouseX, int mouseY) {
		int rx = 0;
		int inc = 0;
		if (stacks.size() > 1) {
			inc = 100 / (stacks.size() - 1);
		} else {
			rx = 50;
		}
		if (inc > 32) {
			rx = (100 - ((32 * (stacks.size() - 1)))) / 2;
			inc = 32;
		}
		for (int i = 0; i < stacks.size(); i++) {
			context.renderItemStack(matrices, rx + inc * i, y, mouseX, mouseY, stacks.get(i));
		}
	}
}