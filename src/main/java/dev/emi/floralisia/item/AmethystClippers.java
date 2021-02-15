package dev.emi.floralisia.item;

import dev.emi.floralisia.registry.FloralisiaItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class AmethystClippers extends Item {

	public AmethystClippers() {
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1).maxDamage(200));
		DispenserBlock.registerBehavior(this, new FallibleItemDispenserBehavior() {

			protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				World world = pointer.getWorld();
				BlockPos pos = pointer.getBlockPos().offset((Direction) pointer.getBlockState().get(DispenserBlock.FACING));
				if (clip(world, pos, stack)) {
					this.setSuccess(true);
					if (world.isClient) {
						world.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_CROP_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
					}
				} else {
					this.setSuccess(false);
				}
				return stack;
			}
		});
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context){
		if(clip(context.getWorld(), context.getBlockPos(), context.getStack())){
			context.getPlayer().playSound(SoundEvents.BLOCK_CROP_BREAK, 1.0F, 1.0F);
			return ActionResult.SUCCESS;
		}else{
			return ActionResult.FAIL;
		}
	}
	public boolean clip(World world, BlockPos pos, ItemStack stack){
		BlockState state = world.getBlockState(pos);
		Item drop;
		if(state.getBlock() == Blocks.DANDELION){
			drop = FloralisiaItems.DANDELION_PETALS;
		}else if(state.getBlock() == Blocks.POPPY){
			drop = FloralisiaItems.POPPY_PETALS;
		}else if(state.getBlock() == Blocks.BLUE_ORCHID){
			drop = FloralisiaItems.BLUE_ORCHID_PETALS;
		}else if(state.getBlock() == Blocks.ALLIUM){
			drop = FloralisiaItems.ALLIUM_PETALS;
		}else if(state.getBlock() == Blocks.AZURE_BLUET){
			drop = FloralisiaItems.AZURE_BLUET_PETALS;
		}else if(state.getBlock() == Blocks.RED_TULIP){
			drop = FloralisiaItems.RED_TULIP_PETALS;
		}else if(state.getBlock() == Blocks.ORANGE_TULIP){
			drop = FloralisiaItems.ORANGE_TULIP_PETALS;
		}else if(state.getBlock() == Blocks.WHITE_TULIP){
			drop = FloralisiaItems.WHITE_TULIP_PETALS;
		}else if(state.getBlock() == Blocks.PINK_TULIP){
			drop = FloralisiaItems.PINK_TULIP_PETALS;
		}else if(state.getBlock() == Blocks.OXEYE_DAISY){
			drop = FloralisiaItems.OXEYE_DAISY_PETALS;
		}else if(state.getBlock() == Blocks.CORNFLOWER){
			drop = FloralisiaItems.CORNFLOWER_PETALS;
		}else if(state.getBlock() == Blocks.LILY_OF_THE_VALLEY){
			drop = FloralisiaItems.LILY_OF_THE_VALLEY_PETALS;
		}else if(state.getBlock() == Blocks.WITHER_ROSE){
			drop = FloralisiaItems.WITHER_ROSE_PETALS;
		}else{
			return false;
		}
		if(!world.isClient){
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			Block.dropStack(world, pos, new ItemStack(drop));
			stack.damage(1, world.getRandom(), null);
		}
		return true;
	}
}