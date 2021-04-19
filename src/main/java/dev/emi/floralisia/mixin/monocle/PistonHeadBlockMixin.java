package dev.emi.floralisia.mixin.monocle;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;

import dev.emi.floralisia.block.AmethystMonocleProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin implements AmethystMonocleProvider {

	@Override
	public List<Text> getMonocleText(World world, BlockPos pos, BlockState state) {
		pos = pos.offset(state.get(PistonHeadBlock.FACING).getOpposite());
		state = world.getBlockState(pos);
		System.out.println(state);
		if (state.getBlock() instanceof PistonBlock) {
			return ((AmethystMonocleProvider) state.getBlock()).getMonocleText(world, pos, state);
		} else {
			return ImmutableList.of();
		}
	}
}
