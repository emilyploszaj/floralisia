package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(value = FluidBlock.class, priority = 2000)
public abstract class FluidBlockMixin {
	@Unique
	BlockState cachedState;
	
	@ModifyArg(at = @At(value = "INVOKE",
	target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"),
	method = "receiveNeighborFluids", index = 1)
	private BlockState readBlockState(BlockState state) {
		cachedState = state;
		return state;
	}

	@Inject(at = @At("RETURN"), method = "receiveNeighborFluids")
	private void receiveNeighborFluids(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> info) {
		if (!info.getReturnValueZ()) {
			int variance = 4;
			float chance = 0.15f;
			if (cachedState.getBlock() == Blocks.COBBLESTONE) {
				variance = 0;
			} else if (cachedState.getBlock() == Blocks.OBSIDIAN) {
				variance = 2;
				chance = 1.0f;
			} else if (cachedState.getBlock() == Blocks.BASALT) {
				variance = 3;
			}
			EnchantedGrass.spreadFlowers(world, pos, EssenceType.EARTH, chance, variance);
		}
	}
}
