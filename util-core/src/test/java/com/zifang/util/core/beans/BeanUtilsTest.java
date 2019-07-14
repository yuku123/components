package com.zifang.util.core.beans;

import com.zifang.util.core.demo.thirdpart.jar.json.GsonUtil;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

public class BeanUtilsTest {

    @Test
    public void tes1() throws Exception {
        Map<String,Object> map= new HashedMap();
        map.put("baseByteType",(byte)1);
        map.put("baseCharType",'c');
        map.put("baseIntType",2);
        map.put("baseLongType",3L);
        map.put("baseFloatType",1.2f);
        map.put("baseDoubleType",1.4d);
        map.put("byteWapperType",new Byte("9"));
        map.put("charWapperType",new Character('c'));
        map.put("intWapperType",new Integer("11"));
        map.put("longWapperType",new Long("12"));
        map.put("floatWapperType",new Float("9.1"));
        map.put("doubleWapperType",new Double(22.22d));
        map.put("stringType","s");
        Person person = BeanUtils.mapToBean(Person.class,map);
        System.out.println(GsonUtil.objectToJsonStr(person));
    }

    @Test
    public void test2() throws Exception {
        PropertyDescriptor pro = new PropertyDescriptor("stringType", Person.class);
        Person preson=new Person();
        Method method=pro.getWriteMethod();
        method.invoke(preson, "xiong");
        System.out.println(pro.getReadMethod().invoke(preson));
    }

    public static Object evaluatePo(String className, Object po, Properties prop) {
        try {

            Class poClass = Class.forName(className);

            if (po == null) {
                po = poClass.newInstance();
            }
            // Introspector相当beans这个架构的一个入口。类似于Hibernate的SessionFactory// 通过bean的类型获得bean的描述—》获得属性描述的集合
            BeanInfo bi = Introspector.getBeanInfo(poClass);
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();

            for (int i = 0; i < pd.length; i++) {
                if (prop.getProperty(pd[i].getName()) != null) {
                    Object value = getPropValue(pd[i].getPropertyType(),
                            prop.getProperty(pd[i].getName()));

                    executeEvaluate(po, pd[i].getWriteMethod(), value);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return po;
    }

    // 这里是PropertyEditor 的使用，规避了if条件的使用。让代码的扩展性得到了很好的保证。这里使用的是依赖查找（DL）的方式。// 注册PropertyEditor 的步骤放在了bean容器当中    static Object getPropValue(Class clazz, String value)
    static Object getPropValue(Class clazz, String value) throws InstantiationException, IllegalAccessException {
        PropertyEditor pe = PropertyEditorManager.findEditor(clazz);
        pe.setAsText(value);
        return pe.getValue();
    }

    //调用赋值方法
    static void executeEvaluate(Object dest, Method m, Object value)
            throws Exception {
        Expression e = new Expression(dest, m.getName(),
                new Object[] { value });
        e.execute();
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        getPropValue(Person.class,"stringType");
    }

//    public static void main(String[] args) throws ClassNotFoundException, IntrospectionException, InstantiationException, IllegalAccessException {
//        Class<?> clazz = Class.forName("com.zifang.util.core.beans.Person");
//        // 在bean上进行内省
//        BeanInfo beaninfo = Introspector.getBeanInfo(clazz, Object.class);
//        PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
//        for(PropertyDescriptor propertyDescriptor:pro){
//            String name = propertyDescriptor.getName();
//            String getter = propertyDescriptor.getReadMethod().getName();
//            String setter = propertyDescriptor.getWriteMethod().getName();
//            System.out.println(name+":"+getter+":"+setter);
//        }
//    }

}
