package com.zifang.util.core.lang.converter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注枚举上承载业务值的字段。转换时优先按该字段的字面量匹配，而不是按 {@link Enum#name()}。
 *
 * <pre>
 *   public enum Status &#64;Convert.EnumValue... &#64;EnumValue private final String code;
 * </pre>
 *
 * 声明了该字段的枚举，字面量匹配顺序变为：{@link ConvertPolicy#getEnumAliases()} 别名 →
 * {@code @EnumValue} 字段值 → 常量名。声明了字段却没有命中任何常量的，直接判失败，
 * 不再回退到常量名，避免"看起来匹配成功其实取错了值"。
 *
 * @author zifang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumValue {
}