package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class ConvertCaller<T> {

    private Method method;
    private Object caller;

    private Class<?> from;
    private Class<T> target;

    public T to(Object o) {
        Object defaultValue = null;
        try {
            if(PrimitiveUtil.isGeneralType(target)){
                defaultValue = target.newInstance();
            } else {
                defaultValue = PrimitiveUtil.defaultValue(target);
            }
            return (T) method.invoke(caller, o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Object to(Object o, Object defaultValue) {
        if (from == target) {
            return o;
        }
        try {
            return method.invoke(caller, o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
