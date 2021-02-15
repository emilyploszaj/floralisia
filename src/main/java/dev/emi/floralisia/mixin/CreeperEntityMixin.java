package dev.emi.floralisia.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.floralisia.registry.FloralisiaItems;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.Trinket.SlotReference;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends LivingEntity {
	@Shadow
	private int explosionRadius;

	protected CreeperEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("HEAD"), method = "explode")
	private void explode(CallbackInfo info) {
		if (!this.world.isClient) {
			Box box = new Box(this.getX() - 10, this.getY() - 10, this.getZ() - 10, this.getX() + 10, this.getY() + 10, this.getZ() + 10);
			List<PlayerEntity> players = this.world.getEntitiesByClass(PlayerEntity.class, box, player -> {
				Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
				if (optional.isPresent()) {
					TrinketComponent comp = optional.get();
					return comp.isEquipped(stack -> {
						return stack.getItem() == FloralisiaItems.CREEPER_CHARM && stack.getDamage() == 0;
					});
				}
				return false;
			});
			if (players.size() > 0) {
				this.explosionRadius = 0;
				PlayerEntity player = players.get(0);
				TrinketsApi.getTrinketComponent(player).ifPresent(comp -> {
					List<Pair<SlotReference, ItemStack>> stacks = comp.getEquipped(stack -> {
						return stack.getItem() == FloralisiaItems.CREEPER_CHARM && stack.getDamage() == 0;
					});
					if (stacks.size() > 0) {
						stacks.get(0).getRight().setDamage(1);
					}
				});
			}
		}
	}
}
