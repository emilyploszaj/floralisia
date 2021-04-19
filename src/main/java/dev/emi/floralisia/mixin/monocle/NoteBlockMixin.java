package dev.emi.floralisia.mixin.monocle;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;

import dev.emi.floralisia.block.AmethystMonocleProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(NoteBlock.class)
public class NoteBlockMixin implements AmethystMonocleProvider {
	private static final String[] NOTE_NAMES = {
		"F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F"
	};

	@Override
	public List<Text> getMonocleText(World world, BlockPos pos, BlockState state) {
		return ImmutableList.of(new TranslatableText("tooltip.floralisia.monocle.note", noteFrom(state.get(NoteBlock.NOTE))));
	}
	
	private Text noteFrom(int i) {
		float d = i / 24f;
		float r = Math.max(0.0F, MathHelper.sin((d + 0.0F) * 6.2831855F) * 0.65F + 0.35F);
		float g = Math.max(0.0F, MathHelper.sin((d + 0.33333334F) * 6.2831855F) * 0.65F + 0.35F);
		float b = Math.max(0.0F, MathHelper.sin((d + 0.6666667F) * 6.2831855F) * 0.65F + 0.35F);
		return new LiteralText(NOTE_NAMES[i % 12]).setStyle(Style.EMPTY.withColor(MathHelper.packRgb(r, g, b)));
	}
}
