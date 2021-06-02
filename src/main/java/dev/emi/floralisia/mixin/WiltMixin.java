package dev.emi.floralisia.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.floralisia.FloralisiaMain;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BeetrootsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;

// MushroomPlantBlock.class
@Mixin({VineBlock.class, CactusBlock.class, SugarCaneBlock.class, BambooBlock.class, SaplingBlock.class,
		SweetBerryBushBlock.class, AbstractPlantStemBlock.class, CocoaBlock.class, CropBlock.class,
		BeetrootsBlock.class, StemBlock.class, ChorusFlowerBlock.class, NetherWartBlock.class})
public abstract class WiltMixin extends Block {
	private static final BooleanProperty WILT = FloralisiaMain.WILT;

	public WiltMixin(Settings settings) {
		super(settings);
	}/*

	@Inject(at = @At("RETURN"), method = "<init>")
	public void init(CallbackInfo info) {
		this.setDefaultState(this.getDefaultState().with(WILT, false));
	}
	
	@Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
		if (state.get(WILT).booleanValue()) {
			info.cancel();
		}
	}

	@Inject(at = @At("TAIL"), method = "appendProperties")
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo info) {
		builder.add(WILT);
	}*/
}