package dev.emi.floralisia.config;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.fabricmc.loader.api.FabricLoader;

public class FloralisiaConfigLoader {
	private static final Jankson JANKSON = Jankson.builder().build();

	public static FloralisiaConfig load() {
		File f = new File(FabricLoader.getInstance().getConfigDir().toFile(), "floralisia.json5");
		try {
			FloralisiaConfig config;
			if (f.exists()) {
				JsonObject object = JANKSON.load(f);
				config = JANKSON.fromJson(object, FloralisiaConfig.class);
			} else {
				config = new FloralisiaConfig();
			}
			JsonObject object = (JsonObject) JANKSON.toJson(config);
			FileUtils.writeStringToFile(f, object.toJson(JsonGrammar.JANKSON), Charset.defaultCharset());
			return config;
		} catch (SyntaxError error) {
			System.err.println("[floralisia] Failed to prase config file");
			System.err.println(error.getMessage());
			System.err.println(error.getLineMessage());
		} catch (Exception e) {
			System.err.println("[floralisia] Failed to read config file");
			e.printStackTrace();
		}
		return new FloralisiaConfig();
	}
}
