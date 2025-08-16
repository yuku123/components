package com.zifang.util.core.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zifang
 * @time: 2019-05-08 17:11:00
 * @description: class type util
 * @version: JDK 1.8
 */
public class ClassUtil {

    /**
     * base type wrapper list
     */
    private static final List<String> BASE_WRAP_TYPE_LIST = new ArrayList<>();
    /**
     * base type list
     */
    private static final List<String> BASE_TYPE_LIST = new ArrayList<>();

    static {
        BASE_TYPE_LIST.add("int");
        BASE_TYPE_LIST.add("double");
        BASE_TYPE_LIST.add("long");
        BASE_TYPE_LIST.add("short");
        BASE_TYPE_LIST.add("byte");
        BASE_TYPE_LIST.add("boolean");
        BASE_TYPE_LIST.add("char");
        BASE_TYPE_LIST.add("float");

        BASE_WRAP_TYPE_LIST.add("java.lang.Integer");
        BASE_WRAP_TYPE_LIST.add("java.lang.Double");
        BASE_WRAP_TYPE_LIST.add("java.lang.Float");
        BASE_WRAP_TYPE_LIST.add("java.lang.Long");
        BASE_WRAP_TYPE_LIST.add("java.lang.Short");
        BASE_WRAP_TYPE_LIST.add("java.lang.Byte");
        BASE_WRAP_TYPE_LIST.add("java.lang.Boolean");
        BASE_WRAP_TYPE_LIST.add("java.lang.Character");
    }

    /**
     * @author: zifang
     * @description: judge is base
     * @time: 2019-05-08 17:48:00
     * @params: [className] Class name
     * @return: boolean response
     **/
    public static boolean isPrimitive(String className) {
        return BASE_TYPE_LIST.contains(className);
    }

    /**
     * @author: zifang
     * @description: judge class name is base wrapper
     * @time: 2019-06-14 10:01:00
     * @params: [className] Class name
     * @return: boolean response
     */
    public static boolean isBaseWrap(String className) {
        return BASE_WRAP_TYPE_LIST.contains(className);
    }

    /**
     * @author: zifang
     * @description: judge class is base wrapper
     * @time: 2019-06-14 10:01:00
     * @params: [clazz] class
     * @return: boolean response
     */
    public static boolean isBaseWrap(Class<?> clazz) {
        return isBaseWrap(clazz.getCanonicalName());
    }

    /**
     * @author: zifang
     * @description: judge class name is base or base wrapper
     * @time: 2019-06-14 10:07:00
     * @params: [className] class name
     * @return: boolean response
     */
    public static boolean isBaseOrWrap(String className) {
        return isPrimitive(className) || isBaseWrap(className);
    }

    /**
     * @author: zifang
     * @description: judge class is base or base wrapper
     * @time: 2019-06-14 10:08:00
     * @params: [clazz] class
     * @return: boolean response
     */
    public static boolean isBaseOrWrap(Class<?> clazz) {
        return isBaseOrWrap(clazz.getCanonicalName());
    }

    /**
     * @author: zifang
     * @description: judge class is base or base wrapper or string
     * @time: 2021-07-02 15:21:00
     * @params: [clazz] request
     * @return: boolean response
     */
    public static boolean isBaseOrWrapOrString(Class<?> clazz) {
        return isBaseOrWrap(clazz.getCanonicalName()) || isSameClass(clazz, String.class);
    }

    /**
     * @author: zifang
     * @description: judge object is base or base wrapper
     * @time: 2019-06-14 10:09:00
     * @params: [object] request
     * @return: boolean response
     */
    public static boolean isBaseOrWrap(Object object) {
        return null != object && isBaseOrWrap(object.getClass());
    }

    /**
     * @author: zifang
     * @description: judge two class is same by classloader and canonicalName
     * @time: 2020-08-27 14:50:00
     * @params: [clazz, clz] class, class two
     * @return: boolean is same
     */
    public static boolean isSameClass(Class<?> clazz, Class<?> clz) {
        if (null == clazz && null == clz) {
            return true;
        }
        if (null == clazz || null == clz) {
            return false;
        }
        return clazz.isAssignableFrom(clz) && clz.isAssignableFrom(clazz)
                && clazz.getCanonicalName().equals(clz.getCanonicalName())
                && clazz.getClassLoader() == clz.getClassLoader();
    }

    /**
     * @author: zifang
     * @description: judge two class is same name(maybe has different classLoader)
     * @description: 判断两个类的类名是否一致（可能在两个类加载器中）
     * @time: 2022-12-02 15:06:00
     * @params: [clazz, clz] class, class two
     * @return: boolean is same name
     */
    public static boolean isSameNameClass(Class<?> clazz, Class<?> clz) {
        if (null == clazz && null == clz) {
            return true;
        }
        if (null == clazz || null == clz) {
            return false;
        }
        return clazz.getCanonicalName().equals(clz.getCanonicalName());
    }

    public static String getShortClassName(String className) {
        if (className == null) {
            return null;
        } else {
            String[] ss = className.split("\\.");
            StringBuilder sb = new StringBuilder(className.length());

            for (int i = 0; i < ss.length; ++i) {
                String s = ss[i];
                if (i != ss.length - 1) {
                    sb.append(s.charAt(0)).append('.');
                } else {
                    sb.append(s);
                }
            }

            return sb.toString();
        }
    }

    /**
     * @author: zifang
     * @description: 判断是否是jdk原生的类型, 如List.class、Map.class
     * @time: 2021-12-13 19:36:00
     * @params: [clazz] request
     * @return: boolean response
     */
    public static boolean isOriginJdkType(Class<?> clazz) {
        return null == clazz.getClassLoader();
    }

    public static boolean isPrimitiveNumberType(Class<?> clazz) {
        return long.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)
                || short.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz);
    }

    public static boolean isPrimitiveFloatingPointNumberType(Class<?> clazz) {
        return double.class.isAssignableFrom(clazz) || float.class.isAssignableFrom(clazz);
    }

    /**
     * <p>Converts an array of {@code Object} in to an array of {@code Class} objects.
     * If any of these objects is null, a null element will be inserted into the array.</p>
     *
     * <p>This method returns {@code null} for a {@code null} input array.</p>
     * <p>因为是数组所以隐式地带上了一个类型装箱如果有基本类型会转为对应的包装类，这可能会导致反射找方法时找不到使用时要慎重</p>
     *
     * @param array an {@code Object} array
     * @return a {@code Class} array, {@code null} if null array input
     */
    public static Class<?>[] toClass(final Object... array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return ArraysUtil.EMPTY_CLASS_ARRAY;
        }
        final Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }

    public static String argumentTypesToString(Class<?>[] argTypes) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                Class<?> c = argTypes[i];
                buf.append((c == null) ? "null" : c.getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }

}
