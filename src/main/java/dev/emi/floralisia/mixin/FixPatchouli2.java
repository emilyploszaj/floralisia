package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import vazkii.patchouli.common.multiblock.AbstractMultiblock;

@Mixin(AbstractMultiblock.class)
public class FixPatchouli2 {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;method_26161()Z"),
		method = "getBlockEntity")
	public boolean getBlockEntity(Block block) {
		return block instanceof BlockEntityProvider;
	}
}
