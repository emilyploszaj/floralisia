package dev.emi.floralisia.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import dev.emi.floralisia.block.entity.SplitterBlockEntity;
import dev.emi.floralisia.wrapper.HopperBlockEntityWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity implements HopperBlockEntityWrapper {
	@Shadow
	private int transferCooldown;
	@Shadow
	private long lastTickTime;
	
	@Shadow
	abstract void setCooldown(int cooldown);
	
	@Shadow
	abstract boolean needsCooldown();
	
	@Shadow
	abstract boolean isFull();
	
	@Shadow
	private static boolean extract(World world, Hopper hopper) {
		return false;
	}

	@Shadow
	private static boolean isInventoryFull(Inventory inventory, Direction direction) {
		return false;
	}

	protected HopperBlockEntityMixin() {
		super(null, null, null);
	}

	@Override
	public void splitterTick(World world, BlockPos pos, BlockState state, SplitterBlockEntity be) {
		transferCooldown--;
		lastTickTime = world.getTime();
		if (!needsCooldown()) {
		   setCooldown(0);
		   splitterInsertAndExtract(world, pos, state, be, () -> {
			  return extract(world, be);
		   });
		}
	}

	@Unique
	private boolean splitterInsertAndExtract(World world, BlockPos pos, BlockState state, SplitterBlockEntity be, BooleanSupplier booleanSupplier) {
		if (!needsCooldown() && (Boolean)state.get(HopperBlock.ENABLED)) {
			boolean bl = false;
			if (!isEmpty()) {
				bl = splitterInsert(world, pos, state, be, be, false);
				if (!bl) {
					bl = splitterInsert(world, pos, state, be, be, true);
				}
			}

			if (!isFull()) {
				bl |= booleanSupplier.getAsBoolean();
			}

			if (bl) {
				setCooldown(8);
				markDirty(world, pos, state);
				return true;
			}
		}

		return false;
	}

	private static Inventory splitterGetOutputInventory(World world, BlockPos pos, BlockState state, SplitterBlockEntity be, boolean reverse) {
		Direction direction = be.getCurrentDirection();
		if (reverse) {
			direction = direction.getOpposite();
		}
		return HopperBlockEntity.getInventoryAt(world, pos.offset(direction));
	}

	@Unique
	private static boolean splitterInsert(World world, BlockPos pos, BlockState state, Inventory inventory, SplitterBlockEntity be,
		boolean reverse) {
		Inventory inventory2 = splitterGetOutputInventory(world, pos, state, be, reverse);
		if (inventory2 == null) {
			return false;
		} else {
			Direction direction = (be.getCurrentDirection()).getOpposite();
			if (reverse) {
				direction = direction.getOpposite();
			}
			if (isInventoryFull(inventory2, direction)) {
				return false;
			} else {
				for(int i = 0; i < inventory.size(); ++i) {
					if (!inventory.getStack(i).isEmpty()) {
						ItemStack itemStack = inventory.getStack(i).copy();
						ItemStack itemStack2 = HopperBlockEntity.transfer(inventory, inventory2, inventory.removeStack(i, 1), direction);
						if (itemStack2.isEmpty()) {
							inventory2.markDirty();
							be.flipDirection();
							return true;
						}

						inventory.setStack(i, itemStack);
					}
				}

				return false;
			}
		}
	 }
}
