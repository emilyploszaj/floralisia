package dev.emi.floralisia.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.floralisia.screen.handler.KilnScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class KilnScreen extends HandledScreen<KilnScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("floralisia", "textures/gui/container/kiln.png");
	
	public KilnScreen(KilnScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
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
		PropertyDelegate propertyDelegate = ((KilnScreenHandler) handler).propertyDelegate;
		if (propertyDelegate.get(0) > 0) {
			int i = propertyDelegate.get(1);
			if (i == 0) {
				i = 200;
			}
			int l = propertyDelegate.get(0) * 13 / i;
			this.drawTexture(matrices, x + 56, y + 36 + 12 - l, 176, 12 - l, 14, l + 1);
		}
		int i = propertyDelegate.get(2);
		int j = propertyDelegate.get(3);
		int l = j != 0 && i != 0 ? i * 24 / j : 0;
		this.drawTexture(matrices, x + 79, y + 34, 176, 14, l + 1, 16);
	}
}
