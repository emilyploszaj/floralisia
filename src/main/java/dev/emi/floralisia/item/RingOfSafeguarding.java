package dev.emi.floralisia.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Multimap;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class RingOfSafeguarding extends TrinketItem {

	public RingOfSafeguarding(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (entity instanceof PlayerEntity) {
			if (!user.world.isClient) {
				NbtCompound tag = stack.getOrCreateTag();
				tag.putUuid("ProtectedPlayer", entity.getUuid());
				tag.putString("ProtectedName", entity.getName().asString());
				user.sendMessage(new TranslatableText("action.floralisia.ring_of_safeguarding.bound", entity.getName()), true);
			}
			return ActionResult.SUCCESS;
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		NbtCompound tag = stack.getOrCreateTag();
		if (tag.contains("ProtectedName")) {
			tooltip.add(new TranslatableText("tooltip.floralisia.ring_of_safeguarding.bound", new LiteralText(tag.getString("ProtectedName")))
				.formatted(Formatting.GRAY));
		}
		if (tag.contains("Cooldown")) {
			tooltip.add(new TranslatableText("tooltip.floralisia.ring_of_safeguarding.cooldown", 
				new LiteralText("" + (tag.getInt("Cooldown") + 10) / 20)).formatted(Formatting.RED));
		}
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		if (!entity.world.isClient) {
			NbtCompound tag = stack.getOrCreateTag();
			if (tag.contains("Cooldown")) {
				int cooldown = tag.getInt("Cooldown") - 1;
				if (cooldown < 0) {
					tag.remove("Cooldown");
					tag.remove("Health");
					if (entity instanceof PlayerEntity) {
						((PlayerEntity) entity).sendMessage(new TranslatableText("action.floralisia.ring_of_safeguarding.restored"), false);
					}
				} else {
					tag.putInt("Cooldown", cooldown);
				}
			}
		}
	}

	@Override
	public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
		NbtCompound tag = stack.getOrCreateTag();
		if (tag.contains("Cooldown") && tag.getInt("Cooldown") > 0) {
			return false;
		}
		return super.canUnequip(stack, slot, entity);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> map = super.getModifiers(stack, slot, entity, uuid);
		NbtCompound tag = stack.getOrCreateTag();
		if (tag.contains("Cooldown")) {
			if (tag.getInt("Cooldown") > 0) {
				float health = 10;
				if (tag.contains("Health")) {
					health = tag.getFloat("Health");
				}
				map.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(uuid, "Health Decrease", -health, Operation.ADDITION));
			}
		}
		return map;
	}
}
