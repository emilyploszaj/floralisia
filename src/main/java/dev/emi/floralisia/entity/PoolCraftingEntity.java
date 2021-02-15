package dev.emi.floralisia.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class PoolCraftingEntity extends Entity {
	public List<ItemStack> results = new ArrayList<ItemStack>();
	public int duration;

	public PoolCraftingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		for (int i = 0; i < 15; i++) {
			double xOff = world.getRandom().nextDouble() - 0.5;
			double yOff = world.getRandom().nextDouble() - 0.5;
			double zOff = world.getRandom().nextDouble() - 0.5;
			world.addImportantParticle((ParticleEffect) ParticleTypes.POOF, getX() + xOff, getY() + yOff, getZ() + zOff, 0.0F, 0.0F, 0.0F);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (duration == 20) {
			world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, 1.0F, 2.0F, false);
		}
		if (duration <= 0) {
			world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS,
					0.5F, 0.7F, false);
			if (!this.world.isClient) {
				for (ItemStack stack : results) {
					ItemEntity entity = new ItemEntity(world, getX(), getY(), getZ(), stack.copy());
					entity.setVelocity(0.1F * (world.getRandom().nextFloat() - 0.5F), 0.5F,
							0.1F * (world.getRandom().nextFloat() - 0.5F));
					entity.setToDefaultPickupDelay();
					world.spawnEntity(entity);
				}
			}
			this.remove(RemovalReason.DISCARDED);
		}
		if (this.world.isClient) {
			float completion = duration / 20.0F;
			double xOff = Math.sin(duration / 2F - world.getTime() / 200F);
			double zOff = Math.cos(duration / 2F - world.getTime() / 200F);
			world.addImportantParticle((ParticleEffect) ParticleTypes.HAPPY_VILLAGER, getX() + xOff * completion * 2.0f,
					getY(), getZ() + zOff * completion * 2.0f, 0.1F, 0.1F, 0.1F);
			xOff = world.getRandom().nextDouble() - 0.5;
			zOff = world.getRandom().nextDouble() - 0.5;
			world.addImportantParticle((ParticleEffect) ParticleTypes.BUBBLE, getX() + xOff, getY() - 1.0F,
					getZ() + zOff, 0.1F, 0.1F, 0.1F);
		}
		duration--;
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		this.duration = tag.getInt("Duration");
		ListTag list = tag.getList("Results", 10);
		for (int i = 0; i < list.size(); i++) {
			results.add(ItemStack.fromTag(list.getCompound(i)));
		}
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		tag.putInt("Duration", duration);
		ListTag list = new ListTag();
		for (ItemStack stack : results) {
			list.add(stack.toTag(new CompoundTag()));
		}
		tag.put("Results", list);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
}