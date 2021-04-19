package dev.emi.floralisia.block.entity;

import dev.emi.floralisia.recipe.FloralisiaRecipe;
import dev.emi.floralisia.recipe.FloralisiaRecipeType;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class OvenBlockEntity extends BlockEntity implements SidedInventory {
	public DefaultedList<ItemStack> inv = DefaultedList.ofSize(5, ItemStack.EMPTY);
	public PropertyDelegate propertyDelegate;
	private int burnTime;
	private int fuelTime;
	private int cookTime;
	private int cookTimeTotal;
	private FloralisiaRecipe recipe;

	public OvenBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.OVEN, pos, state);
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				switch(index) {
				case 0:
					return OvenBlockEntity.this.burnTime;
				case 1:
					return OvenBlockEntity.this.fuelTime;
				case 2:
					return OvenBlockEntity.this.cookTime;
				case 3:
					return OvenBlockEntity.this.cookTimeTotal;
				default:
					return 0;
				}
			}

				public void set(int index, int value) {
				switch(index) {
				case 0:
					OvenBlockEntity.this.burnTime = value;
					break;
				case 1:
					OvenBlockEntity.this.fuelTime = value;
					break;
				case 2:
					OvenBlockEntity.this.cookTime = value;
					break;
				case 3:
					OvenBlockEntity.this.cookTimeTotal = value;
				}

			}

			public int size() {
				return 4;
			}
		};
	}

	public static void tick(World world, BlockPos pos, BlockState state, OvenBlockEntity be) {
		be.serverTick();
	}

	public void serverTick() {
		if (burnTime > 0) {
			burnTime--;
		}
		if (world.isClient) return;
		if (recipe != null) {
			if (!checkRecipe(recipe)) {
				cookTime = 0;
				recipe = null;
			}
		}
		if (recipe == null) {
			for (Recipe<?> r: world.getRecipeManager().listAllOfType(FloralisiaRecipeType.OVEN)) {
				FloralisiaRecipe fr = (FloralisiaRecipe) r;
				if (checkRecipe(fr)) {
					recipe = fr;
					cookTime = 0;
					cookTimeTotal = 200;
				}
			}
		}
		if (recipe != null) {
			if (burnTime == 0) {
				if (AbstractFurnaceBlockEntity.canUseAsFuel(inv.get(1))) {
					burnTime = AbstractFurnaceBlockEntity.createFuelTimeMap().get(inv.get(1).getItem());
					fuelTime = burnTime;
					inv.get(1).decrement(1);
				}
			}
			if (burnTime > 0) {
				cookTime++;
				if (cookTime >= cookTimeTotal) {
					outputRecipe(recipe);
					cookTime = 0;
				}
			} else if (cookTime > 0) {
				cookTime--;
			}
		}
	}

	public boolean checkRecipe(FloralisiaRecipe recipe) {
		if (recipe.ingredients.size() < 1) return false;
		if (!recipe.ingredients.get(0).test(inv.get(0))) return false;
		if (recipe.ingredients.size() > 1) { // Why would someone make an oven recipe without the second input slot...
			if (!recipe.ingredients.get(1).test(inv.get(2))) return false;
		}
		if (recipe.outputs.size() > 0) { // Very reasonable
			if (!inv.get(3).isEmpty() && !inv.get(3).isItemEqual(recipe.outputs.get(0))) return false;
			if (inv.get(3).getCount() + recipe.outputs.get(0).getCount() > inv.get(3).getMaxCount()) return false;
		}
		if (recipe.outputs.size() > 1) {
			if (!inv.get(4).isEmpty() && !inv.get(4).isItemEqual(recipe.outputs.get(1))) return false;
			if (inv.get(4).getCount() + recipe.outputs.get(1).getCount() > inv.get(4).getMaxCount()) return false;
		}
		return true;
	}

	public void outputRecipe(FloralisiaRecipe recipe) {
		if (recipe.outputs.size() > 0) {
			if (inv.get(3).isEmpty()) {
				inv.set(3, recipe.outputs.get(0).copy());
			} else {
				inv.get(3).setCount(inv.get(3).getCount() + recipe.outputs.get(0).getCount());
			}
		}
		if (recipe.outputs.size() > 1) {
			if (inv.get(4).isEmpty()) {
				inv.set(4, recipe.outputs.get(1).copy());
			} else {
				inv.get(4).setCount(inv.get(4).getCount() + recipe.outputs.get(1).getCount());
			}
		}
		if (recipe.ingredients.size() > 0) {
			inv.get(0).decrement(recipe.ingredients.get(0).getInputCount(inv.get(0)));;
		}
		if (recipe.ingredients.size() > 1) {
			inv.get(2).decrement(recipe.ingredients.get(1).getInputCount(inv.get(2)));;
		}
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		Inventories.readNbt(tag, inv);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		Inventories.writeNbt(tag, inv);
		return tag;
	}

	@Override
	public int size() {
		return 5;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < inv.size(); i++) {
			if (!inv.get(i).isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return inv.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inv, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(this.inv, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inv.set(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inv.size(); i++) {
			setStack(i, ItemStack.EMPTY);
		}
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[0];
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}
}