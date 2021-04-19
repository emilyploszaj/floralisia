package dev.emi.floralisia.registry;

import java.util.ArrayList;
import java.util.List;

import dev.emi.floralisia.wrapper.PlayerEntityWrapper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class FloralisiaClientPackets {
	public static final Identifier AMETHYST_MONOCLE = new Identifier("floralisia", "amethyst_monocle");

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(AMETHYST_MONOCLE, (client, handler, buf, sender) -> {
			BlockPos pos = buf.readBlockPos();
			int length = buf.readInt();
			List<Text> text = new ArrayList<>(length);
			for (int i = 0; i < length; i++) {
				text.add(Text.Serializer.fromJson(buf.readString()));
			}
			client.execute(() -> {
				((PlayerEntityWrapper) client.player).updateAmethystMonocle(pos, text);
			});
		});
	}
}
