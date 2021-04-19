package dev.emi.floralisia.block.entity;


import dev.emi.floralisia.block.SplitterBlock;
import dev.emi.floralisia.block.SplitterBlock.SplitterAxis;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.wrapper.HopperBlockEntityWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SplitterBlockEntity extends HopperBlockEntity {
	private boolean flipped = false;

	public SplitterBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return FloralisiaBlockEntities.SPLITTER;
	}

	public static void splitterTick(World world, BlockPos pos, BlockState state, SplitterBlockEntity be) {
		((HopperBlockEntityWrapper) be).splitterTick(world, pos, state, be);
	}

	public Direction getCurrentDirection() {
		if (this.getCachedState().get(SplitterBlock.AXIS) == SplitterAxis.X) {
			return flipped ? Direction.EAST : Direction.WEST;
		} else {
			return flipped ? Direction.NORTH : Direction.SOUTH;
		}
	}

	public void flipDirection() {
		flipped = !flipped;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt = super.writeNbt(nbt);
		nbt.putBoolean("flipped", flipped);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		flipped = nbt.getBoolean("flipped");
	}
}
