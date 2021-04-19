package dev.emi.floralisia.patchouli;

import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.floralisia.recipe.FloralisiaRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class OvenRecipeComponent implements ICustomComponent {
	private transient FloralisiaRecipe floralisiaRecipe;
	private transient FloralisiaRecipe floralisiaRecipe2;
	//private transient int componentX, componentY;
	public IVariable headertext;
	public IVariable recipe;
	public IVariable recipe2;

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		//System.out.println("Loading recipe variable: " + lookup.apply(recipe));
		MinecraftClient client = MinecraftClient.getInstance();
		Recipe<?> rec = client.world.getRecipeManager().get(new Identifier(lookup.apply(recipe).asString()))
			.orElseThrow(() -> new NoSuchElementException());
		if (rec instanceof FloralisiaRecipe) {
			floralisiaRecipe = (FloralisiaRecipe) rec;
		}
		try {
			rec = client.world.getRecipeManager().get(new Identifier(lookup.apply(recipe2).asString())).orElseThrow(() -> new NoSuchElementException());
			if (rec instanceof FloralisiaRecipe) {
				floralisiaRecipe2 = (FloralisiaRecipe) rec;
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		//this.componentX = componentX;
		//this.componentY = componentY;
	}

	@Override
	public void render(MatrixStack matrices, IComponentRenderContext context, float delta, int mouseX, int mouseY) {
		if (floralisiaRecipe != null) {
			for (int i = 0; i < floralisiaRecipe.ingredients.size(); i++) {
				ItemStack stack = (ItemStack) floralisiaRecipe.ingredients.get(i).entries[0].getStacks().toArray()[0];
				context.renderItemStack(matrices, 28, 28 + i * 19, mouseX, mouseY, stack);
			}
			for (int i = 0; i < floralisiaRecipe.outputs.size(); i++) {
				ItemStack stack = floralisiaRecipe.outputs.get(i);
				context.renderItemStack(matrices, 72, 28 + i * 19, mouseX, mouseY, stack);
			}
			GlStateManager._enableBlend();
			RenderSystem.setShaderTexture(0, context.getCraftingTexture());
			DrawableHelper.drawTexture(matrices, 24, 24, 11, 135, 24, 43, 128, 256);
			DrawableHelper.drawTexture(matrices, 54, 41, 38, 152, 9, 9, 128, 256);
			DrawableHelper.drawTexture(matrices, 68, 24, 11, 135, 24, 43, 128, 256);
		}
		if (floralisiaRecipe2 != null) {
			for (int i = 0; i < floralisiaRecipe2.ingredients.size(); i++) {
				ItemStack stack = (ItemStack) floralisiaRecipe2.ingredients.get(i).entries[0].getStacks().toArray()[0];
				context.renderItemStack(matrices, 28, 78 + i * 19, mouseX, mouseY, stack);
			}
			for (int i = 0; i < floralisiaRecipe2.outputs.size(); i++) {
				ItemStack stack = floralisiaRecipe2.outputs.get(i);
				context.renderItemStack(matrices, 72, 78 + i * 19, mouseX, mouseY, stack);
			}
			GlStateManager._enableBlend();
			RenderSystem.setShaderTexture(0, context.getCraftingTexture());
			DrawableHelper.drawTexture(matrices, 24, 74, 11, 135, 24, 43, 128, 256);
			DrawableHelper.drawTexture(matrices, 54, 91, 38, 152, 9, 9, 128, 256);
			DrawableHelper.drawTexture(matrices, 68, 74, 11, 135, 24, 43, 128, 256);
		}
	}
}