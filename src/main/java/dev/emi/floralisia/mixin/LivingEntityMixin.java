package dev.emi.floralisia.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.floralisia.FloralisiaMain;
import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.Trinket.SlotReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract void setHealth(float health);

	@Inject(at = @At("HEAD"), method = "tryUseTotem", cancellable = true)
	private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
		if (source.isOutOfWorld() || source == FloralisiaMain.SACRIFICE) return;
		if (((LivingEntity) (Object) this) instanceof PlayerEntity && this.world instanceof ServerWorld) {
			PlayerManager manager = ((ServerWorld) this.world).getServer().getPlayerManager(); 
			for (ServerPlayerEntity player : manager.getPlayerList()) {
				if (player == ((ServerPlayerEntity) (Object) this)) {
					continue;
				}
				Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
				if (optional.isPresent()) {
					TrinketComponent comp = optional.get();
					List<Pair<SlotReference, ItemStack>> list = comp.getEquipped(stack -> {
						if (!stack.isOf(FloralisiaItems.RING_OF_SAFEGUARDING)) return false;
						if (!stack.hasTag()) return false;
						NbtCompound tag = stack.getTag();
						return tag.contains("ProtectedPlayer") && tag.getUuid("ProtectedPlayer").equals(this.getUuid());
					});
					if (list.size() > 0) {
						ItemStack stack = list.get(0).getRight();
						NbtCompound tag = stack.getOrCreateTag();
						float max = player.getMaxHealth();
						float health = player.getHealth() / 2;
						if (tag.contains("Health")) {
							max += tag.getFloat("Health");
						}
						tag.putFloat("Health", max - health);
						tag.putInt("Cooldown", 20 * 60 * 7);
						if (health < 2f) {
							health *= 2;
							tag.remove("Cooldown");
							tag.remove("Health");
							player.damage(FloralisiaMain.SACRIFICE, 20f);
							((PlayerEntity) (Object) this).sendMessage(new TranslatableText("action.floralisia.ring_of_safeguarding.protected.sacrifice", player.getName()), false);
						} else {
							player.setHealth(health);
							player.sendMessage(new TranslatableText("action.floralisia.ring_of_safeguarding.sacrificed", this.getName()), false);
							((PlayerEntity) (Object) this).sendMessage(new TranslatableText("action.floralisia.ring_of_safeguarding.protected", player.getName()), false);
						}
						this.setHealth(health);
						info.setReturnValue(true);
					}
				}
			}
		}
	}
}
