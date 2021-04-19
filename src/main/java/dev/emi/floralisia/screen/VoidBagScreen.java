package dev.emi.floralisia.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.floralisia.screen.handler.VoidBagScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VoidBagScreen extends HandledScreen<VoidBagScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("floralisia", "textures/gui/container/void_bag.png");

	public VoidBagScreen(VoidBagScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected boolean handleHotbarKeyPressed(int keyCode, int scanCode) {
		if (this.client.options.keySwapHands.matchesKey(keyCode, scanCode) && this.handler.propertyDelegate.get(0) == 36) {
			return false;
		}
		return super.handleHotbarKeyPressed(keyCode, scanCode);
	 }

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (this.width - this.backgroundWidth) / 2;
		int y = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}