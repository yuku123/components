package com.zifang.util.core.io;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 配置相关的一些辅助类
 */
public class ConfigUtil {

    /**
     * 获取配置文件资源
     */
    public static URL findAsResource(String path) {
        URL url = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            url = contextClassLoader.getResource(path);
        }
        if (url != null) {
            return url;
        }
        url = ConfigUtil.class.getClassLoader().getResource(path);
        if (url != null) {
            return url;
        }
        url = ClassLoader.getSystemClassLoader().getResource(path);

        return url;
    }

    public static String resourcePath(String path) {
        URL asResource = findAsResource(path);
        return new File(asResource.getFile()).getPath();
    }


    private static InputStream getConfigStream(final String path) throws RuntimeException {
        try {
            URL url = new URL(path);
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException("Unable to open config file: " + path);
        }
    }

    private static InputStream resourceStream(final String path) throws IOException {
        URL asResource = findAsResource(path);
        return asResource.openStream();
    }

    public static Properties getConfigProperties(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(resourceStream(path));
        return properties;
    }


}
