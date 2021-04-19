package dev.emi.floralisia.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.floralisia.screen.handler.CrafterScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CrafterScreen extends HandledScreen<CrafterScreenHandler>{
	private static final Identifier TEXTURE = new Identifier("textures/gui/container/crafting_table.png");

	public CrafterScreen(CrafterScreenHandler handler, PlayerInventory inventory, Text title) {
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
		for (int sx = 0; sx < 3; sx++) {
			for (int sy = 0; sy < 3; sy++) {
				if (handler.propertyDelegate.get(sy * 3 + sx) != 0) {
					int dx = x + sx * 18 + 30;
					int dy = y + sy * 18 + 17;
					fill(matrices, dx, dy, dx + 16, dy + 16, 0x7FFF0000);
				}
			}
		}
	}
}
