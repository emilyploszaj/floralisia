package dev.emi.floralisia.mixin;

import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {
	@Unique
	private static final Set<UUID> FLORALISIA_CAPE_OWNERS = ImmutableSet.of(UUID.fromString("d9cb8eaa-5b7c-4f06-b0f4-081b5b79423a"));
	@Unique
	private static final Identifier FLORALISIA_CAPE = new Identifier("floralisia", "textures/cape/floralisia_cape.png");

	@Shadow @Final
	private GameProfile profile;

	@Inject(at = @At("HEAD"), method = "getCapeTexture", cancellable = true)
	private void getCapeTexture(CallbackInfoReturnable<Identifier> info) {
		/* unless :flushed:
		if (FLORALISIA_CAPE_OWNERS.contains(profile.getId())) {
			info.setReturnValue(FLORALISIA_CAPE);
		}*/
	}
}
