package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.block.entity.EnchantedGrassBlockEntity;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {

	private PotionEntityMixin() {
		super(null, null);
	}

	@Inject(at = @At("HEAD"), method = "extinguishFire")
	private void extinguishFire(BlockPos pos, CallbackInfo info) {
		pos = pos.offset(Direction.DOWN);
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof EnchantedGrass) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof EnchantedGrassBlockEntity) {
				((EnchantedGrassBlockEntity) be).spreadFlower(world, pos, EssenceType.WATER, 1.0f, 0);
			}
		}
	}
}
