package dev.emi.floralisia.mixin.monocle;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;

import dev.emi.floralisia.block.AmethystMonocleProvider;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ComparatorBlock.class)
public abstract class ComparatorBlockMixin extends AbstractRedstoneGateBlock implements AmethystMonocleProvider {
	
	protected ComparatorBlockMixin() {
		super(null);
	}

	@Override
	public boolean requiresSync() {
		return true;
	}

	@Override
	public List<Text> getMonocleText(World world, BlockPos pos, BlockState state) {
		return ImmutableList.of(
			new TranslatableText("tooltip.floralisia.monocle.comparator_input", this.getPower(world, pos, state)),
			new TranslatableText("tooltip.floralisia.monocle.comparator_side", this.getMaxInputLevelSides(world, pos, state)),
			new TranslatableText("tooltip.floralisia.monocle.strength", this.getOutputLevel(world, pos, state))
		);
	}
}
