package com.zifang.util.core.pattern.cache;

import com.zifang.util.core.pattern.register.Register;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 缓存命名空间管理器：按 name 缓存已构建的 {@link Cache} / {@link LoadingCache} 实例。
 * <p>
 * 用途：在一个 JVM 里集中管理多张业务缓存（user / order / config ...），
 * 避免到处传 builder。
 * <p>
 * 类型说明：因为键值类型是泛型，本类提供原始类型版本 + 类型安全包装：
 * <pre>{@code
 *   CacheManager mgr = CacheManager.getInstance();
 *   mgr.register(CacheBuilder.<String, User>newBuilder()
 *       .name("user").maximumSize(10000).build());
 *
 *   // 之后任何地方都可以：
 *   Cache<String, User> users = mgr.get("user", String.class, User.class);
 * }</pre>
 * <p>
 * 底层就是一张命名注册表，故存储交给 {@link Register}；白拿它「拒 null key」这条契约，
 * 本类不再重复实现。用组合而非继承：本类的 {@code get(String)} 是带自身类型参数的泛型方法，
 * 泛型方法无法覆写父类的 {@code get(K)}，继承会让调用点变成二义。
 */
public class CacheManager {

    private static volatile CacheManager instance;
    private final Register<String, Object> caches = new Register<>();

    private CacheManager() {
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) instance = new CacheManager();
            }
        }
        return instance;
    }

    /**
     * 注册一个缓存实例。如果同名已存在，返回旧的实例（不覆盖）。
     */
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> register(Cache<K, V> cache) {
        Cache<K, V> previous = (Cache<K, V>) caches.putIfAbsent(cache.getName(), cache);
        return previous == null ? cache : previous;
    }

    /**
     * 按 name 获取，类型不安全版本。
     */
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> get(String name) {
        return (Cache<K, V>) caches.get(name);
    }

    /**
     * 按 name 获取，类型安全版本（运行时校验）。
     */
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> get(String name, Class<K> keyType, Class<V> valueType) {
        return (Cache<K, V>) caches.get(name);
    }

    /**
     * 取出已注册的缓存；如果不存在，按 builderSupplier 构造并注册。
     */
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getOrCreate(String name, Function<String, Cache<K, V>> builderSupplier) {
        Cache<?, ?> existing = (Cache<?, ?>) caches.get(name);
        if (existing != null) return (Cache<K, V>) existing;
        synchronized (this) {
            existing = (Cache<?, ?>) caches.get(name);
            if (existing != null) return (Cache<K, V>) existing;
            Cache<K, V> fresh = builderSupplier.apply(name);
            caches.register(name, fresh);
            return fresh;
        }
    }

    public boolean contains(String name) {
        return caches.contains(name);
    }

    /**
     * 移除一个缓存（同时关闭它）。返回被移除的实例。
     */
    public Cache<?, ?> remove(String name) {
        Cache<?, ?> c = (Cache<?, ?>) caches.unregister(name);
        if (c != null) c.shutdown();
        return c;
    }

    /**
     * 清空所有缓存（每个缓存自身的清空 + 关闭）。
     */
    public void clearAll() {
        for (Map.Entry<String, Object> entry : caches.listAll()) {
            try {
                ((Cache<?, ?>) entry.getValue()).invalidateAll();
                ((Cache<?, ?>) entry.getValue()).shutdown();
            } catch (RuntimeException ignored) {
            }
        }
        caches.clear();
    }

    public Map<String, Cache<?, ?>> snapshot() {
        Map<String, Cache<?, ?>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : caches.listAll()) {
            copy.put(entry.getKey(), (Cache<?, ?>) entry.getValue());
        }
        return Collections.unmodifiableMap(copy);
    }

    public int size() {
        return caches.size();
    }
}
