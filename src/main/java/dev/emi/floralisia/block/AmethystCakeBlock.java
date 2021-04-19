package dev.emi.floralisia.block;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AmethystCakeBlock extends Block implements AmethystMonocleProvider {
	public static final IntProperty BITES = IntProperty.of("bites", 0, 13);

	public AmethystCakeBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(BITES, 0));
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int i = state.get(BITES);
		i += player.isSneaking() ? -1 : 1;
		if (i > 13 || i < 0) {
			return ActionResult.FAIL;
		} else {
			world.setBlockState(pos, state.with(BITES, i), 3);
		}

		return ActionResult.SUCCESS;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}

	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return 14 - state.get(BITES);
	}

	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);
	}

	@Override
	public List<Text> getMonocleText(World world, BlockPos pos, BlockState state) {
		return ImmutableList.of(new TranslatableText("tooltip.floralisia.monocle.cake_slices", 14 - state.get(BITES)));
	}
}
