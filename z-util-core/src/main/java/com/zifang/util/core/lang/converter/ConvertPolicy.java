package com.zifang.util.core.lang.converter;

import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 转换策略。集中承载 trim / 空串语义 / 窄化溢出 / 精度舍入 / 枚举匹配 / 时间格式 / 布尔词表，
 * 避免这些语义散落成一堆重载参数或隐式 if-else。
 * <p>
 * 不可变；改动请走 {@link #builder()} 或 {@link #toBuilder()}。
 *
 * @author zifang
 */
public final class ConvertPolicy {

    /**
     * 空串 / 纯空白输入的处置
     */
    public enum BlankPolicy {
        /**
         * 视为 null，返回默认值
         */
        AS_NULL,
        /**
         * 视为 null 并抛异常
         */
        FAIL,
        /**
         * 照原样参与转换
         */
        KEEP
    }

    /**
     * 数值窄化溢出（如 long→int、BigDecimal 超精度）的处置
     */
    public enum NarrowingPolicy {
        /**
         * 抛 {@link ConvertException}
         */
        FAIL,
        /**
         * 饱和到目标类型的上下界
         */
        SATURATE,
        /**
         * 按目标位宽回绕
         */
        WRAP
    }

    /**
     * 枚举字面量的匹配方式
     */
    public enum EnumMatchPolicy {
        EXACT,
        CASE_INSENSITIVE
    }

    private static final Set<String> DEFAULT_TRUE_VALUES =
            unmodifiableSet("true", "1", "yes", "y", "on", "t");
    private static final Set<String> DEFAULT_FALSE_VALUES =
            unmodifiableSet("false", "0", "no", "n", "off", "f");

    private final BlankPolicy blankPolicy;
    private final boolean trim;
    private final NarrowingPolicy narrowingPolicy;
    private final int scale;
    private final RoundingMode roundingMode;
    private final EnumMatchPolicy enumMatchPolicy;
    private final Map<String, String> enumAliases;
    private final Set<String> trueValues;
    private final Set<String> falseValues;
    private final Map<Class<?>, List<DateTimeFormatter>> temporalFormats;

    private ConvertPolicy(Builder builder) {
        this.blankPolicy = builder.blankPolicy;
        this.trim = builder.trim;
        this.narrowingPolicy = builder.narrowingPolicy;
        this.scale = builder.scale;
        this.roundingMode = builder.roundingMode;
        this.enumMatchPolicy = builder.enumMatchPolicy;
        this.enumAliases = Collections.unmodifiableMap(new HashMap<>(builder.enumAliases));
        this.trueValues = unmodifiableSet(builder.trueValues.toArray(new String[0]));
        this.falseValues = unmodifiableSet(builder.falseValues.toArray(new String[0]));
        Map<Class<?>, List<DateTimeFormatter>> formats = new LinkedHashMap<>();
        for (Map.Entry<Class<?>, List<DateTimeFormatter>> entry : builder.temporalFormats.entrySet()) {
            formats.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        this.temporalFormats = Collections.unmodifiableMap(formats);
    }

    /**
     * 默认策略：trim 开启、空串按 null、窄化溢出抛错、BigDecimal 不限 scale
     */
    public static ConvertPolicy defaults() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.blankPolicy = blankPolicy;
        builder.trim = trim;
        builder.narrowingPolicy = narrowingPolicy;
        builder.scale = scale;
        builder.roundingMode = roundingMode;
        builder.enumMatchPolicy = enumMatchPolicy;
        builder.enumAliases.putAll(enumAliases);
        builder.trueValues.addAll(trueValues);
        builder.falseValues.addAll(falseValues);
        for (Map.Entry<Class<?>, List<DateTimeFormatter>> entry : temporalFormats.entrySet()) {
            builder.temporalFormats.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return builder;
    }

    public BlankPolicy getBlankPolicy() {
        return blankPolicy;
    }

    public boolean isTrim() {
        return trim;
    }

    public NarrowingPolicy getNarrowingPolicy() {
        return narrowingPolicy;
    }

    /**
     * @return 强制小数位；-1 表示不强制，保留源值 scale
     */
    public int getScale() {
        return scale;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    public EnumMatchPolicy getEnumMatchPolicy() {
        return enumMatchPolicy;
    }

    /**
     * @return 枚举字面量别名表，键为别名值为枚举常量名
     */
    public Map<String, String> getEnumAliases() {
        return enumAliases;
    }

    public Set<String> getTrueValues() {
        return trueValues;
    }

    public Set<String> getFalseValues() {
        return falseValues;
    }

    /**
     * @return 时间类型到格式器的候选列表，按声明顺序尝试；未声明的类型走内置默认格式
     */
    public Map<Class<?>, List<DateTimeFormatter>> getTemporalFormats() {
        return temporalFormats;
    }

    private static Set<String> unmodifiableSet(String... values) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }

    @Override
    public String toString() {
        return "ConvertPolicy{blank=" + blankPolicy
                + ", trim=" + trim
                + ", narrowing=" + narrowingPolicy
                + ", scale=" + scale
                + ", rounding=" + roundingMode
                + ", enumMatch=" + enumMatchPolicy
                + ", enumAliases=" + enumAliases.size()
                + ", trueValues=" + trueValues
                + ", falseValues=" + falseValues
                + ", temporalTypes=" + temporalFormats.keySet()
                + '}';
    }

    public static class Builder {

        private BlankPolicy blankPolicy = BlankPolicy.AS_NULL;
        private boolean trim = true;
        private NarrowingPolicy narrowingPolicy = NarrowingPolicy.FAIL;
        private int scale = -1;
        private RoundingMode roundingMode = RoundingMode.HALF_UP;
        private EnumMatchPolicy enumMatchPolicy = EnumMatchPolicy.EXACT;
        private final Map<String, String> enumAliases = new HashMap<>();
        private final Set<String> trueValues = new HashSet<>(DEFAULT_TRUE_VALUES);
        private final Set<String> falseValues = new HashSet<>(DEFAULT_FALSE_VALUES);
        private final Map<Class<?>, List<DateTimeFormatter>> temporalFormats = new LinkedHashMap<>();

        public Builder blankPolicy(BlankPolicy blankPolicy) {
            this.blankPolicy = blankPolicy;
            return this;
        }

        public Builder trim(boolean trim) {
            this.trim = trim;
            return this;
        }

        public Builder narrowingPolicy(NarrowingPolicy narrowingPolicy) {
            this.narrowingPolicy = narrowingPolicy;
            return this;
        }

        public Builder scale(int scale) {
            this.scale = scale;
            return this;
        }

        public Builder roundingMode(RoundingMode roundingMode) {
            this.roundingMode = roundingMode;
            return this;
        }

        public Builder enumMatchPolicy(EnumMatchPolicy enumMatchPolicy) {
            this.enumMatchPolicy = enumMatchPolicy;
            return this;
        }

        public Builder enumAlias(String alias, String constantName) {
            this.enumAliases.put(alias, constantName);
            return this;
        }

        public Builder enumAliases(Map<String, String> aliases) {
            this.enumAliases.putAll(aliases);
            return this;
        }

        public Builder trueValues(String... values) {
            this.trueValues.clear();
            this.trueValues.addAll(Arrays.asList(values));
            return this;
        }

        public Builder falseValues(String... values) {
            this.falseValues.clear();
            this.falseValues.addAll(Arrays.asList(values));
            return this;
        }

        public Builder addTrueValues(Collection<String> values) {
            this.trueValues.addAll(values);
            return this;
        }

        public Builder addFalseValues(Collection<String> values) {
            this.falseValues.addAll(values);
            return this;
        }

        /**
         * 追加某时间类型的候选格式，按声明顺序依次尝试
         */
        public Builder format(Class<?> temporalType, String pattern, DateTimeFormatter... more) {
            List<DateTimeFormatter> list = this.temporalFormats.get(temporalType);
            if (list == null) {
                list = new ArrayList<>();
                this.temporalFormats.put(temporalType, list);
            }
            list.add(DateTimeFormatter.ofPattern(pattern));
            if (more != null) {
                for (DateTimeFormatter formatter : more) {
                    list.add(formatter);
                }
            }
            return this;
        }

        public Builder formats(Class<?> temporalType, Collection<DateTimeFormatter> formatters) {
            List<DateTimeFormatter> list = this.temporalFormats.get(temporalType);
            if (list == null) {
                list = new ArrayList<>();
                this.temporalFormats.put(temporalType, list);
            }
            list.addAll(formatters);
            return this;
        }

        public ConvertPolicy build() {
            return new ConvertPolicy(this);
        }
    }
}