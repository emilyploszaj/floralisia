package dev.emi.floralisia.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class CyanRoseBlockItem extends BlockItem {

	public CyanRoseBlockItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("tooltip.floralisia.cyan_rose_1").formatted(Formatting.AQUA));
		tooltip.add(new TranslatableText("tooltip.floralisia.cyan_rose_2").formatted(Formatting.AQUA));
	}
}