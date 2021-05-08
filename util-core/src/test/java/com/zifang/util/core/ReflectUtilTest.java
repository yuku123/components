package com.zifang.util.core;

import com.zifang.util.core.lang.converter.Converter;
import com.zifang.util.core.lang.reflect.ReflectUtil;
import org.junit.Test;

import java.lang.reflect.Type;

public class ReflectUtilTest {

    @Test
    public void test0(){
        Type type1 = ReflectUtil.getGenericInterfaceType(A.class,Converter.class);
        Type type2 = ReflectUtil.getGenericInterfaceType(B.class,Converter.class);
        Type type3 = ReflectUtil.getGenericInterfaceType(C.class,Converter.class);
        assert type1 != null;
        assert type2 != null;
        assert type3 != null;
    }
}


class A implements Converter<Integer,Long> {

    @Override
    public Long to(Integer integer, Long aLong) {
        return null;
    }
}

class B extends A {

    @Override
    public Long to(Integer integer, Long aLong) {
        return null;
    }
}


class C implements D {

    @Override
    public Long to(Integer integer, Long aLong) {
        return null;
    }
}

interface D extends Converter<Integer,Long>{

}
