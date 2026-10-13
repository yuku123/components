package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertException;
import com.zifang.util.core.lang.converter.ConverterScope;

import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * 值类型族：UUID / URI / URL / Class / BigInteger 字面量。
 *
 * @author zifang
 */
public final class MiscFamily {

    private MiscFamily() {
    }

    public static void register(ConverterScope scope) {
        BuiltInConverters.register(scope, String.class, UUID.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            try {
                return UUID.fromString(text);
            } catch (IllegalArgumentException e) {
                throw new ConvertException(value, String.class, UUID.class,
                        "不是合法 UUID: " + text);
            }
        });

        BuiltInConverters.register(scope, String.class, URI.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            try {
                return new URI(text);
            } catch (java.net.URISyntaxException e) {
                throw new ConvertException(value, String.class, URI.class,
                        "不是合法 URI: " + e.getReason());
            }
        });

        BuiltInConverters.register(scope, String.class, URL.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            try {
                return new URL(text);
            } catch (java.net.MalformedURLException e) {
                throw new ConvertException(value, String.class, URL.class, "不是合法 URL: " + text);
            }
        });

        BuiltInConverters.register(scope, String.class, Class.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = MiscFamily.class.getClassLoader();
            }
            try {
                return Class.forName(text, false, loader);
            } catch (ClassNotFoundException e) {
                throw new ConvertException(value, String.class, Class.class, "类不存在: " + text);
            }
        });
    }
}