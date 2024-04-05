package com.zifang.util.expression;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Tests {

    @Test
    public void test001() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("other/js");
        engine.eval("a= 1+1");
        System.out.println(engine.get("a"));
    }
}
