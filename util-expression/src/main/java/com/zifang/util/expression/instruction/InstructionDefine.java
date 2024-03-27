package com.zifang.util.expression.instruction;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InstructionDefine {

    public static Map<String, Method> instructionMap = new HashMap<>();

    @CommandAnnotation("idda")
    public static void idda() {

    }


    public static void init() {
        for (Method method : InstructionDefine.class.getMethods()) {
            String instructionCode = method.getAnnotation(CommandAnnotation.class).value();
            instructionMap.put(instructionCode, method);
        }
    }
}
