package com.zifang.util.core.lang.converter;

/**
 * 一条注册项：某个 scope 下 from→to 的一个转换器。
 *
 * @author zifang
 */
public class ConverterEntry<F, T> {

    private final ConverterScope scope;
    private final Class<F> sourceType;
    private final Class<T> targetType;
    private final Converter<F, T> converter;
    private final int priority;
    private final long sequence;
    private final String description;

    public ConverterEntry(ConverterScope scope, Class<F> sourceType, Class<T> targetType,
                          Converter<F, T> converter, int priority, long sequence) {
        this.scope = scope;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.converter = converter;
        this.priority = priority;
        this.sequence = sequence;
        this.description = sourceType.getSimpleName() + " -> " + targetType.getSimpleName();
    }

    public ConverterScope getScope() {
        return scope;
    }

    public Class<F> getSourceType() {
        return sourceType;
    }

    public Class<T> getTargetType() {
        return targetType;
    }

    public Converter<F, T> getConverter() {
        return converter;
    }

    /**
     * 转换器自身携带的权重，同类型距离下权重高者先试
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 注册序号，权重相同时先注册者先试
     */
    public long getSequence() {
        return sequence;
    }

    public String getDescription() {
        return description;
    }

    public String getConverterType() {
        return converter.getClass().getName();
    }

    /**
     * 该注册项与请求类型对的距离，0 最精确。全序且确定，用于排序候选链。
     */
    public int distanceTo(Class<?> from, Class<?> to) {
        int distance = 0;
        if (!sourceType.equals(from)) {
            distance += !sourceType.isAssignableFrom(from) ? 8 : 2;
        }
        if (!targetType.equals(to)) {
            distance += !targetType.isAssignableFrom(to) ? 4 : 1;
        }
        return distance;
    }

    @Override
    public String toString() {
        return scope.getName() + " | " + sourceType.getName() + " -> " + targetType.getName()
                + " | " + description + " | priority=" + priority + " seq=" + sequence;
    }
}