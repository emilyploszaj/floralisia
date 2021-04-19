package dev.emi.floralisia.wrapper;

import java.util.List;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public interface PlayerEntityWrapper {

	boolean isProxiedGlove();

	void setProxiedGlove(boolean b);

	void updateAmethystMonocle(BlockPos pos, List<Text> text);

	void updateAmethystMonocle(ServerPlayerEntity player);

	BlockPos getLastPos();

	List<Text> getLastText();
}
