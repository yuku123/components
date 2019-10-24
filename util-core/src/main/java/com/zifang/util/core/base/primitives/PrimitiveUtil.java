package com.zifang.util.core.base.primitives;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理基本类型与包装类型的工具类
 * */
public class PrimitiveUtil {

    private final static Logger log = Logger.getLogger(PrimitiveUtil.class);

    public static List<Class> primitiveTypeList = new ArrayList(){
        {
            add(byte.class);
            add(char.class);
            add(short.class);
            add(int.class);
            add(long.class);
            add(float.class);
            add(double.class);
            add(boolean.class);
        }
    };

    public static List<Class> primitiveWrapperTypeList = new ArrayList(){
        {
            add(Byte.class);
            add(Character.class);
            add(Short.class);
            add(Integer.class);
            add(Long.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
        }
    };


    /**
     * 判断是否是基本类型
     * */
    public static <T> boolean isPrimitive(Class<T> clazz){
        return primitiveTypeList.contains(clazz);
    }

    /**
     * 判断是否是基本类型的包装类
     * */
    public static <T> boolean isPrimitiveWrapper(Class<T> clazz){
        return primitiveWrapperTypeList.contains(clazz);
    }

    /**
     * 得到包装类对应的基本类型
     * */
    public static Class getPrimitive(Class clazz){
        if(!isPrimitiveWrapper(clazz)){
            String error = "the input class"+clazz.getName()+" is not wapperType";
            log.error(error);
            throw new RuntimeException(error);
        }
        return primitiveTypeList.get(primitiveWrapperTypeList.indexOf(clazz));
    }

    /**
     * 得到基本类型对应的包装类型
     * */
    public static Class getPrimitiveWrapper(Class clazz){
        if(!isPrimitive(clazz)){
            String error = "the input class"+clazz.getName()+" is not Primitive Type";
            log.error(error);
            throw new RuntimeException(error);
        }
        return primitiveWrapperTypeList.get(primitiveTypeList.indexOf(clazz));
    }
}
