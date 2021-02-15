package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.registry.FloralisiaItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.ItemModBook;

@Mixin(ItemModBook.class)
public class ItemModBookMixin {
	private static final Identifier BOOK_ID = new Identifier("floralisia", "floralisia");
	
	@Inject(at = @At("HEAD"), method = "getBook", cancellable = true)
	private static void getBook(ItemStack stack, CallbackInfoReturnable<Book> info) {
		if (stack.getItem() == FloralisiaItems.GUIDE_BOOK) {
			info.setReturnValue(BookRegistry.INSTANCE.books.get(BOOK_ID));
		}
	}
}
