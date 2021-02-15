package dev.emi.floralisia.block;

import dev.emi.floralisia.block.entity.KilnBlockEntity;
import dev.emi.floralisia.screen.handler.KilnScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Kiln extends Block implements BlockEntityProvider {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.kiln");

	public Kiln(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
				KilnBlockEntity kbe = (KilnBlockEntity) world.getBlockEntity(pos);
				return new KilnScreenHandler(i, playerInventory, kbe, kbe.propertyDelegate, ScreenHandlerContext.create(world, playerEntity.getBlockPos()));
			}, TITLE));
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new KilnBlockEntity(pos, state);
	}
}