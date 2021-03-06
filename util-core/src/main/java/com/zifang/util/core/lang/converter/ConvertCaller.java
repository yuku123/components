package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class ConvertCaller {

    private Method method;
    private Object caller;

    private Class<?> from;
    private Class<?> target;

    public Object to(Object o){
        if(from == target){
            return o;
        }

        try {
            return method.invoke(caller,o, PrimitiveUtil.defaultValue(target));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object to(Object o, Object defaultValue){
        if(from == target){
            return o;
        }
        try {
            return method.invoke(caller,o, defaultValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
