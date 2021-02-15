package dev.emi.floralisia.block;

import dev.emi.floralisia.block.entity.BreakerBlockEntity;
import dev.emi.floralisia.screen.handler.BreakerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BreakerBlock extends Block implements BlockEntityProvider {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.breaker");
	public static final DirectionProperty FACING = FacingBlock.FACING;

	public BreakerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));

	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
				BreakerBlockEntity bbe = (BreakerBlockEntity) world.getBlockEntity(pos);
				return new BreakerScreenHandler(i, playerInventory, bbe, ScreenHandlerContext.create(world, playerEntity.getBlockPos()));
			}, TITLE));
		}
		return ActionResult.SUCCESS;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
	}
 
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BreakerBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient) {
			return (World w, BlockPos pos, BlockState s, T be) -> {
				if (be instanceof BreakerBlockEntity) {
					BreakerBlockEntity.tick(w, pos, s, (BreakerBlockEntity) be);
				}
			};
		}
		return null;
	}
}
