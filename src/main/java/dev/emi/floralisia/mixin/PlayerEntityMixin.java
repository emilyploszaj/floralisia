package dev.emi.floralisia.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import dev.emi.floralisia.block.AmethystMonocleProvider;
import dev.emi.floralisia.mixin.accessor.ItemAccessor;
import dev.emi.floralisia.registry.FloralisiaClientPackets;
import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext.FluidHandling;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityWrapper {
	@Unique
	private boolean proxiedGlove;
	@Unique
	private BlockPos lastPos;
	@Unique
	private List<Text> lastText;

	@Override
	public boolean isProxiedGlove() {
		return proxiedGlove;
	}

	@Override
	public void setProxiedGlove(boolean b) {
		proxiedGlove = b;
	}

	@Override
	public void updateAmethystMonocle(BlockPos pos, List<Text> text) {
		lastPos = pos;
		lastText = text;
	}

	@Override
	public void updateAmethystMonocle(ServerPlayerEntity player) {
		BlockHitResult hit = ItemAccessor.invokeRaycast(player.world, player, FluidHandling.NONE);
		if (hit.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = hit.getBlockPos();
			BlockState state = player.world.getBlockState(pos);
			if (state.getBlock() instanceof AmethystMonocleProvider) {
				AmethystMonocleProvider provider = ((AmethystMonocleProvider) state.getBlock());
				if (provider.requiresSync()) {
					List<Text> text = provider.getMonocleText(player.world, pos, state);
					if (!pos.equals(lastPos) || !text.equals(lastText)) {
						PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
						buf.writeBlockPos(pos);
						buf.writeInt(text.size());
						for (int i = 0; i < text.size(); i++) {
							buf.writeString(Text.Serializer.toJson(text.get(i)));
						}
						ServerPlayNetworking.send(player, FloralisiaClientPackets.AMETHYST_MONOCLE, buf);
						lastPos = pos;
						lastText = text;
					}
				}
			}
		}
	}

	@Override
	public BlockPos getLastPos() {
		return lastPos;
	}

	@Override
	public List<Text> getLastText() {
		return lastText;
	}
}
