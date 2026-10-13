package com.zifang.util.core.lang.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类型转换失败异常。
 * <p>
 * 严格轨（{@link Converters#to}）在候选链全部失败时抛出本异常，并携带每一次尝试的失败原因，
 * 避免"打栈之后返回 null"式的静默丢数据。
 *
 * @author zifang
 */
public class ConvertException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 候选链上的一次尝试失败记录
     */
    public static class Attempt {

        private final String converterName;
        private final String scopeName;
        private final Throwable cause;

        public Attempt(String converterName, String scopeName, Throwable cause) {
            this.converterName = converterName;
            this.scopeName = scopeName;
            this.cause = cause;
        }

        public String getConverterName() {
            return converterName;
        }

        public String getScopeName() {
            return scopeName;
        }

        public Throwable getCause() {
            return cause;
        }

        @Override
        public String toString() {
            return scopeName + "/" + converterName + " -> " + (cause == null ? "null" : cause.toString());
        }
    }

    private final transient Object value;
    private final Class<?> sourceType;
    private final Class<?> targetType;
    private final String path;
    private final transient List<Attempt> attempts;

    public ConvertException(Object value, Class<?> sourceType, Class<?> targetType, String path,
                            List<Attempt> attempts, Throwable cause) {
        super(buildMessage(value, sourceType, targetType, path, attempts), cause);
        this.value = value;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.path = path;
        this.attempts = attempts == null
                ? Collections.<Attempt>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(attempts));
    }

    public ConvertException(Object value, Class<?> sourceType, Class<?> targetType, String message) {
        super(message);
        this.value = value;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.path = null;
        this.attempts = Collections.emptyList();
    }

    public ConvertException(Object value, Class<?> sourceType, Class<?> targetType,
                            List<Attempt> attempts, Throwable cause) {
        this(value, sourceType, targetType, null, attempts, cause);
    }

    public ConvertException(Object value, Class<?> sourceType, Class<?> targetType, Throwable cause) {
        this(value, sourceType, targetType, null, null, cause);
    }

    private static String buildMessage(Object value, Class<?> sourceType, Class<?> targetType,
                                       String path, List<Attempt> attempts) {
        StringBuilder sb = new StringBuilder();
        sb.append("类型转换失败: ")
                .append(sourceType == null ? "null" : sourceType.getName())
                .append(" -> ")
                .append(targetType == null ? "null" : targetType.getName());
        if (path != null && !path.isEmpty()) {
            sb.append(" @ ").append(path);
        }
        sb.append("，原值=").append(value);
        if (attempts != null && !attempts.isEmpty()) {
            sb.append("，已尝试 ").append(attempts.size()).append(" 个转换器:");
            for (Attempt attempt : attempts) {
                sb.append("\n    - ").append(attempt);
            }
        } else {
            sb.append("，无候选转换器");
        }
        return sb.toString();
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getSourceType() {
        return sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    /**
     * 深度转换时的元素路径（如 list[2].date），根级转换为 null
     */
    public String getPath() {
        return path;
    }

    public List<Attempt> getAttempts() {
        return attempts;
    }
}