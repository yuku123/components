package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.reflect.PackageScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 扫描驱动注册。发现打了 {@link Convert} 的类或静态方法并注册进对应 scope。
 * <p>
 * 显式触发而非 static 块自动跑——扫描的时机与顺序由调用方决定，重复扫描幂等。
 *
 * @author zifang
 */
public final class ConverterScanner {

    private static final Set<String> SCANNED = ConcurrentHashMap.newKeySet();

    private ConverterScanner() {
    }

    /**
     * 扫描包及其子包下的 {@link Convert} 标注者
     *
     * @return 本次实际注册的转换器条数
     */
    public static int scan(String... packageNames) {
        int registered = 0;
        for (String packageName : packageNames) {
            Set<Class<?>> classes = PackageScanner.searchClasses(packageName, ConverterScanner::hasConvert);
            for (Class<?> clazz : classes) {
                registered += registerClass(clazz);
            }
        }
        return registered;
    }

    /**
     * 注册单个类上的 {@link Convert}
     *
     * @return 本次实际注册的条数
     */
    public static int registerClass(Class<?> clazz) {
        int registered = 0;
        Convert classLevel = clazz.getAnnotation(Convert.class);
        if (classLevel != null && SCANNED.add(clazz.getName())) {
            registerFromClass(clazz, classLevel);
            registered++;
        }
        for (Method method : annotatedMethods(clazz)) {
            if (SCANNED.add(describe(method))) {
                registerFromMethod(clazz, method, method.getAnnotation(Convert.class));
                registered++;
            }
        }
        return registered;
    }

    private static boolean hasConvert(Class<?> clazz) {
        return clazz.getAnnotation(Convert.class) != null
                || !annotatedMethods(clazz).isEmpty();
    }

    private static List<Method> annotatedMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(Convert.class) != null) {
                methods.add(method);
            }
        }
        return methods;
    }

    @SuppressWarnings("unchecked")
    private static <F, T> void registerFromClass(Class<?> clazz, Convert annotation) {
        if (annotation.enabled()) {
            ConverterScope scope = scopeOf(annotation.scope());
            Converter<F, T> converter = (Converter<F, T>) instantiate(clazz);
            Converters.register(scope, (Class<F>) annotation.from(), (Class<T>) annotation.to(),
                    converter, annotation.priority());
        }
    }

    private static <F, T> void registerFromMethod(Class<?> owner, Method method, Convert annotation) {
        if (!annotation.enabled()) {
            return;
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalStateException("@Convert 只能标在静态方法上: " + describe(method));
        }
        if (method.getParameterCount() != 2
                || !method.getParameterTypes()[1].isAssignableFrom(ConvertContext.class)) {
            throw new IllegalStateException(
                    "@Convert 方法签名须为 (F value, ConvertContext context): " + describe(method));
        }
        method.setAccessible(true);
        Converter<F, T> converter = (value, context) -> {
            try {
                return (T) method.invoke(null, value, context);
            } catch (InvocationTargetException e) {
                throw asRuntime(e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("无法调用转换器方法: " + describe(method), e);
            }
        };
        Converters.register(scopeOf(annotation.scope()),
                (Class<F>) annotation.from(), (Class<T>) annotation.to(), converter, annotation.priority());
    }

    private static Object instantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "@Convert 类须有无参构造器且可访问: " + clazz.getName(), e);
        }
    }

    private static ConverterScope scopeOf(String name) {
        return Converters.defineScope(name);
    }

    private static RuntimeException asRuntime(Throwable target) {
        return target instanceof RuntimeException
                ? (RuntimeException) target
                : new ConvertException(target.getMessage(), null, null, target);
    }

    private static String describe(Method method) {
        return method.getDeclaringClass().getName() + "#" + method.getName();
    }
}