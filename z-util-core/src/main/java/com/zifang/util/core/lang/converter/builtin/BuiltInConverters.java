package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.Converter;
import com.zifang.util.core.lang.converter.ConverterScope;

/**
 * 内置转换器装配入口。
 * <p>
 * 全部注册在 core scope 下。业务方自定义的转换器注册进子 scope，
 * 会在类型距离相同时凭 scope 优先级排在前面。
 *
 * @author zifang
 */
public final class BuiltInConverters {

    private BuiltInConverters() {
    }

    public static void registerAll(ConverterScope scope) {
        StringFamily.register(scope);
        NumberFamily.register(scope);
        TemporalFamily.register(scope);
        MiscFamily.register(scope);
        EnumFamily.register(scope);
    }

    static <F, T> void register(ConverterScope scope, Class<F> from, Class<T> to,
                                 Converter<F, T> converter, int priority) {
        com.zifang.util.core.lang.converter.Converters.register(scope, from, to, converter, priority);
    }

    static <F, T> void register(ConverterScope scope, Class<F> from, Class<T> to,
                                 Converter<F, T> converter) {
        register(scope, from, to, converter, 0);
    }
}