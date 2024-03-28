package com.zifang.util.core.lang.converter.converters;

import com.zifang.util.core.lang.PrimitiveUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zifang
 */
@Slf4j
public class DefaultConverter {


    /**
     * 1: boolean, char , String, 6种数值
     * 2: boolean, char , String, 6种数值
     * 数值间互转
     */
    public static <T1,T2> T2 to(T1 value, T2 defaultValue){

        if(defaultValue == null){
            throw new RuntimeException("transform defaultValue is null");
        }

        if(value.getClass() == defaultValue.getClass()){
            return (T2)value;
        }

        if(value instanceof  Number && defaultValue instanceof Number){
            String type = PrimitiveUtil.getPrimitive(defaultValue.getClass()).getName();
            String methodName = type + "Value";
            try {
                Method method = value.getClass().getMethod(methodName);
                return (T2) method.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }



        return null;
    }

    /**
     * 数值 -> 字面量
     */
    public static <N1 extends Number> String to(N1 value, String defaultValue) {

        if(defaultValue == null){
            throw new RuntimeException("transform defaultValue is null");
        }

        log.info("call "+defaultValue.getClass().getName()+" to(Short value, Byte defaultValue");

        return value.toString();
    }


    /**
     * 数值转char
     */
    public static <N1 extends Number> Character to(N1 value, Character defaultValue) {

        if(value == null){
            return defaultValue;
        }

        return (char)value.intValue();
    }


    /**
     * char转数值
     */
    public static <N1 extends Number> N1 to(Character value, N1 defaultValue) {

        if(value == null){
            return defaultValue;
        }

        return to((int)value.charValue(), defaultValue);
    }


    /**
     * Character -> Character
     **/
    public Character to(Character value, Character defaultValue) {
        log.info("call Character to(Character value, Character defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * Character -> String
     **/
    public String to(Character value, String defaultValue) {
        log.info("call String to(Character value, String defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

//--------------------String-----------------------

    /**
     * String -> Byte
     **/
    public Byte to(String value, Byte defaultValue) {
        log.info("call Byte to(String value, Byte defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> Short
     **/
    public Short to(String value, Short defaultValue) {
        log.info("call Short to(String value, Short defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> Integer
     **/
    public Integer to(String value, Integer defaultValue) {
        log.info("call Integer to(String value, Integer defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> Long
     **/
    public Long to(String value, Long defaultValue) {
        log.info("call Long to(String value, Long defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> Float
     **/
    public Float to(String value, Float defaultValue) {
        log.info("call Float to(String value, Float defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> Double
     **/
    public Double to(String value, Double defaultValue) {
        log.info("call Double to(String value, Double defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> Character
     **/
    public Character to(String value, Character defaultValue) {
        log.info("call Character to(String value, Character defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    /**
     * String -> String
     **/
    public String to(String value, String defaultValue) {
        log.info("call String to(String value, String defaultValue");
        if (value == null) {
            return defaultValue;
        }
        return null;
    }

    public Boolean to(Boolean value, Boolean defaultValue) {
        log.info("to(Boolean value, Boolean defaultValue)");
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
//
//    /**
//     * char[] -> byte[]
//     */
//    public byte[] to(char[] chars) {
//        Charset cs = Charset.forName("UTF-8");
//        CharBuffer cb = CharBuffer.allocate(chars.length);
//        cb.put(chars);
//        cb.flip();
//        ByteBuffer bb = cs.encode(cb);
//        return bb.array();
//    }
//
//    public static char[] to(byte[] bytes) {
//        Charset cs = Charset.forName("UTF-8");
//        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
//        bb.put(bytes);
//        bb.flip();
//        CharBuffer cb = cs.decode(bb);
//        return cb.array();
//    }
//
//    public static byte[] to(char c) {
//        byte[] b = new byte[2];
//        b[0] = (byte) ((c & 0xFF00) >> 8);
//        b[1] = (byte) (c & 0xFF);
//        return b;
//    }
//
//    public char to(byte[] b, char defaultValue) {
//        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
//        return c;
//    }
}
