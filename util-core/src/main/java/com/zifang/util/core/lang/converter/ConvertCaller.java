package com.zifang.util.core.lang.converter;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
public class ConvertCaller{

    private Method method;
    private Object caller;

    public Object to(Object o){
        try {
            return method.invoke(caller,o,null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
