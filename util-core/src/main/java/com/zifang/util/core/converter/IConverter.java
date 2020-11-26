package com.zifang.util.core.converter;

/**
 * <p>the interface for convert object to be specific type</p>
 *
 *
 */
public interface IConverter<T> {
    /**
     * Convert the specified input object into an output object of the
     * specified type.
     *
     * @param <T> the desired result type
     * @param value The input value to be converted
     * @return The converted value
     *
     */
    <T> T convert(Object value);


}
