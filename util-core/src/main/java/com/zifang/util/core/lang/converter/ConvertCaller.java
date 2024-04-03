package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行转换的句柄包装
 */
@Data
public class ConvertCaller<F, T> implements IConverter<F,T>{

    private Method method;
    private Object caller;

    private Class<?> from;
    private Class<?> target;

    public T to(F o) {
        Object defaultValue = null;
        try {
            if(PrimitiveUtil.isGeneralType(target)){
                defaultValue = target.newInstance();
            } else {
                defaultValue = PrimitiveUtil.defaultValue(target);
            }
            method.setAccessible(true);
            return (T) method.invoke(caller, o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public T to(Object o, Object defaultValue) {
        if (from == target) {
            return (T) o;
        }
        try {
            return (T) method.invoke(caller, o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ConvertCaller<F, T> copy() {
        ConvertCaller<F, T> convertCaller = new ConvertCaller<>();
        convertCaller.setMethod(method);
        convertCaller.setCaller(caller);
        convertCaller.setFrom(from);
        convertCaller.setTarget(target);
        return convertCaller;
    }
}
