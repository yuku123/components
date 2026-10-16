package com.zifang.util.json.annotation;

import java.lang.annotation.*;

/**
 * 标记在 POJO 字段上，控制反序列化行为。
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonDeserialize {

    /**
     * 指定自定义反序列化器类。
     * 默认值为 {@link #NONE}，表示使用默认反序列化逻辑。
     */
    Class<?> using() default NONE.class;

    /**
     * 反序列化时的 JSON 属性名（用于从不同字段名读取）。
     */
    String name() default "";

    /**
     * 反序列化时额外接受的备选属性名（对应 Jackson 的 {@code @JsonAlias}）。
     * <p>查找顺序：先 {@code name()}（缺省为字段名），再按声明顺序逐个试别名，
     * 取第一个非 null 的值。只作用于读取方向，序列化永远输出主名。</p>
     */
    String[] alias() default {};

    /**
     * 反序列化时跳过此字段（不回填）。
     * <p>与 {@link JsonSerialize#ignore()} 分方向：只写不读的「出口字段」用
     * serialize 侧 ignore，只读不写的「入口字段」用本项。</p>
     */
    boolean ignore() default false;

    /**
     * 反序列化时使用的格式字符串（如日期格式）。
     * 默认空字符串，表示使用反序列化器内置默认格式。
     */
    String format() default "";

    /**
     * 空标记类，用于表示"不指定自定义反序列化器"。
     */
    final class NONE {}
}
