package dev.emi.floralisia.block;

import java.util.Random;

import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RandomizerBlock extends AbstractRedstoneGateBlock {
	public static final IntProperty OUTPUT_DIRECTION = IntProperty.of("output_direction", 0, 2);
	public static final BooleanProperty BINARY = BooleanProperty.of("binary");

	public RandomizerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(POWERED, false).with(BINARY, false));
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getAbilities().allowModifyWorld) {
			return ActionResult.PASS;
		} else {
			state = state.cycle(BINARY);
			float f = state.get(BINARY) ? 0.55F : 0.5F;
			world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			world.setBlockState(pos, state, 2);
			return ActionResult.success(world.isClient);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.get(POWERED)) {
			int i;
			if (state.get(BINARY)) {
				i = random.nextInt(2) * 2; // 0 or 2
			} else {
				i = random.nextInt(3);
			}
			state = state.with(OUTPUT_DIRECTION, i);
		}
		super.scheduledTick(state, world, pos, random);
	}

	@Override
	protected void updateTarget(World world, BlockPos pos, BlockState state) {
		world.updateNeighbors(pos, this);
		for (Direction d: Direction.Type.HORIZONTAL) {
			world.updateNeighborsExcept(pos.offset(d), this, d.getOpposite());
		}
	}

	@Override
	protected int getUpdateDelayInternal(BlockState state) {
		return 2;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		if (!state.get(POWERED)) {
			return 0;
		} else {
			float rot = state.get(FACING).asRotation();
			rot += (state.get(OUTPUT_DIRECTION) - 1) * 90;
			return Direction.fromRotation(rot) == direction ? this.getOutputLevel(world, pos, state) : 0;
		}
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED, OUTPUT_DIRECTION, BINARY);
	}
}
