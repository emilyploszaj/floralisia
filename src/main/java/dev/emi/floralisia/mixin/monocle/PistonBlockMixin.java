package dev.emi.floralisia.mixin.monocle;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.emi.floralisia.block.AmethystMonocleProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(PistonBlock.class)
public class PistonBlockMixin implements AmethystMonocleProvider {
	@Shadow @Final
	private boolean sticky;

	@Override
	public List<Text> getMonocleText(World world, BlockPos pos, BlockState state) {
		boolean extended = state.get(PistonBlock.EXTENDED);
		if (extended && !sticky) {
			return ImmutableList.of();
		}
		Direction dir = state.get(PistonBlock.FACING);
		BlockState cachedHead = null;
		if (extended) {
			cachedHead = world.getBlockState(pos.offset(dir));
			world.setBlockState(pos.offset(dir), Blocks.AIR.getDefaultState(), 180);
		}
		PistonHandler handler = new PistonHandler(world, pos, dir, !extended);
		boolean b = handler.calculatePush();
		if (extended) {
			world.setBlockState(pos.offset(dir), cachedHead, 180);
		}
		if (b) {
			if (extended) {
				if (handler.getBrokenBlocks().size() > 0) {
					return ImmutableList.of(
						new TranslatableText("tooltip.floralisia.monocle.piston_pulled", handler.getMovedBlocks().size()),
						new TranslatableText("tooltip.floralisia.monocle.piston_broken", handler.getBrokenBlocks().size())
					);
				} else {
					return ImmutableList.of(new TranslatableText("tooltip.floralisia.monocle.piston_pulled", handler.getMovedBlocks().size()));
				}
			} else {
				if (handler.getBrokenBlocks().size() > 0) {
					return ImmutableList.of(
						new TranslatableText("tooltip.floralisia.monocle.piston_pushed", handler.getMovedBlocks().size()),
						new TranslatableText("tooltip.floralisia.monocle.piston_broken", handler.getBrokenBlocks().size())
					);
				} else {
					return ImmutableList.of(new TranslatableText("tooltip.floralisia.monocle.piston_pushed", handler.getMovedBlocks().size()));
				}
			}
		} else {
			if (extended) {
				return ImmutableList.of(
					new TranslatableText("tooltip.floralisia.monocle.piston_stuck_pull", handler.getMovedBlocks().size())
				);
			} else {
				return ImmutableList.of(
					new TranslatableText("tooltip.floralisia.monocle.piston_stuck_push", handler.getMovedBlocks().size())
				);
			}
		}
	}
}
