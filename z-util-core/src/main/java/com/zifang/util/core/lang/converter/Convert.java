package com.zifang.util.core.lang.converter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注一个类型转换器，交由 {@link ConverterScanner} 自动注册。
 *
 * <pre>
 *   &#64;Convert(scope = "sql", from = String.class, to = LocalDate.class)
 *   public class SqlDateConverter implements Converter&lt;String, LocalDate&gt; { ... }
 *
 *   &#64;Convert(from = String.class, to = Money.class)
 *   public class MoneyHolder {
 *       &#64;Convert(from = String.class, to = Money.class)
 *       public static Money parse(String value, ConvertContext context) { ... }
 *   }
 * </pre>
 *
 * 打在类上时要求有无参构造器；打在静态方法上时方法签名须为
 * {@code (F value, ConvertContext context)}，与 {@link Converter} 一致。
 *
 * @author zifang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Convert {

    /**
     * 归属 scope 名。未声明时归入 core。
     */
    String scope() default "core";

    Class<?> from();

    Class<?> to();

    /**
     * 转换器权重。类型距离相同时权重高者先试。
     */
    int priority() default 0;

    /**
     * 是否启用，便于灰度与临时下线
     */
    boolean enabled() default true;
}