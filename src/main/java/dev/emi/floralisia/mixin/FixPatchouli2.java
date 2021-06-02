package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resource.language.I18n;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookLanding;
import vazkii.patchouli.common.book.Book;

@Mixin(GuiBookLanding.class)
public abstract class FixPatchouli2 {
	@Shadow
	BookTextRenderer text;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void ctor(Book book, CallbackInfo info) {

		text = new BookTextRenderer((GuiBookLanding) (Object) this, I18n.translate(book.landingText),
				GuiBook.LEFT_PAGE_X, GuiBook.TOP_PADDING + 25);

	}
}
