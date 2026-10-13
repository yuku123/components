package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 转换器注册表：按 scope 管理注册项，并为类型对解析出**确定性有序的候选链**。
 * <p>
 * 候选链排序：类型距离升序 → scope 优先级降序 → 转换器权重降序 → 注册序号升序。
 * 全序且不依赖 Map 遍历序，因此同一组注册在不同 JVM 上解析结果一致。
 *
 * @author zifang
 */
public class ConverterRegistry {

    private final ConcurrentMap<ConverterScope, ConcurrentMap<Key, List<ConverterEntry<?, ?>>>> byScope =
            new ConcurrentHashMap<>();
    private final ConcurrentMap<Key, List<ConverterEntry<?, ?>>> resolved = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    private static final class Key {

        private final Class<?> from;
        private final Class<?> to;

        Key(Class<?> from, Class<?> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key)) {
                return false;
            }
            Key other = (Key) o;
            return from.equals(other.from) && to.equals(other.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return from.getName() + "->" + to.getName();
        }
    }

    public <F, T> void register(ConverterScope scope, Class<F> from, Class<T> to,
                               Converter<F, T> converter, int priority) {
        Objects.requireNonNull(scope, "scope 不能为空");
        Objects.requireNonNull(from, "from 不能为空");
        Objects.requireNonNull(to, "to 不能为空");
        Objects.requireNonNull(converter, "converter 不能为空");

        Class<?> normalizedFrom = PrimitiveUtil.getPrimitiveWrapper(from);
        Class<?> normalizedTo = PrimitiveUtil.getPrimitiveWrapper(to);

        ConverterEntry<?, ?> entry = new ConverterEntry(scope, normalizedFrom, normalizedTo,
                converter, priority, sequence.getAndIncrement());

        ConcurrentMap<Key, List<ConverterEntry<?, ?>>> scopeMap = byScope.get(scope);
        if (scopeMap == null) {
            scopeMap = new ConcurrentHashMap<>();
            ConcurrentMap<Key, List<ConverterEntry<?, ?>>> existing = byScope.putIfAbsent(scope, scopeMap);
            if (existing != null) {
                scopeMap = existing;
            }
        }
        Key key = new Key(normalizedFrom, normalizedTo);
        scopeMap.compute(key, (k, old) -> {
            List<ConverterEntry<?, ?>> list = old == null
                    ? new ArrayList<>()
                    : new ArrayList<>(old);
            list.add(entry);
            return list;
        });
        resolved.clear();
    }

    public boolean unregister(ConverterScope scope, Class<?> from, Class<?> to) {
        ConcurrentMap<Key, List<ConverterEntry<?, ?>>> scopeMap = byScope.get(scope);
        if (scopeMap == null) {
            return false;
        }
        Key key = new Key(PrimitiveUtil.getPrimitiveWrapper(from), PrimitiveUtil.getPrimitiveWrapper(to));
        List<ConverterEntry<?, ?>> removed = scopeMap.remove(key);
        resolved.clear();
        return removed != null && !removed.isEmpty();
    }

    /**
     * 摘除某 scope 名下的全部注册项（scope 本身保留）
     */
    public int unregisterScope(ConverterScope scope) {
        ConcurrentMap<Key, List<ConverterEntry<?, ?>>> removed = byScope.remove(scope);
        resolved.clear();
        if (removed == null) {
            return 0;
        }
        int count = 0;
        for (List<ConverterEntry<?, ?>> list : removed.values()) {
            count += list.size();
        }
        return count;
    }

    /**
     * 解析类型对的候选链，按 CHAIN_ORDER 全序排序；无候选返回空列表
     */
    public List<ConverterEntry<?, ?>> chain(Class<?> from, Class<?> to) {
        Class<?> normalizedFrom = PrimitiveUtil.getPrimitiveWrapper(from);
        Class<?> normalizedTo = PrimitiveUtil.getPrimitiveWrapper(to);
        Key key = new Key(normalizedFrom, normalizedTo);
        List<ConverterEntry<?, ?>> cached = resolved.get(key);
        if (cached != null) {
            return cached;
        }

        List<ConverterEntry<?, ?>> candidates = new ArrayList<>();
        for (Map.Entry<ConverterScope, ConcurrentMap<Key, List<ConverterEntry<?, ?>>>> scopeEntry : byScope.entrySet()) {
            if (!scopeEntry.getKey().isActive()) {
                continue;
            }
            for (Map.Entry<Key, List<ConverterEntry<?, ?>>> typeEntry : scopeEntry.getValue().entrySet()) {
                Key registered = typeEntry.getKey();
                if (registered.from.isAssignableFrom(normalizedFrom) && registered.to.isAssignableFrom(normalizedTo)) {
                    candidates.addAll(typeEntry.getValue());
                }
            }
        }
        sort(candidates, normalizedFrom, normalizedTo);
        List<ConverterEntry<?, ?>> chain = Collections.unmodifiableList(candidates);
        resolved.put(key, chain);
        return chain;
    }

    private void sort(List<ConverterEntry<?, ?>> candidates, Class<?> from, Class<?> to) {
        Collections.sort(candidates, new Comparator<ConverterEntry<?, ?>>() {
            @Override
            public int compare(ConverterEntry<?, ?> a, ConverterEntry<?, ?> b) {
                int byDistance = Integer.compare(a.distanceTo(from, to), b.distanceTo(from, to));
                if (byDistance != 0) {
                    return byDistance;
                }
                int byScope = Integer.compare(b.getScope().getPriority(), a.getScope().getPriority());
                if (byScope != 0) {
                    return byScope;
                }
                int byPriority = Integer.compare(b.getPriority(), a.getPriority());
                if (byPriority != 0) {
                    return byPriority;
                }
                return Long.compare(a.getSequence(), b.getSequence());
            }
        });
    }

    public void clear() {
        byScope.clear();
        resolved.clear();
    }

    /**
     * 仅失效候选链缓存，注册项不变（scope 启停用）
     */
    public void clearResolved() {
        resolved.clear();
    }

    /**
     * 全量注册项快照，按 scope 声明顺序输出
     */
    public List<ConverterEntry<?, ?>> entries() {
        List<ConverterEntry<?, ?>> all = new ArrayList<>();
        for (Map.Entry<ConverterScope, ConcurrentMap<Key, List<ConverterEntry<?, ?>>>> scopeEntry : byScope.entrySet()) {
            for (List<ConverterEntry<?, ?>> list : scopeEntry.getValue().values()) {
                all.addAll(list);
            }
        }
        all.sort(new Comparator<ConverterEntry<?, ?>>() {
            @Override
            public int compare(ConverterEntry<?, ?> a, ConverterEntry<?, ?> b) {
                int byScope = a.getScope().getName().compareTo(b.getScope().getName());
                return byScope != 0 ? byScope : Long.compare(a.getSequence(), b.getSequence());
            }
        });
        return all;
    }

    /**
     * 可读的注册表快照：先按父子关系打出 scope 树，再在每个节点下列出注册项
     */
    public String describe() {
        StringBuilder sb = new StringBuilder();
        List<ConverterEntry<?, ?>> all = entries();
        sb.append("ConverterRegistry 共 ").append(all.size()).append(" 条注册项，")
                .append(byScope.size()).append(" 个 scope\n");
        Set<ConverterScope> roots = new LinkedHashSet<>();
        for (ConverterScope scope : byScope.keySet()) {
            if (scope.getParent() == null) {
                roots.add(scope);
            }
        }
        List<ConverterScope> sorted = new ArrayList<>(roots);
        sorted.sort(Comparator.comparing(ConverterScope::getName));
        for (ConverterScope root : sorted) {
            appendScope(sb, root, all);
        }
        return sb.toString();
    }

    private void appendScope(StringBuilder sb, ConverterScope scope, List<ConverterEntry<?, ?>> all) {
        List<ConverterEntry<?, ?>> owned = new ArrayList<>();
        for (ConverterEntry<?, ?> entry : all) {
            if (entry.getScope() == scope) {
                owned.add(entry);
            }
        }
        sb.append("  ").append(scope.getName())
                .append("  (priority=").append(scope.getPriority())
                .append(", enabled=").append(scope.isEnabled())
                .append(", 自身 ").append(owned.size()).append(" 条")
                .append(scope.getChildren().isEmpty() ? ")\n" : ")\n");
        for (ConverterEntry<?, ?> entry : owned) {
            sb.append("      ").append(entry.getSourceType().getSimpleName())
                    .append(" -> ").append(entry.getTargetType().getSimpleName())
                    .append("  <- ").append(entry.getConverterType())
                    .append("  (weight=").append(entry.getPriority()).append(")\n");
        }
        List<ConverterScope> children = new ArrayList<>(scope.getChildren());
        children.sort(Comparator.comparing(ConverterScope::getName));
        for (ConverterScope child : children) {
            appendScope(sb, child, all);
        }
    }

    /**
     * 某个类型对的命中链路：候选按什么顺序、为什么是这个顺序
     */
    public String explain(Class<?> from, Class<?> to) {
        Class<?> normalizedFrom = PrimitiveUtil.getPrimitiveWrapper(from);
        Class<?> normalizedTo = PrimitiveUtil.getPrimitiveWrapper(to);
        List<ConverterEntry<?, ?>> chain = chain(normalizedFrom, normalizedTo);
        StringBuilder sb = new StringBuilder();
        sb.append("解析链路 ").append(normalizedFrom.getSimpleName())
                .append(" -> ").append(normalizedTo.getSimpleName())
                .append("，命中 ").append(chain.size()).append(" 个候选（类型距离升序 → scope 优先级降序 → 权重降序 → 注册序升序）\n");
        for (int i = 0; i < chain.size(); i++) {
            ConverterEntry<?, ?> entry = chain.get(i);
            sb.append("  #").append(i + 1)
                    .append(' ').append(entry.getDescription())
                    .append("  distance=").append(entry.distanceTo(normalizedFrom, normalizedTo))
                    .append(", scope=").append(entry.getScope().getName())
                    .append("(priority=").append(entry.getScope().getPriority())
                    .append(", enabled=").append(entry.getScope().isEnabled()).append(')')
                    .append(", weight=").append(entry.getPriority())
                    .append(", seq=").append(entry.getSequence())
                    .append('\n');
        }
        return sb.toString();
    }
}