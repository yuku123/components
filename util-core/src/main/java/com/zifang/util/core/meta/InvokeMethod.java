package com.zifang.util.core.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokeMethod {

    /***
     * An instance of the class in which the method is located
     * 方法所在的类的实例
     */
    private Object target;

    private MethodHandle methodHandle;

    private Method method;
}
