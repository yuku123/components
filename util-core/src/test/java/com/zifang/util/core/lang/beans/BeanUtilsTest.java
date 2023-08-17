package com.zifang.util.core.lang.beans;

import com.zifang.util.core.lang.BeanUtils;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {

    @Test
    public void isBeanTest() {
        Assert.assertFalse(BeanUtils.isBean(new IsBeanTest1()));
        Assert.assertTrue(BeanUtils.isBean(new IsBeanTest2()));
        Assert.assertFalse(BeanUtils.isBean(new IsBeanTest3()));
        Assert.assertTrue(BeanUtils.isBean(new IsBeanTest4()));
    }

    @Test
    public void mapToBeanTest() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("baseByteType", (byte) 1);
        map.put("baseCharType", 'c');
        map.put("baseIntType", 2);
        map.put("baseLongType", 3L);
        map.put("baseFloatType", 1.2f);
        map.put("baseDoubleType", 1.4d);
        map.put("byteWapperType", new Byte("9"));
        map.put("charWapperType", new Character('c'));
        map.put("intWapperType", new Integer("11"));
        map.put("longWapperType", new Long("12"));
        map.put("floatWapperType", new Float("9.1"));
        map.put("doubleWapperType", new Double(22.22d));
        map.put("stringType", "s");
        Person person = BeanUtils.mapToBean(Person.class, map);
        Assert.assertEquals((byte) 1, person.getBaseByteType());
        Assert.assertEquals('c', person.getBaseCharType());
        Assert.assertEquals(2, person.getBaseIntType());
        Assert.assertEquals(3L, person.getBaseLongType());
        Assert.assertEquals("message", 1.2f, person.getBaseFloatType(), 0.01);
        Assert.assertEquals("message", 1.4d, person.getBaseDoubleType(), 0.01);
        Assert.assertEquals(new Byte("9"), person.getByteWapperType());
        Assert.assertEquals(new Character('c'), person.getCharWapperType());
        Assert.assertEquals(new Integer("11"), person.getIntWapperType());
        Assert.assertEquals(new Long("12"), person.getLongWapperType());
        Assert.assertEquals(new Float("9.1"), person.getFloatWapperType());
        Assert.assertEquals(new Double(22.22d), person.getDoubleWapperType());
        Assert.assertEquals("s", person.getStringType());
    }
}


@Data
class Person {

    private byte baseByteType;
    private char baseCharType;
    private int baseIntType;
    private long baseLongType;
    private float baseFloatType;
    private double baseDoubleType;

    private Byte byteWapperType;
    private Character charWapperType;
    private Integer intWapperType;
    private Long longWapperType;
    private Float floatWapperType;
    private Double doubleWapperType;

    private String stringType;
}

class IsBeanTest1 {
    private String name;
}

class IsBeanTest2 {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}

class IsBeanTest3 {
    private String name;

    public String getName() {
        return name;
    }
}

class IsBeanTest4 {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}