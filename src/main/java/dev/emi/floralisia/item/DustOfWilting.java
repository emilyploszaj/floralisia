package dev.emi.floralisia.item;

import dev.emi.floralisia.FloralisiaMain;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DustOfWilting extends Item {

	public DustOfWilting(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		if (state.getProperties().contains(FloralisiaMain.WILT)) {
			if (state.get(FloralisiaMain.WILT).booleanValue() == false) {
				if (!world.isClient) {
					world.setBlockState(pos, state.with(FloralisiaMain.WILT, true));
					context.getStack().decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		}
		return super.useOnBlock(context);
	}
}