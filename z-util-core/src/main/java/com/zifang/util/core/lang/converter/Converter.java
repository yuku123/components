package com.zifang.util.core.lang.converter;

/**
 * 类型转换器扩展点。
 * <p>
 * 实现类或静态方法可标注 {@link Convert} 由 {@link ConverterScanner} 自动发现，
 * 也可通过 {@link Converters#register} 直接注册。
 * <p>
 * 约定：入参非 null 而返回 null 视为本次转换失败，注册表会记录原因并尝试候选链上的下一个。
 * 转换失败请抛 {@link ConvertException}，不要吞掉。
 *
 * @param <F> 源类型
 * @param <T> 目标类型
 * @author zifang
 */
public interface Converter<F, T> {

    /**
     * 执行转换
     *
     * @param value   源值，非 null（null 由门面层短路）
     * @param context 转换上下文，携带策略与元素路径
     * @return 目标值
     * @throws ConvertException 转换失败
     */
    T convert(F value, ConvertContext context);
}