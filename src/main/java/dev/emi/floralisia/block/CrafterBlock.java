package dev.emi.floralisia.block;

import dev.emi.floralisia.block.entity.CrafterBlockEntity;
import dev.emi.floralisia.screen.handler.CrafterScreenHandler;
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

public class CrafterBlock extends Block implements BlockEntityProvider {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.crafter");

	public CrafterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
				CrafterBlockEntity cbe = (CrafterBlockEntity) world.getBlockEntity(pos);
				return new CrafterScreenHandler(i, playerInventory, cbe, cbe.propertyDelegate, ScreenHandlerContext.create(world, playerEntity.getBlockPos()));
			}, TITLE));
		}
		return ActionResult.SUCCESS;
	}

	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof CrafterBlockEntity) {
			return ((CrafterBlockEntity) be).getComparatorOutput();
		}
		return 0;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrafterBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return new BlockEntityTicker<T>() {

				@Override
				public void tick(World world, BlockPos pos, BlockState state, T be) {
					if (be instanceof CrafterBlockEntity) {
						CrafterBlockEntity.tick(world, pos, state, (CrafterBlockEntity) be);
					}
				}
			};
		}
		return null;
	}
}
