package com.zifang.common.properties.conf;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PropertyConfigCache {

	private final Properties configProperties = new Properties();
	private String envConfig = "local";
	public PropertyConfigCache() {
		String confUrl = System.getProperty("conf");
		System.out.print(confUrl);
		confUrl = confUrl==null?"/home/piday":confUrl;

		try {
			envConfig = System.getProperty("env");
			final Integer trimLength = "file://".length();
			final String trimmedUrl = confUrl.substring(trimLength);
			final File confDir = new File(trimmedUrl);
			populateCache(confDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class LazyHolder {
		private static final PropertyConfigCache INSTANCE = new PropertyConfigCache();
	}
	
	public static PropertyConfigCache getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String getProperty(String key) {
		return this.configProperties.getProperty(key);
	}

	public String getEnvConfig() {
		return this.envConfig == null ? "local" :this.envConfig;
	}
	
	private void  populateCache(final File folder) throws IOException{
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	populateCache(fileEntry);
	        } else {
	        	if(fileEntry.getName().endsWith(".properties")) {
		        	final FileReader reader = new FileReader(fileEntry);
		        	configProperties.load(reader);
	        	}
	        }
	    }
	}
}
