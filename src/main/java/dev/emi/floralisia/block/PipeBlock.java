package dev.emi.floralisia.block;

import java.util.ArrayList;
import java.util.List;

import dev.emi.floralisia.block.SplitterBlock.SplitterAxis;
import dev.emi.floralisia.block.entity.PipeBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class PipeBlock extends Block implements BlockEntityProvider {
	private static final VoxelShape[] SHAPES = generateShapes();
	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty DOWN = BooleanProperty.of("down");
	public static final BooleanProperty NORTH = BooleanProperty.of("north");
	public static final BooleanProperty EAST = BooleanProperty.of("east");
	public static final BooleanProperty SOUTH = BooleanProperty.of("south");
	public static final BooleanProperty WEST = BooleanProperty.of("west");
	public static final BooleanProperty INPUT_UP = BooleanProperty.of("input_up");
	public static final BooleanProperty INPUT_NORTH = BooleanProperty.of("input_north");
	public static final BooleanProperty INPUT_EAST = BooleanProperty.of("input_east");
	public static final BooleanProperty INPUT_SOUTH = BooleanProperty.of("input_south");
	public static final BooleanProperty INPUT_WEST = BooleanProperty.of("input_west");
	public static final BooleanProperty OUTPUT = BooleanProperty.of("output");

	public PipeBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
			.with(UP, false)
			.with(DOWN, false)
			.with(NORTH, false)
			.with(EAST, false)
			.with(SOUTH, false)
			.with(WEST, false)
			.with(INPUT_UP, false)
			.with(INPUT_NORTH, false)
			.with(INPUT_EAST, false)
			.with(INPUT_SOUTH, false)
			.with(INPUT_WEST, false)
			.with(OUTPUT, false));
	}

	private static VoxelShape[] generateShapes() {
		VoxelShape base = Block.createCuboidShape(5, 5, 5, 11, 11, 11);
		VoxelShape upShape = Block.createCuboidShape(5, 11, 5, 11, 16, 11);
		VoxelShape downShape = Block.createCuboidShape(5, 0, 5, 11, 5, 11);
		VoxelShape northShape = Block.createCuboidShape(5, 5, 0, 11, 11, 5);
		VoxelShape eastShape = Block.createCuboidShape(11, 5, 5, 16, 11, 11);
		VoxelShape southShape = Block.createCuboidShape(5, 5, 11, 11, 11, 16);
		VoxelShape westShape = Block.createCuboidShape(0, 5, 5, 5, 11, 11);
		VoxelShape[] shapes = new VoxelShape[64];
		for (int i = 0; i < 64; i++) {
			boolean up = (i & 1) > 0;
			boolean down = (i & 2) > 0;
			boolean north = (i & 4) > 0;
			boolean east = (i & 8) > 0;
			boolean south = (i & 16) > 0;
			boolean west = (i & 32) > 0;
			VoxelShape shape = base;
			if (up) {
				shape = VoxelShapes.union(shape, upShape);
			}
			if (down) {
				shape = VoxelShapes.union(shape, downShape);
			}
			if (north) {
				shape = VoxelShapes.union(shape, northShape);
			}
			if (east) {
				shape = VoxelShapes.union(shape, eastShape);
			}
			if (south) {
				shape = VoxelShapes.union(shape, southShape);
			}
			if (west) {
				shape = VoxelShapes.union(shape, westShape);
			}
			shapes[i] = shape;
		}
		return shapes;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState returnState = this.getDefaultState();

		BlockEntity be = world.getBlockEntity(pos.down());
		if (be != null && be instanceof Inventory && !(be instanceof PipeBlockEntity)) {
			returnState = returnState.with(OUTPUT, true);
		}

		int connections = 0;

		for (Direction d : Direction.values()) {
			BlockPos adjPos = pos.offset(d);
			BlockState state = world.getBlockState(adjPos);
			if (connections < 2) {
				if (state.getBlock() instanceof PipeBlock && canConnect(state)) {
					returnState = returnState.with(fromDirection(d), true);
					connections++;
				}
			}
			if (isInputting(state, d.getOpposite())) {
				returnState = returnState.with(inputFromDirection(d), true);
				connections++;
				continue;
			}
		}
		
		return returnState;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world,
			BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.DOWN) {
			BlockEntity be = world.getBlockEntity(neighborPos);
			state = state.with(OUTPUT, be != null && be instanceof Inventory && !(be instanceof PipeBlockEntity));
		}
		if (neighborState.getBlock() instanceof PipeBlock && neighborState.get(fromDirection(direction.getOpposite()))) {
			state = state.with(fromDirection(direction), true);
		} else {
			state = state.with(fromDirection(direction), false);
		}
		if (isInputting(neighborState, direction.getOpposite())) {
			state = state.with(inputFromDirection(direction), true);
		} else if (direction != Direction.DOWN) {
			state = state.with(inputFromDirection(direction), false);
		}
		return state;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (world.isClient) {
			return;
		}
		PipeBlockEntity be = getPipe(world, pos);
		List<Direction> connections = getConnections(state);
		if (connections.size() == 1) {
			BlockPos offPos = pos.offset(connections.get(0));
			PipeBlockEntity cbe = getPipe(world, offPos);
			be.end = true;
			if (cbe.pair == null) { // Solo
				cbe.end = true;
				cbe.pair = pos;
				be.pair = offPos;
			} else {
				PipeBlockEntity pbe = getPipe(world, cbe.pair);
				pbe.pair = pos;
				be.pair = cbe.pair;

				cbe.pair = null;
				cbe.end = false;
			}
		} else if (connections.size() == 2) {
			be.end = false;
			BlockPos offPos1 = pos.offset(connections.get(0));
			BlockPos offPos2 = pos.offset(connections.get(1));
			PipeBlockEntity cbe1 = getPipe(world, offPos1);
			PipeBlockEntity cbe2 = getPipe(world, offPos2);
			if (offPos1 == cbe2.pair) { // Loop
				cbe1.pair = null;
				cbe1.end = false;
				cbe2.pair = null;
				cbe2.end = false;
			} else {
				if (cbe1.pair != null) {
					PipeBlockEntity clear = cbe1;
					offPos1 = cbe1.pair;
					cbe1 = getPipe(world, cbe1.pair);
					clear.pair = null;
					clear.end = false;
				}
				if (cbe2.pair != null) {
					PipeBlockEntity clear = cbe2;
					offPos2 = cbe2.pair;
					cbe2 = getPipe(world, cbe2.pair);
					clear.pair = null;
					clear.end = false;
				}
				cbe1.pair = offPos2;
				cbe2.pair = offPos1;
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!(newState.getBlock() instanceof PipeBlock)) {
			PipeBlockEntity be = getPipe(world, pos);
			List<Direction> connections = getConnections(state);
			if (connections.size() == 0) {
				return;
			}
			if (be.end) { // Scooch the end over
				PipeBlockEntity pbe = getPipe(world, be.pair);
				BlockPos offPos = pos.offset(connections.get(0));
				PipeBlockEntity cbe = getPipe(world, offPos);
				if (cbe.end) { // Make solo
					cbe.pair = null;
				} else {
					cbe.end = true;
					cbe.pair = be.pair;
					pbe.pair = offPos;
				}
			} else { // Split 1 network into 2
				for (Direction d : connections) {
					BlockPos start = pos.offset(d);
					BlockPos end = travelToEnd(world, start, d.getOpposite());
					PipeBlockEntity sbe = getPipe(world, start);
					PipeBlockEntity ebe = getPipe(world, end);
					sbe.pair = end;
					sbe.end = true;
					ebe.pair = start;
					ebe.end = true;
				}
			}
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	private BlockPos travelToEnd(World world, BlockPos pos, Direction from) {
		List<Direction> connections = getConnections(world.getBlockState(pos));
		if (connections.size() == 1) {
			return pos;
		} else {
			for (Direction d : connections) {
				if (d == from) {
					continue;
				}
				return travelToEnd(world, pos.offset(d), d.getOpposite());
			}
		}
		return null;
	}

	public static PipeBlockEntity getPipe(World world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be != null && be instanceof PipeBlockEntity) {
			return (PipeBlockEntity) be;
		}
		return null;
	}

	private boolean canConnect(BlockState state) {
		int i = 0;
		int j = 0;
		for (Direction d : Direction.values()) {
			if (state.get(fromDirection(d))) {
				i++;
			}
			if (d != Direction.DOWN && state.get(inputFromDirection(d))) {
				j++;
			}
		}
		if (state.get(OUTPUT)) {
			j++;
		}
		if (j > 0) {
			return i < 1;
		}
		return i < 2;
	}

	private boolean isInputting(BlockState state, Direction dir) {
		Block block = state.getBlock();
		if (block instanceof HopperBlock) {
			return state.get(HopperBlock.FACING) == dir;
		} else if (block instanceof SplitterBlock) {
			SplitterAxis axis = state.get(SplitterBlock.AXIS);
			if (axis == SplitterAxis.X && dir.getAxis() == Axis.X) {
				return true;
			} else if (axis == SplitterAxis.Z && dir.getAxis() == Axis.Z) {
				return true;
			}
		}
		return false;
	}

	private List<Direction> getConnections(BlockState state) {
		List<Direction> connections = new ArrayList<>();
		for (Direction d : Direction.values()) {
			if (state.get(fromDirection(d))) {
				connections.add(d);
			}
		}
		return connections;
	}

	private BooleanProperty fromDirection(Direction dir) {
		switch(dir) {
			case UP:
				return UP;
			case DOWN:
				return DOWN;
			case NORTH:
				return NORTH;
			case EAST:
				return EAST;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			default:
				throw new RuntimeException("_/OxO\\_ Somebody has been messing with the universe");
		}
	}

	private BooleanProperty inputFromDirection(Direction dir) {
		switch(dir) {
			case UP:
				return INPUT_UP;
			case NORTH:
				return INPUT_NORTH;
			case EAST:
				return INPUT_EAST;
			case SOUTH:
				return INPUT_SOUTH;
			case WEST:
				return INPUT_WEST;
			default:
				throw new RuntimeException("_/OxO\\_ Somebody has been messing with the universe");
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		int i = 0;
		if (state.get(UP) || state.get(INPUT_UP)) {
			i |= 1;
		}
		if (state.get(DOWN) || state.get(OUTPUT)) {
			i |= 2;
		}
		if (state.get(NORTH) || state.get(INPUT_NORTH)) {
			i |= 4;
		}
		if (state.get(EAST) || state.get(INPUT_EAST)) {
			i |= 8;
		}
		if (state.get(SOUTH) || state.get(INPUT_SOUTH)) {
			i |= 16;
		}
		if (state.get(WEST) || state.get(INPUT_WEST)) {
			i |= 32;
		}
		return SHAPES[i];
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, INPUT_UP, INPUT_NORTH, INPUT_EAST, INPUT_SOUTH, INPUT_WEST, OUTPUT);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PipeBlockEntity(pos, state);
	}
}
