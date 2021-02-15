package dev.emi.floralisia.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(FireBlock.class)
public class FireBlockMixin {
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"),
		method = "trySpreadingFire", locals = LocalCapture.CAPTURE_FAILHARD)
	private void trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random rand, int currentAge, CallbackInfo info,
			int i, BlockState state) {
		EnchantedGrass.spreadFlowers(world, pos, EssenceType.HEAT, 1.0f, 0);
	}
}
