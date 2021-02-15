package dev.emi.floralisia.item;

import dev.emi.floralisia.pool.PoolCrafter;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AmethystStarter extends Item{
	public AmethystStarter() {
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(200));
		DispenserBlock.registerBehavior(this, new FallibleItemDispenserBehavior(){
			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				World world = pointer.getWorld();
				BlockPos pos = pointer.getBlockPos().offset((Direction) pointer.getBlockState().get(DispenserBlock.FACING));
				if(PoolCrafter.craft(world, pos)){
					this.setSuccess(true);
					stack.damage(1, world.getRandom(), null);
					if(world.isClient){
						world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
					}
				}else{
					this.setSuccess(false);
				}
				return stack;
			}
		});
	}
	@Override
	public ActionResult useOnBlock(ItemUsageContext context){
		if(PoolCrafter.craft(context.getWorld(), context.getBlockPos().offset(context.getSide()))){
			context.getStack().damage(1, context.getWorld().getRandom(), null);
			context.getPlayer().playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F);
			return ActionResult.SUCCESS;
		}else{
			return ActionResult.FAIL;
		}
	}
}