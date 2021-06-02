package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import vazkii.patchouli.client.base.ClientProxy;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.client.gui.GoVoteHandler;
import vazkii.patchouli.client.handler.BookRightClickHandler;
import vazkii.patchouli.client.handler.MultiblockVisualizationHandler;
import vazkii.patchouli.client.shader.ShaderHelper;
import vazkii.patchouli.common.base.Patchouli;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.ItemModBook;
import vazkii.patchouli.common.item.PatchouliItems;
import vazkii.patchouli.common.network.NetworkHandler;

@Mixin(ClientProxy.class)
public class FixPatchouli {
	
	@Overwrite
	public void onInitializeClient() {
		GoVoteHandler.init();
		ClientBookRegistry.INSTANCE.init();
		PersistentData.setup();
		ClientTicker.init();
		BookRightClickHandler.init();
		MultiblockVisualizationHandler.init();
		NetworkHandler.registerMessages();
		Patchouli.reloadBookHandler = ClientBookRegistry.INSTANCE::reload;
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ShaderHelper.INSTANCE);

		ModelLoadingRegistry.INSTANCE.registerAppender((manager, register) -> {
			BookRegistry.INSTANCE.books.values().stream()
					.map(b -> new ModelIdentifier(b.model, "inventory"))
					.forEach(register);
		});

		FabricModelPredicateProviderRegistry.register(PatchouliItems.book,
				new Identifier("completion"),
				(stack, world, entity, seed) -> ItemModBook.getCompletion(stack));
	}
}
