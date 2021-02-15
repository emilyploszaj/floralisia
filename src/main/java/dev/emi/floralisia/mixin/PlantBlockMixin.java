package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.registry.FloralisiaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(PlantBlock.class)
public class PlantBlockMixin {
	
	@Inject(at = @At("HEAD"), method = "canPlantOnTop", cancellable = true)
	protected void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
		if (floor.getBlock() == FloralisiaBlocks.ENCHANTED_GRASS) info.setReturnValue(true);
	}
}
