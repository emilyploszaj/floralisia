package dev.emi.floralisia.config;

import blue.endless.jankson.Comment;

public class FloralisiaConfig {

	@Comment("Whether pipes are enabled or not")
	public boolean pipes = true;

	@Comment("Whether the amethyst starter has durability and can be broken")
	public boolean amethystStarterDurability = true;

	@Comment("Whether the amethyst clippers have durability and can be broken")
	public boolean amethystClipperDurability = true;

	@Comment("The damage multiplier tools take when used in a breaker")
	public int breakerDamageMultiplier = 4;
}
