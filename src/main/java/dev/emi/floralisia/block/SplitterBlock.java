package dev.emi.floralisia.block;

import java.util.List;

import com.google.common.collect.ImmutableList;

import dev.emi.floralisia.block.entity.SplitterBlockEntity;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SplitterBlock extends BlockWithEntity implements AmethystMonocleProvider {
	public static final EnumProperty<SplitterAxis> AXIS = EnumProperty.of("axis", SplitterAxis.class);
	public static final BooleanProperty ENABLED = BooleanProperty.of("enabled");

	public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	public static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 10.0D, 12.0D);
	public static final VoxelShape OUTSIDE_SHAPE = VoxelShapes.union(MIDDLE_SHAPE, TOP_SHAPE);
	public static final VoxelShape DEFAULT_SHAPE = VoxelShapes.combineAndSimplify(OUTSIDE_SHAPE, Hopper.INSIDE_SHAPE,
		BooleanBiFunction.ONLY_FIRST);
	public static final VoxelShape X_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(12.0D, 4.0D, 6.0D, 16.0D, 8.0D, 10.0D),
		Block.createCuboidShape(0.0D, 4.0D, 6.0D, 4.0D, 8.0D, 10.0D));
	public static final VoxelShape Z_SHAPE = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(6.0D, 4.0D, 0.0D, 10.0D, 8.0D, 4.0D),
		Block.createCuboidShape(6.0D, 4.0D, 12.0D, 10.0D, 8.0D, 16.0D));
	
	public static final VoxelShape X_RAY_TRACE_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE,
		Block.createCuboidShape(12.0D, 8.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.createCuboidShape(0.0D, 8.0D, 6.0D, 4.0D, 10.0D, 10.0D));
	public static final VoxelShape Z_RAY_TRACE_SHAPE = VoxelShapes.union(Hopper.INSIDE_SHAPE,
		Block.createCuboidShape(6.0D, 8.0D, 0.0D, 10.0D, 10.0D, 4.0D), Block.createCuboidShape(6.0D, 8.0D, 12.0D, 10.0D, 10.0D, 16.0D));

	public SplitterBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, SplitterAxis.X).with(ENABLED, true));
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (state.get(AXIS) == SplitterAxis.X) {
			return X_SHAPE;
		} else {
			return Z_SHAPE;
		}
	}

	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		if (state.get(AXIS) == SplitterAxis.X) {
			return X_RAY_TRACE_SHAPE;
		} else {
			return Z_RAY_TRACE_SHAPE;
		}
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SplitterBlockEntity) {
				player.openHandledScreen((SplitterBlockEntity)blockEntity);
			}
			return ActionResult.CONSUME;
		}
	}

	public BlockState getPlacementState(ItemPlacementContext context) {
		Axis axis = context.getPlayerFacing().getAxis();
		return this.getDefaultState().with(AXIS, axis == Axis.Z ? SplitterAxis.X : SplitterAxis.Z).with(ENABLED, true);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SplitterBlockEntity(pos, state);
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : checkType(type, FloralisiaBlockEntities.SPLITTER, SplitterBlockEntity::splitterTick);
	}

	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.isOf(state.getBlock())) {
			this.updateEnabled(world, pos, state);
		}
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		this.updateEnabled(world, pos, state);
	}

	private void updateEnabled(World world, BlockPos pos, BlockState state) {
		boolean bl = !world.isReceivingRedstonePower(pos);
		if (bl != state.get(ENABLED)) {
			world.setBlockState(pos, state.with(ENABLED, bl), 4);
		}
	}

	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof SplitterBlockEntity) {
				ItemScatterer.spawn(world, pos, (SplitterBlockEntity) be);
			}

			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	/* TODO
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
	}*/

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AXIS, ENABLED);
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SplitterBlockEntity) {
			SplitterBlockEntity.onEntityCollided(world, pos, state, entity, (SplitterBlockEntity) blockEntity);
		}

	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public List<Text> getMonocleText(World world, BlockPos pos, BlockState state) {
		if (state.get(HopperBlock.ENABLED)) {
			return ImmutableList.of(new TranslatableText("tooltip.floralisia.monocle.hopper_unlocked"));
		} else {
			return ImmutableList.of(new TranslatableText("tooltip.floralisia.monocle.hopper_locked"));
		}
	}

	public static enum SplitterAxis implements StringIdentifiable {
		X("x"),
		Z("z");

		private final String name;

		private SplitterAxis(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
