package com.zifang.util.core.lang.converter;

import com.zifang.util.core.lang.PrimitiveUtil;
import com.zifang.util.core.lang.beans.tuples.Pair;

import java.lang.reflect.Method;

public class Converter {

    private static ConvertCaller defaultCaller = new ConvertCaller();

    public static ConvertCaller caller(Class<?> from, Class<?> target) {
        if (PrimitiveUtil.getPrimitiveWrapper(from) == PrimitiveUtil.getPrimitiveWrapper(target)) {
            return defaultCaller;
        }
        ConvertCaller convertCaller = new ConvertCaller();
        Pair<Method, Object> pair = ConvertRegister.find(
                PrimitiveUtil.getPrimitiveWrapper(from),
                PrimitiveUtil.getPrimitiveWrapper(target)
        );
        convertCaller.setMethod(pair.getA());
        convertCaller.setCaller(pair.getB());
        convertCaller.setFrom(from);
        convertCaller.setTarget(target);
        return convertCaller;
    }
}
