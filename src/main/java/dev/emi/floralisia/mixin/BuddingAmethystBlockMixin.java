package dev.emi.floralisia.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import dev.emi.floralisia.block.AmethystRevibrator;
import dev.emi.floralisia.registry.FloralisiaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Mixin(BuddingAmethystBlock.class)
public class BuddingAmethystBlockMixin {
	
	@Inject(at = @At(value = "INVOKE", target =
		"Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z",
		shift = At.Shift.AFTER), method = "randomTick", locals = LocalCapture.CAPTURE_FAILHARD)
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info,
			Direction direction, BlockPos growthPos, BlockState grownState) {
		if (grownState.getBlock() == Blocks.AMETHYST_CLUSTER) {
			for (Direction d : Direction.values()) {
				BlockState vibeState = world.getBlockState(pos.offset(d));
				if (vibeState.getBlock() == FloralisiaBlocks.AMETHYST_REVIBRATOR && vibeState.get(AmethystRevibrator.FACING) == d) {
					world.breakBlock(growthPos, true);
				}
			}
		}
	}
}
