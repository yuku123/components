package com.zifang.util.http.common;

import com.zifang.util.core.lang.beans.tuples.Pair;

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
