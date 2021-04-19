package dev.emi.floralisia.item;

import dev.emi.floralisia.screen.handler.VoidBagScreenHandler;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class VoidBag extends TrinketItem {
	private static final TranslatableText TITLE = new TranslatableText("container.floralisia.void_bag");

	public VoidBag(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (!world.isClient) {
			final int slot;
			if (hand.equals(Hand.OFF_HAND)) {
				slot = 36;
			} else {
				slot = player.getInventory().selectedSlot;
			}
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
				return new VoidBagScreenHandler(i, playerInventory, slot, ScreenHandlerContext.create(world, player.getBlockPos()));
			}, TITLE));
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	public static boolean shouldVoid(ItemStack bag, ItemStack stack) {
		NbtCompound tag = bag.getTag();
		if (tag != null && tag.contains("voids")) {
			NbtList list = (NbtList) tag.get("voids");
			for (int i = 0; i < list.size(); i++) {
				if (ItemStack.fromNbt(list.getCompound(i)).getItem() == stack.getItem()) {
					return true;
				}
			}
		}
		return false;
	}
}