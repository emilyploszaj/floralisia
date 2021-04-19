package dev.emi.floralisia.block.entity;

import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class PlanterBlockEntity extends DispenserBlockEntity {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.planter");
	
	public PlanterBlockEntity(BlockPos blockPos, BlockState blockState) {
	   super(FloralisiaBlockEntities.PLANTER, blockPos, blockState);
	}
 
	protected Text getContainerName() {
	   return TITLE;
	}
 }
