package dev.emi.floralisia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.floralisia.entity.PoolCraftingEntity;
import dev.emi.floralisia.registry.FloralisiaEntities;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin{
	@Shadow
	private ClientWorld world;

	@Inject(at = @At(value = "TAIL"), method = "onEntitySpawn")
	public void assignProxy(EntitySpawnS2CPacket packet, CallbackInfo info){
		if(packet.getEntityTypeId() == FloralisiaEntities.POOL_CRAFTING_ENTITY){
			PoolCraftingEntity entity = new PoolCraftingEntity(FloralisiaEntities.POOL_CRAFTING_ENTITY, world);
			entity.setPos(packet.getX(), packet.getY(), packet.getZ());
			entity.duration = 20;
			entity.setEntityId(packet.getId());
			entity.setUuid(packet.getUuid());
			world.addEntity(packet.getId(), entity);
		}
	}
}