package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类型转换门面。
 *
 * <pre>
 *   严格轨：失败抛 {@link ConvertException}，异常内含候选链上每一次的失败原因
 *   宽松轨：失败返回调用方给定的默认值
 * </pre>
 *
 * null 输入恒返回 null（不视为失败）；非 null 输入转换后返回 null 视为该候选转换失败，
 * 注册表会记录原因并继续尝试链上的下一个，全部失败才抛异常——不存在静默返 null 的路径。
 *
 * @author zifang
 */
public class Converters {

    private static final ConverterRegistry REGISTRY = new ConverterRegistry();
    private static final ConcurrentMap<String, ConverterScope> SCOPES = new ConcurrentHashMap<>();

    static {
        SCOPES.put("core", new ConverterScope("core", null, Integer.MIN_VALUE));
        com.zifang.util.core.lang.converter.builtin.BuiltInConverters.registerAll(SCOPES.get("core"));
    }

    /**
     * 内部用：转换器把非 null 输入转成 null 当作失败，登记一条原因
     */
    static final class NullReturn extends RuntimeException {

        private static final long serialVersionUID = 1L;

        NullReturn() {
            super("转换器对非 null 输入返回了 null");
        }
    }

    private Converters() {
    }

    // ==================== 严格轨 ====================

    public static <T> T to(Object value, Class<T> type) {
        return to(value, type, ConvertPolicy.defaults());
    }

    public static <T> T to(Object value, Class<T> type, ConvertPolicy policy) {
        if (type == null) {
            throw new IllegalArgumentException("目标类型不能为空");
        }
        Class target = PrimitiveUtil.getPrimitiveWrapper(type);
        if (value == null) {
            return null;
        }
        Object unwrapped = value instanceof Optional ? ((Optional<?>) value).orElse(null) : value;
        if (unwrapped == null) {
            return null;
        }
        if (Optional.class.equals(target)) {
            return (T) Optional.ofNullable(to(unwrapped, Object.class, policy));
        }
        if (isBlank(unwrapped, policy)) {
            return null;
        }
        return (T) target.cast(runChain(unwrapped, target, new ConvertContext(policy, unwrapped.getClass(), target)));
    }

    /**
     * 空串语义在门面统一裁决，转换器只管拿到非空串。
     */
    private static boolean isBlank(Object value, ConvertPolicy policy) {
        if (!(value instanceof String)) {
            return false;
        }
        String text = (String) value;
        if (!text.trim().isEmpty()) {
            return false;
        }
        switch (policy.getBlankPolicy()) {
            case AS_NULL:
                return true;
            case FAIL:
                throw new ConvertException(text, String.class, Object.class, "空白串不允许参与转换");
            case KEEP:
            default:
                return false;
        }
    }

    /**
     * 按目标泛型参数转换集合中的每个元素，元素级失败会在异常里带出下标
     */
    public static <T> List<T> toList(Object value, Class<T> elementType, ConvertPolicy policy) {
        if (!(value instanceof List<?>)) {
            throw new ConvertException(value, value == null ? null : value.getClass(), List.class,
                    "期望 List 入参");
        }
        List<?> raw = (List<?>) value;
        List<T> result = new ArrayList<>(raw.size());
        ConvertContext root = new ConvertContext(policy, raw.getClass(), List.class);
        for (int i = 0; i < raw.size(); i++) {
            result.add(element(raw.get(i), elementType, root, "[" + i + "]"));
        }
        return result;
    }

    public static <T> Set<T> toSet(Object value, Class<T> elementType, ConvertPolicy policy) {
        return new LinkedHashSet<>(toList(value, elementType, policy));
    }

    public static <T> T[] toArray(Object value, Class<T> componentType, ConvertPolicy policy) {
        if (!(value instanceof Collection<?>)) {
            throw new ConvertException(value, value == null ? null : value.getClass(), Object[].class,
                    "期望 Collection 入参");
        }
        Collection<?> raw = (Collection<?>) value;
        List<T> converted = toList(new ArrayList<>(raw), componentType, policy);
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(componentType, converted.size());
        return converted.toArray(array);
    }

