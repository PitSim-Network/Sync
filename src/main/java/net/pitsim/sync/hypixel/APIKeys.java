package net.pitsim.sync.hypixel;

import dev.kyro.arcticapi.data.AConfig;

public class APIKeys {
	public static String getAPIKey() {
		return AConfig.getString("hypixel-api-key");
	}
}
