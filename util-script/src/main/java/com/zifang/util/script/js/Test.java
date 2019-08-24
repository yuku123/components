package com.zifang.util.script.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Test {
    public static void main(String[] args) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
        engine.eval("1+1");
        System.out.println(engine.get("a"));
    }
}
