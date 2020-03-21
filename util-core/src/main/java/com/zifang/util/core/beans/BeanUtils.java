package com.zifang.util.core.beans;

import com.zifang.util.core.reflect.ClassUtil;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供Bean的相关操作
 * */
public class BeanUtils {

    /**
     * 检测传入的Object是否为标准的Bean
     * */
    public static <T> boolean isBean(T bean){
        if (ClassUtil.isNormalClass(bean.getClass())) {
            final Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    // 检测包含标准的setXXX方法即视为标准的JavaBean
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * t通过序列化的方式进行深复制
     * */
    public static <T> T cloneBean(final T bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {
        T t = (T) bean.getClass().newInstance();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(bean.getClass(),Object.class).getPropertyDescriptors();
        for(PropertyDescriptor propertyDescriptor : pro){
            Method writeMethod = propertyDescriptor.getWriteMethod();
            Method readMethod = propertyDescriptor.getReadMethod();
            writeMethod.invoke(t,readMethod.invoke(bean));
        }
        return t;
    }

    /**
     * 输入map，输出组装好了的bean
     * */
    public static <T> T  mapToBean(Class<T> clazz, Map<String,? extends Object> map) throws IllegalAccessException, InstantiationException, IntrospectionException {
        T t = clazz.newInstance();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(clazz,Object.class).getPropertyDescriptors();
        for(PropertyDescriptor propertyDescriptor : pro){
            String name = propertyDescriptor.getName();
            Method method = propertyDescriptor.getWriteMethod();
            if(map.keySet().contains(name)){
                try {
                    method.invoke(t,map.get(name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    /**
     * 输入bean 输出这个bean第一层value值
     * */
    public static <T> Map<String,Object>  beanToMap(final T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<String,Object> map = new HashMap<>();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(t.getClass(),Object.class).getPropertyDescriptors();
        for(PropertyDescriptor propertyDescriptor : pro){
            String key = propertyDescriptor.getName();
            Method readMethod = propertyDescriptor.getReadMethod();
            Object value = readMethod.invoke(t);
            map.put(key,value);
        }
        return map;
    }

    /**
     * 对一个贫血对象设入参数
     *
     * @param obj 等待设入
     * @param name 设入的字段名
     * @param value 设入的字段值
     *
     */
    public static void setProperty(Object obj,String name,Object value){
        try {
            PropertyDescriptor[] pro = Introspector.getBeanInfo(obj.getClass(),Object.class).getPropertyDescriptors();
            PropertyDescriptor propertyDescriptor = findPropertyDescriptorByName(pro,name);
            if(propertyDescriptor!=null){
                propertyDescriptor.getWriteMethod().invoke(obj,value);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 得到一个Object的字段值
     *
     * @param obj 等待被摄取的实例
     * @param name 摄取的字段名
     * @return 字段值
     */
    public static Object getProperty(Object obj,String name){
        try {
            PropertyDescriptor[] pro = Introspector.getBeanInfo(obj.getClass(),Object.class).getPropertyDescriptors();
            PropertyDescriptor propertyDescriptor = findPropertyDescriptorByName(pro,name);
            if(propertyDescriptor!=null){
                return propertyDescriptor.getReadMethod().invoke(obj);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 找到所需要的handle
     * */
    private static PropertyDescriptor findPropertyDescriptorByName(PropertyDescriptor[] pro,String name){
        for (PropertyDescriptor propertyDescriptor : pro) {
            if(name.equals(propertyDescriptor.getName())){
                return propertyDescriptor;
            }
        }
        return null;
    }
}
