package com.zifang.util.core.lang.tuples;


import com.zifang.util.core.beans.BeanUtils;
import com.zifang.util.core.util.GsonUtil;
import lombok.Data;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Unit<A> {
    protected A a;

    public Unit(A a){
        this.a = a;
    }

    protected Map<String,Object> toMap(){
        try {
            return BeanUtils.beanToMap(this);
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }
}
