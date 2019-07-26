package com.zifang.util.core.beans;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BasicBeanUtils {

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

    public static <T extends java.io.Serializable> T deepCloneBean(final T bean) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(bean);
        oos.flush();
        ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        T t=(T)ois.readObject();
        return t;
    }

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
    public static void setProperty(Object obj,String name,Object value){
        try {
            PropertyDescriptor[] pro = Introspector.getBeanInfo(obj.getClass(),Object.class).getPropertyDescriptors();
            PropertyDescriptor propertyDescriptor = findByName(pro,name);
            if(propertyDescriptor!=null){
                propertyDescriptor.getWriteMethod().invoke(obj,value);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static Object getProperty(Object obj,String name){
        try {
            PropertyDescriptor[] pro = Introspector.getBeanInfo(obj.getClass(),Object.class).getPropertyDescriptors();
            PropertyDescriptor propertyDescriptor = findByName(pro,name);
            if(propertyDescriptor!=null){
                return propertyDescriptor.getReadMethod().invoke(obj);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PropertyDescriptor findByName(PropertyDescriptor[] pro,String name){
        for (PropertyDescriptor propertyDescriptor : pro) {
            if(name.equals(propertyDescriptor.getName())){
                return propertyDescriptor;
            }
        }
        return null;
    }
}
