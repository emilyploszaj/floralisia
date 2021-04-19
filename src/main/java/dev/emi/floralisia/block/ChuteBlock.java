package dev.emi.floralisia.block;

import dev.emi.floralisia.block.entity.ChuteBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ChuteBlock extends Block implements BlockEntityProvider {
	public static final DirectionProperty FACING =
		DirectionProperty.of("facing", Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

	public ChuteBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.DOWN));
	}

	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction direction = context.getSide().getOpposite();
		return this.getDefaultState().with(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ChuteBlockEntity(pos, state);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
