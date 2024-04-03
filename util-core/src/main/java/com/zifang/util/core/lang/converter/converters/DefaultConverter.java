package com.zifang.util.core.lang.converter.converters;

import com.zifang.util.core.lang.PrimitiveUtil;
import com.zifang.util.core.lang.converter.IConverter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zifang
 */
@Slf4j
public class DefaultConverter<F,T> implements IConverter<F,T> {

    @Override
    public T to(F value, T defaultValue) {

        if(value == null){
            return defaultValue;
        }

        if(defaultValue == null){
            throw new RuntimeException("transform defaultValue is null");
        }

        if(value.getClass() == defaultValue.getClass()){
            return (T)value;
        }

        if(value.getClass() == Boolean.class){
            if(defaultValue.getClass() == String.class){
                return (T)((Boolean)value).toString();
            } else {
                throw new RuntimeException("boolean cannot transformTo" + defaultValue.getClass().getName());
            }
        }

        if(value.getClass() == Character.class){

            if(defaultValue.getClass() == String.class){
                return (T)(value.toString());
            }

            if(defaultValue instanceof Number){
                return (T)to((int)(Character)value, (Number) defaultValue);
            }
        }

        if(value.getClass() == String.class){
            if(defaultValue.getClass() == Boolean.class){
                return (T)(Boolean.valueOf(value.toString()));
            }
            if(defaultValue.getClass() == Character.class){
                String str = value.toString();
                if(str.length() != 1){
                    throw new RuntimeException("inputString:"+value+",length>1, cannot transformToChar");
                } else {
                    return (T)(Character.valueOf(str.charAt(0)));
                }
            }

            if(defaultValue instanceof Number){
                try {
                    Method method = defaultValue.getClass().getMethod("valueOf",String.class);
                    return (T)method.invoke(null, value);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(value instanceof Number){

            if(defaultValue.getClass() == Boolean.class){
                throw new RuntimeException("can not transform value "+value +"to boolean");
            }

            if(defaultValue.getClass() == Character.class){
                return (T)Character.valueOf((char) ((Number) value).intValue());
            }

            if(defaultValue.getClass() == String.class){
                return (T)value.toString();
            }

            if(defaultValue instanceof Number){
                return (T)to((Number)value, (Number)defaultValue);
            }
        }
        return null;
    }

    public Number to(Number value, Number defaultValue) {
        if(value == null){
            return defaultValue;
        }
        String type = PrimitiveUtil.getPrimitive(defaultValue.getClass()).getName();
        String methodName = type + "Value";
        try {
            Method method = value.getClass().getMethod(methodName);
            return (Number) method.invoke(value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
