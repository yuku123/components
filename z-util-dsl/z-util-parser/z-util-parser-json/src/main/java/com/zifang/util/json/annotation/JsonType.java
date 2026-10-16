package com.zifang.util.json.annotation;

import java.lang.annotation.*;

/**
 * 标记在 POJO 类上，声明该类型整体层面的 JSON 绑定行为。
 *
 * <p>与字段级 {@link JsonSerialize}/{@link JsonDeserialize} 的分工：
 * 字段级管「单个字段叫什么、用什么转换器」，本注解管「这一类对象统一怎么绑」。
 * 对应 Jackson 里散落在 {@code @JsonInclude}/{@code @PropertyNamingStrategy}/
 * {@code @JsonIgnoreProperties} 上的类级语义，收敛到一个注解。</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonType {

    /**
     * 类级命名策略：字段名 → JSON 属性名的统一变换。
     * <p>字段上显式 {@code name()} 优先于本策略；未被覆盖的字段按策略变换。
     * 序列化与反序列化同用一套，保证两边报文一致。</p>
     */
    Naming naming() default Naming.IDENTITY;

    /**
     * 序列化时跳过值为 {@code null} 的字段（对应 Jackson 的 NON_NULL）。
     * <p>只作用于 bean 字段，不影响集合/数组里的 null 元素；
     * {@code JsonUtil.toJsonExcludingNulls} 的全局开关对本类字段同样生效。</p>
     */
    boolean nonNull() default false;

    /**
     * 类级忽略名单：列出的字段名（Java 字段名）双向不参与绑定——
     * 序列化不输出、反序列化不回填。字段级 {@code ignore()} 是单方向的，
     * 本名单是「这个属性彻底不属于报文」的声明。
     */
    String[] exclude() default {};

    /**
     * 字段名 → JSON 属性名的命名策略。
     */
    enum Naming {
        /** 原样使用字段名。 */
        IDENTITY,
        /** camelCase → snake_case（promptTokens → prompt_tokens）。 */
        SNAKE_CASE,
        /** camelCase → kebab-case（modelName → model-name）。 */
        KEBAB_CASE
    }
}
