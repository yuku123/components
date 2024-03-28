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

    public static <T1,T2> T2 to(T1 value, T2 defaultValue){

        if(value == null){
            return defaultValue;
        }

        if(defaultValue == null){
            throw new RuntimeException("transform defaultValue is null");
        }

        if(value.getClass() == defaultValue.getClass()){
            return (T2)value;
        }

        if(value.getClass() == Boolean.class){
            if(defaultValue.getClass() == String.class){
                return (T2)((Boolean)value).toString();
            } else {
                throw new RuntimeException("boolean cannot transformTo" + defaultValue.getClass().getName());
            }
        }

        if(value.getClass() == Character.class){

            if(defaultValue.getClass() == String.class){
                return (T2)(value.toString());
            }

            if(defaultValue instanceof Number){
                return to((int)((Character)value).charValue(),defaultValue);
            }
        }

        if(value.getClass() == String.class){
            if(defaultValue.getClass() == Boolean.class){
                return (T2)(Boolean.valueOf(value.toString()));
            }
            if(defaultValue.getClass() == Character.class){
                String str = value.toString();
                if(str.length() != 1){
                    throw new RuntimeException("inputString:"+value+",length>1, cannot transformToChar");
                } else {
                    return (T2)(Character.valueOf(str.charAt(0)));
                }
            }

            if(defaultValue instanceof Number){
                try {
                    Method method = defaultValue.getClass().getMethod("valueOf",String.class);
                    return (T2)method.invoke(null, value);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(value instanceof  Number){

            if(defaultValue.getClass() == Boolean.class){
                throw new RuntimeException("can not transform value "+value +"to boolean");
            }

            if(defaultValue.getClass() == Character.class){
                return (T2)Character.valueOf((char) ((Number) value).intValue());
            }

            if(defaultValue.getClass() == String.class){
                return (T2)value.toString();
            }

            if(defaultValue instanceof Number){
                String type = PrimitiveUtil.getPrimitive(defaultValue.getClass()).getName();
                String methodName = type + "Value";
                try {
                    Method method = value.getClass().getMethod(methodName);
                    return (T2) method.invoke(value);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
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
