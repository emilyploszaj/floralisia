package dev.emi.floralisia.block;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface AmethystMonocleProvider {

	default boolean requiresSync() {
		return false;
	}

	List<Text> getMonocleText(World world, BlockPos pos, BlockState state);
}
