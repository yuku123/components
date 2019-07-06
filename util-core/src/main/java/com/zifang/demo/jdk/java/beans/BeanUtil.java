package com.zifang.demo.jdk.java.beans;

import com.zifang.demo.thirdpart.jar.json.GsonUtil;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

public class BeanUtil {

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




    public static void main(String[] args) throws ClassNotFoundException, IntrospectionException, InstantiationException, IllegalAccessException {
//        Class<?> clazz = Class.forName("com.zifang.demo.jdk.java.beans.Person");
//        // 在bean上进行内省
//        BeanInfo beaninfo = Introspector.getBeanInfo(clazz, Object.class);
//        PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
//        for(PropertyDescriptor propertyDescriptor:pro){
//            String name = propertyDescriptor.getName();
//            String getter = propertyDescriptor.getReadMethod().getName();
//            String setter = propertyDescriptor.getWriteMethod().getName();
//            System.out.println(name+":"+getter+":"+setter);
//        }


    }
}
