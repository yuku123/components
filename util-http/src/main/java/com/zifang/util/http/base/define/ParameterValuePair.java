package com.zifang.util.http.base.define;

import com.zifang.util.core.lang.tuples.Pair;

import java.lang.reflect.Parameter;

public class ParameterValuePair extends Pair<Parameter, Object> {

    public ParameterValuePair(Parameter parameter, Object obj) {
        super(parameter, obj);
    }

    public Parameter getParameter() {
        return getA();
    }

    public Object getObj() {
        return getB();
    }


}
