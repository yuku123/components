package com.zifang.util.core.beans;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanUtils {

    public static Object cloneBean(final Object bean) throws IllegalAccessException, InstantiationException,InvocationTargetException, NoSuchMethodException {
        return BeanUtilsBean.getInstance().cloneBean(bean);
    }

    public static Object deepCloneBean(final Object bean)throws IllegalAccessException, InstantiationException,InvocationTargetException, NoSuchMethodException {
        return BeanUtilsBean.getInstance().cloneBean(bean);
    }

    public static <T> T  beanMapper(Class<T> clazz, Map<String,Object> map) throws IllegalAccessException, InstantiationException, IntrospectionException {
        T t = clazz.newInstance();
        PropertyDescriptor[] pro = Introspector.getBeanInfo(clazz,Object.class).getPropertyDescriptors();
        for(PropertyDescriptor propertyDescriptor : pro){
            String name = propertyDescriptor.getName();
            Method method = propertyDescriptor.getWriteMethod();
            if(map.keySet().contains(name)){
                try {
                    method.invoke(t,map.get(name));
                } catch (Exception e) {
                    System.out.println(name);
                    e.printStackTrace();
                }
            }
        }
        return t;
    }
}
