package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {
	
	@Inject(at = @At(value = "INVOKE",
	target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"),
	method = "flow")
	private void flow(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo info) {
		if (world instanceof World) {
			EnchantedGrass.spreadFlowers((World) world, pos, EssenceType.EARTH, 0.2f, 1);
		}
	}
}
