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
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrafterBlock extends Block implements BlockEntityProvider {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.crafter");
	public static final BooleanProperty POWERED = Properties.POWERED;

	public CrafterBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
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

	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof CrafterBlockEntity) {
				ItemScatterer.spawn(world, pos, (Inventory) be);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos)/* || world.isReceivingRedstonePower(pos.up())*/;
		boolean bl2 = state.get(POWERED);
		if (bl && !bl2) {
			world.setBlockState(pos, state.with(POWERED, true), 4);
		} else if (!bl && bl2) {
			world.setBlockState(pos, state.with(POWERED, false), 4);
		}
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

	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
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
