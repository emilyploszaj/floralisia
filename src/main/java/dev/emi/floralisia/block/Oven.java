package dev.emi.floralisia.block;

import dev.emi.floralisia.block.entity.OvenBlockEntity;
import dev.emi.floralisia.screen.handler.OvenScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Oven extends Block implements BlockEntityProvider {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.oven");

	public Oven(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
				OvenBlockEntity obe = (OvenBlockEntity) world.getBlockEntity(pos);
				return new OvenScreenHandler(i, playerInventory, obe, obe.propertyDelegate, ScreenHandlerContext.create(world, playerEntity.getBlockPos()));
			}, TITLE));
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new OvenBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return new BlockEntityTicker<T>() {

				@Override
				public void tick(World world, BlockPos pos, BlockState state, T be) {
					if (be instanceof OvenBlockEntity) {
						OvenBlockEntity.tick(world, pos, state, (OvenBlockEntity) be);
					}
				}
			};
		}
		return null;
	}
}