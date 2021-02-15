package dev.emi.floralisia.screen;

import dev.emi.floralisia.screen.handler.BreakerScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BreakerScreen extends HandledScreen<BreakerScreenHandler>{
	private static final Identifier TEXTURE = new Identifier("floralisia", "textures/gui/container/breaker.png");

	public BreakerScreen(BreakerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundHeight = 133;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.client.getTextureManager().bindTexture(TEXTURE);
		int x = this.x;
		int y = this.y;
		this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
}
