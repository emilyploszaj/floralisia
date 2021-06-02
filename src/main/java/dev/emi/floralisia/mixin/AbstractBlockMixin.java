package dev.emi.floralisia.mixin;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.registry.FloralisiaItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
	
	@Inject(at = @At("RETURN"), method = "getDroppedStacks", cancellable = true)
	private void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool.isOf(FloralisiaItems.AMETHYST_PICKAXE) && state.isOf(Blocks.BUDDING_AMETHYST)) {
			info.setReturnValue(ImmutableList.<ItemStack>builder()
				.addAll(info.getReturnValue()).add(new ItemStack(state.getBlock())).build());
		}
	}
}
