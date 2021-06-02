package dev.emi.floralisia.block;

import dev.emi.floralisia.registry.FloralisiaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class FlaxBlock extends CropBlock {
	public static final IntProperty AGE = Properties.AGE_5;

	public FlaxBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public IntProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getMaxAge() {
		return 5;
	}

	protected ItemConvertible getSeedsItem() {
		return FloralisiaItems.FLAX_SEEDS;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
