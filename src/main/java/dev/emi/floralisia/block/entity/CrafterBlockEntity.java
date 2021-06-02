package dev.emi.floralisia.block.entity;

import java.util.List;

import dev.emi.floralisia.block.EnchantedGrass;
import dev.emi.floralisia.registry.FloralisiaBlockEntities;
import dev.emi.floralisia.util.EssenceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CrafterBlockEntity extends BlockEntity implements SidedInventory {
	private static final BonelessScreenHandler HANDLER = new BonelessScreenHandler();
	private static final int[] ALL_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private static final int[] BOTTOM_SLOTS = new int[] { 9 };
	private static final int[] NO_INSERT = new int[0];
	public boolean powered = false;
	public boolean persistOut = false;
	public CraftingInventory craftingInv = new CraftingInventory(HANDLER, 3, 3);
	public DefaultedList<ItemStack> output = DefaultedList.ofSize(1, ItemStack.EMPTY);
	public int[] insertSlot = new int[] { 0 };
	public boolean[] lockedSlot = new boolean[9];
	public PropertyDelegate propertyDelegate;
	public CraftingRecipe lastRecipe;

	public CrafterBlockEntity(BlockPos pos, BlockState state) {
		super(FloralisiaBlockEntities.CRAFTER, pos, state);
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				return lockedSlot[index] ? 1 : 0;
			}

			public void set(int index, int value) {
				lockedSlot[index] = value != 0;
			}

			public int size() {
				return 9;
			}
		};
		updateInsertSlot();
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putBoolean("persist", persistOut);
		DefaultedList<ItemStack> input = DefaultedList.ofSize(9, ItemStack.EMPTY);
		for (int i = 0; i < 9; i++) {
			input.set(i, craftingInv.getStack(i));
		}
		tag.put("input", Inventories.writeNbt(new NbtCompound(), input));
		tag.put("output", Inventories.writeNbt(new NbtCompound(), output));
		NbtCompound locks = new NbtCompound();
		for (int i = 0; i < 9; i++) {
			locks.putBoolean("" + i, lockedSlot[i]);
		}
		tag.put("locked", locks);
		return tag;
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		persistOut = tag.getBoolean("persist");
		DefaultedList<ItemStack> input = DefaultedList.ofSize(9, ItemStack.EMPTY);
		Inventories.readNbt(tag.getCompound("input"), input);
		for (int i = 0; i < 9; i++) {
			craftingInv.setStack(i, input.get(i));
		}
		Inventories.readNbt(tag.getCompound("output"), output);
		NbtCompound locks = tag.getCompound("locked");
		for (int i = 0; i < 9; i++) {
			lockedSlot[i] = locks.getBoolean("" + i);
		}
		updateInsertSlot();
	}

	public int getComparatorOutput() {
		int total = 0;
		for (int i = 0; i < 9; i++) {
			if (!craftingInv.getStack(i).isEmpty()) {
				total++;
			}
		}
		return total;
	}

	public static void tick(World world, BlockPos pos, BlockState state, CrafterBlockEntity be) {
		be.serverTick(world, pos, state);
	}

	private void serverTick(World world, BlockPos pos, BlockState state) {
		powered = state.get(Properties.POWERED);
		if (persistOut && output.get(0).isEmpty()) {
			persistOut = false;
		}
		if (!persistOut) {
			List<CraftingRecipe> recipes = world.getRecipeManager().getAllMatches(RecipeType.CRAFTING, craftingInv, world);
			if (recipes.size() > 0) {
				CraftingRecipe r = recipes.get(0);
				if (r.equals(lastRecipe)) {
					if (!ItemStack.areEqual(r.getOutput(), output.get(0))) {
						consumeInput(r);
						persistOut = true;
						r = null;
					}
				} else {
					output.set(0, r.getOutput().copy());
				}
				lastRecipe = r;
			} else {
				output.set(0, ItemStack.EMPTY);
				lastRecipe = null;
			}
		}
	}

	public void updateInsertSlot() {
		for (int i = 0; i < 9; i++) {
			if (getStack(i).isEmpty() && !lockedSlot[i]) {
				insertSlot[0] = i;
				return;
			}
		}
		insertSlot[0] = -1;
	}

	public void consumeInput(CraftingRecipe recipe) {
		DefaultedList<ItemStack> remainders = recipe.getRemainder(craftingInv);
		for (int i = 0; i < 9; i++) {
			if (remainders.get(i).isEmpty()) {
				if (!craftingInv.getStack(i).isEmpty()) {
					craftingInv.getStack(i).decrement(1);
				}
			} else {
				craftingInv.setStack(i, remainders.get(i));
			}
		}
		EnchantedGrass.spreadFlowers(getWorld(), pos, EssenceType.FABRICATION, 0.05f, 0);
		updateInsertSlot();
		this.markDirty();
	}

	@Override
	public int size() {
		return 10;
	}

	@Override
	public boolean isEmpty() {
		if (!craftingInv.isEmpty()) {
			return false;
		}
		for (int i = 0; i < output.size(); i++) {
			if (!output.get(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		if (slot == 9) {
			return output.get(0);
		} else {
			return craftingInv.getStack(slot);
		}
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack ret;
		if (slot == 9) {
			ret = Inventories.splitStack(this.output, 0, amount);
		} else {
			ret = craftingInv.removeStack(slot, amount);
		}
		updateInsertSlot();
		return ret;
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack ret;
		if (slot == 9) {
			ret = Inventories.removeStack(this.output, 0);
		} else {
			ret = craftingInv.removeStack(slot);
		}
		updateInsertSlot();
		return ret;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot == 9) {
			output.set(0, stack);
		} else {
			craftingInv.setStack(slot, stack);
		}
		updateInsertSlot();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		craftingInv.clear();
		for (int i = 0; i < output.size(); i++) {
			setStack(i, ItemStack.EMPTY);
		}
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		if (side == Direction.DOWN) {
			return powered && output.get(0).isEmpty() ? ALL_SLOTS : BOTTOM_SLOTS;
		} else if (insertSlot[0] == -1) {
			return NO_INSERT;
		} else {
			return insertSlot;
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot == insertSlot[0] && dir != Direction.DOWN;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return dir == Direction.DOWN;
	}

	static class BonelessScreenHandler extends ScreenHandler {

		public BonelessScreenHandler() {
			super(null, 0);
		}

		@Override
		public boolean canUse(PlayerEntity player) {
			return false;
		}

		public void onContentChanged(Inventory inventory) {
			// No op
		}
	}
}