    public static <K, V> Map<K, V> toMap(Object value, Class<K> keyType, Class<V> valueType, ConvertPolicy policy) {
        if (!(value instanceof Map<?, ?>)) {
            throw new ConvertException(value, value == null ? null : value.getClass(), Map.class,
                    "期望 Map 入参");
        }
        Map<?, ?> raw = (Map<?, ?>) value;
        Map<K, V> result = new LinkedHashMap<>(Math.max(16, raw.size() * 2));
        ConvertContext root = new ConvertContext(policy, raw.getClass(), Map.class);
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            K key = element(entry.getKey(), keyType, root, "key[" + entry.getKey() + "]");
            V converted = element(entry.getValue(), valueType, root, "value[" + entry.getKey() + "]");
            result.put(key, converted);
        }
        return result;
    }

    private static <T> T element(Object raw, Class<T> expectedType, ConvertContext root, String segment) {
        if (raw == null) {
            return null;
        }
        Class target = PrimitiveUtil.getPrimitiveWrapper(expectedType);
        return (T) target.cast(runChain(raw, target, root.child(raw.getClass(), target, segment)));
    }

    @SuppressWarnings("unchecked")
    private static Object runChain(Object value, Class<?> target, ConvertContext context) {
        if (target.isInstance(value)) {
            return target.cast(value);
        }
        Class<?> source = value.getClass();
        List<ConverterEntry<?, ?>> chain = REGISTRY.chain(source, target);
        List<ConvertException.Attempt> attempts = new ArrayList<>(chain.size());

        for (ConverterEntry<?, ?> entry : chain) {
            Converter<Object, Object> converter = (Converter<Object, Object>) (Converter<?, ?>) entry.getConverter();
            try {
                Object result = converter.convert(value, context);
                if (result == null) {
                    attempts.add(new ConvertException.Attempt(entry.getDescription(),
                            entry.getScope().getName(), new NullReturn()));
                    continue;
                }
                if (!target.isInstance(result)) {
                    attempts.add(new ConvertException.Attempt(entry.getDescription(),
                            entry.getScope().getName(),
                            new ConvertException(result, source, target, "转换结果类型不符")));
                    continue;
                }
                return target.cast(result);
            } catch (RuntimeException e) {
                attempts.add(new ConvertException.Attempt(entry.getDescription(),
                        entry.getScope().getName(), e));
            }
        }

        Throwable lastCause = attempts.isEmpty() ? null : attempts.get(attempts.size() - 1).getCause();
        throw new ConvertException(value, source, target, context.getPath(), attempts, lastCause);
    }

    // ==================== 宽松轨 ====================

    public static <T> T toOrDefault(Object value, Class<T> type, T defaultValue) {
        return toOrDefault(value, type, defaultValue, ConvertPolicy.defaults());
    }

    public static <T> T toOrDefault(Object value, Class<T> type, T defaultValue, ConvertPolicy policy) {
        try {
            return to(value, type, policy);
        } catch (ConvertException e) {
            return defaultValue;
        }
    }

    // ==================== 注册 ====================

    public static <F, T> void register(Class<F> from, Class<T> to, Converter<F, T> converter) {
        register(scope("core"), from, to, converter, 0);
    }

    public static <F, T> void register(ConverterScope scope, Class<F> from, Class<T> to, Converter<F, T> converter) {
        register(scope, from, to, converter, 0);
    }

    public static <F, T> void register(ConverterScope scope, Class<F> from, Class<T> to,
                                       Converter<F, T> converter, int priority) {
        REGISTRY.register(scope, from, to, converter, priority);
    }

    public static boolean unregister(String scopeName, Class<?> from, Class<?> to) {
        ConverterScope scope = SCOPES.get(scopeName);
        return scope != null && REGISTRY.unregister(scope, from, to);
    }

    public static int unregisterScope(String scopeName) {
        ConverterScope scope = SCOPES.get(scopeName);
        return scope == null ? 0 : REGISTRY.unregisterScope(scope);
    }

    // ==================== scope ====================

    public static ConverterScope defineScope(String name) {
        return defineScope(name, null, 0);
    }

    public static ConverterScope defineScope(String name, String parentName, int priority) {
        ConverterScope existing = SCOPES.get(name);
        if (existing != null) {
            return existing;
        }
        ConverterScope parent = parentName == null ? null : requireScope(parentName);
        ConverterScope scope = new ConverterScope(name, parent, priority);
        SCOPES.put(name, scope);
        return scope;
    }

    public static ConverterScope scope(String name) {
        return requireScope(name);
    }

    public static ConverterScope requireScope(String name) {
        ConverterScope scope = SCOPES.get(name);
        if (scope == null) {
            throw new IllegalArgumentException("scope 不存在: " + name);
        }
        return scope;
    }

    public static List<ConverterScope> scopes() {
        List<ConverterScope> all = new ArrayList<>(SCOPES.values());
        all.sort((a, b) -> a.getName().compareTo(b.getName()));
        return Collections.unmodifiableList(all);
    }

    public static void enableScope(String name) {
        setScopeEnabled(name, true);
    }

    public static void disableScope(String name) {
        setScopeEnabled(name, false);
    }

    private static void setScopeEnabled(String name, boolean enabled) {
        ConverterScope scope = requireScope(name);
        scope.setEnabled(enabled);
        // 启用态参与候选链解析，注册表缓存需失效
        REGISTRY.clearResolved();
    }

    // ==================== 自省 ====================

    public static List<ConverterEntry<?, ?>> entries() {
        return REGISTRY.entries();
    }

    /**
     * 注册表快照文本，用于排查"这个类型对最终由谁接"
     */
    public static String describe() {
        return REGISTRY.describe();
    }

    /**
     * 某个类型对的命中链路：候选顺序与排序依据，用于排查"这个类型对最终由谁接"
     */
    public static String explain(Class<?> from, Class<?> to) {
        return REGISTRY.explain(from, to);
    }

    /**
     * 注册表句柄，供需要自省候选链的调用方使用
     */
    public static ConverterRegistry registry() {
        return REGISTRY;
    }
}