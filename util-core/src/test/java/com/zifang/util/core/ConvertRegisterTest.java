package com.zifang.util.core;

import com.zifang.util.core.lang.converter.Converters;
import com.zifang.util.core.lang.converter.IConverter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ConvertRegisterTest {

    @Test
    public void test001() {
        assert Converters.caller(Byte.class, Byte.class).to((byte) 1) == ((byte) 1);
        assert Converters.caller(Byte.class, Short.class).to((byte) 1) == ((short) 1);
        assert Converters.caller(Byte.class, Integer.class).to((byte)1) == 1;
        assert Converters.caller(Byte.class, Long.class).to( (byte)1) == 1L;
        assert Converters.caller(Byte.class, Float.class).to((byte)1) == 1F;
        assert Converters.caller(Byte.class, Double.class).to((byte)1) == 1D;
        assert Converters.caller(Byte.class, Character.class).to((byte)1) == (char)1;
        assert Converters.caller(Byte.class, String.class).to((byte)1).equals("1");

        assert Converters.caller(Short.class, Byte.class).to((short) 1) == ((byte) 1);
        assert Converters.caller(Short.class, Short.class).to((short) 1) == ((short) 1);
        assert Converters.caller(Short.class, Integer.class).to((short)1) == 1;
        assert Converters.caller(Short.class, Long.class).to( (short)1) == 1L;
        assert Converters.caller(Short.class, Float.class).to((short)1) == 1F;
        assert Converters.caller(Short.class, Double.class).to((short)1) == 1D;
        assert Converters.caller(Short.class, Character.class).to((short)1) == (char)1;
        assert Converters.caller(Short.class, String.class).to((short)1).equals("1");

        assert Converters.caller(Integer.class, Byte.class).to( 1) == ((byte) 1);
        assert Converters.caller(Integer.class, Short.class).to(1) == ((short) 1);
        assert Converters.caller(Integer.class, Integer.class).to(1) == 1;
        assert Converters.caller(Integer.class, Long.class).to( 1) == 1L;
        assert Converters.caller(Integer.class, Float.class).to(1) == 1F;
        assert Converters.caller(Integer.class, Double.class).to(1) == 1D;
        assert Converters.caller(Integer.class, Character.class).to(1) == (char)1;
        assert Converters.caller(Integer.class, String.class).to(1).equals("1");

        assert Converters.caller(Float.class, Byte.class).to( 1.0f) == ((byte) 1);
        assert Converters.caller(Float.class, Short.class).to(1.0f) == ((short) 1);
        assert Converters.caller(Float.class, Integer.class).to(1.0f) == 1;
        assert Converters.caller(Float.class, Long.class).to( 1.0f) == 1L;
        assert Converters.caller(Float.class, Float.class).to(1.0f) == 1F;
        assert Converters.caller(Float.class, Double.class).to(1.0f) == 1D;
        assert Converters.caller(Float.class, Character.class).to(1.0f) == (char)1;
        assert Converters.caller(Float.class, String.class).to(1.0f).equals("1.0");

        assert Converters.caller(Double.class, Byte.class).to( 1.0d) == ((byte) 1);
        assert Converters.caller(Double.class, Short.class).to(1.0d) == ((short) 1);
        assert Converters.caller(Double.class, Integer.class).to(1.0d) == 1;
        assert Converters.caller(Double.class, Long.class).to( 1.0d) == 1L;
        assert Converters.caller(Double.class, Float.class).to(1.0d) == 1F;
        assert Converters.caller(Double.class, Double.class).to(1.0d) == 1D;
        assert Converters.caller(Double.class, Character.class).to(1.0d) == (char)1;
        assert Converters.caller(Double.class, String.class).to(1.0d).equals("1.0");

        assert Converters.caller(String.class, Byte.class).to( "1") == ((byte) 1);
        assert Converters.caller(String.class, Short.class).to("1") == ((short) 1);
        assert Converters.caller(String.class, Integer.class).to("1") == 1;
        assert Converters.caller(String.class, Long.class).to( "1") == 1L;
        assert Converters.caller(String.class, Float.class).to("1") == 1F;
        assert Converters.caller(String.class, Double.class).to("1") == 1D;
        assert Converters.caller(String.class, Character.class).to("1") == '1';
        assert Converters.caller(String.class, String.class).to("1").equals("1");

        assert Converters.caller(Character.class, Byte.class).to('1') == ((byte) '1');
        assert Converters.caller(Character.class, Short.class).to('1') == ((short) '1');
        assert Converters.caller(Character.class, Integer.class).to('1') == (int)'1';
        assert Converters.caller(Character.class, Long.class).to( '1') == (long)'1';
        assert Converters.caller(Character.class, Float.class).to('1') == (float)'1';
        assert Converters.caller(Character.class, Double.class).to('1') == (double)'1';
        assert Converters.caller(Character.class, Character.class).to('1') == '1';
        assert Converters.caller(Character.class, String.class).to('1').equals("1");
    }

    @Test
    public void test002(){
        assert Converters.caller(Byte.class, Byte.class).to(null) == ((byte) 0);
        assert Converters.caller(Byte.class, Short.class).to(null) == ((short) 0);
        assert Converters.caller(Byte.class, Integer.class).to(null) == 0;
        assert Converters.caller(Byte.class, Long.class).to(null) == 0L;
        assert Converters.caller(Byte.class, Float.class).to(null) == 0F;
        assert Converters.caller(Byte.class, Double.class).to(null) == 0D;
        assert Converters.caller(Byte.class, Character.class).to(null) == '0';
        assert Converters.caller(Byte.class, String.class).to(null).equals("");
    }

    @Test
    public void test003(){
        IConverter<Map<String,String>, String> converter = new IConverter<Map<String,String>, String>(){
            @Override
            public String to(Map<String,String> value, String defaultValue) {
                return String.join(",",value.keySet());
            }
        };

        Converters.registerConverter(converter);

        Map<String,String> m = new HashMap<>();
        m.put("a","a");
        m.put("b","b");

        assert Converters.caller(Map.class, String.class).to(m).equals("a,b");
    }

    @Test
    public void test004(){
        IConverter<Map<String,String>, String> converter = (value, defaultValue) -> String.join(",",value.keySet());
        Converters.registerConverter(converter);

        Map<String,String> m = new HashMap<>();
        m.put("a","a");
        m.put("b","b");

        assert Converters.caller(Map.class, String.class).to(m).equals("a,b");
    }

    @Test
    public void test005(){
        IConverter<Map<String,String>, String> converter = (value, defaultValue) -> String.join(",",value.keySet());
        Converters.registerConverter(converter, Map.class, String.class);

        Map<String,String> m = new HashMap<>();
        m.put("a","a");
        m.put("b","b");

        assert Converters.caller(Map.class, String.class).to(m).equals("a,b");
    }

}
