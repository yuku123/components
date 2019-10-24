package com.zifang.util.core.resource.analytic.args;

public class Config {

	public Config() {
		super();
	}

	public static String getProperty(final String key) {
		return PropertyConfigCache.getInstance().getProperty(key);
	}

	public static String getConfig() {
		return PropertyConfigCache.getInstance().getEnvConfig();
	}
}