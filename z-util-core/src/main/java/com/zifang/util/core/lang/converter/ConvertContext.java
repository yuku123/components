package com.zifang.util.core.lang.converter;

/**
 * 单次转换的上下文。随转换向下传递策略与元素路径，
 * 使集合/数组深度转换中的失败能被定位到具体元素。
 *
 * @author zifang
 */
public class ConvertContext {

    private final ConvertPolicy policy;
    private final Class<?> sourceType;
    private final Class<?> targetType;
    private final String path;

    public ConvertContext(ConvertPolicy policy, Class<?> sourceType, Class<?> targetType) {
        this(policy, sourceType, targetType, null);
    }

    private ConvertContext(ConvertPolicy policy, Class<?> sourceType, Class<?> targetType, String path) {
        this.policy = policy == null ? ConvertPolicy.defaults() : policy;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.path = path;
    }

    public ConvertPolicy getPolicy() {
        return policy;
    }

    public Class<?> getSourceType() {
        return sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    /**
     * @return 元素路径（如 list[2].date）；根级转换为 null
     */
    public String getPath() {
        return path;
    }

    /**
     * 派生一个带路径段的子上下文，元素类型未定（null）
     */
    public ConvertContext child(Class<?> sourceType, Class<?> targetType, String segment) {
        return new ConvertContext(policy, sourceType, targetType, appendPath(segment));
    }

    private String appendPath(String segment) {
        if (segment == null || segment.isEmpty()) {
            return path;
        }
        return path == null ? segment : path + "." + segment;
    }

    @Override
    public String toString() {
        return "ConvertContext{" + (sourceType == null ? "null" : sourceType.getSimpleName())
                + " -> " + (targetType == null ? "null" : targetType.getSimpleName())
                + (path == null ? "" : " @ " + path) + '}';
    }
}