package com.zifang.util.source.generator.info;


import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;


public class ModifierAdapter {

    private static Map<Integer,com.github.javaparser.ast.Modifier> modifierMap = new LinkedHashMap<Integer,com.github.javaparser.ast.Modifier>(){
        {
            put(Modifier.PUBLIC,com.github.javaparser.ast.Modifier.publicModifier());
            put(Modifier.PRIVATE,com.github.javaparser.ast.Modifier.privateModifier());
            put(Modifier.STATIC,com.github.javaparser.ast.Modifier.staticModifier());

        }
    };

    /**
     * 提供给javaparser的
     * */
    public static com.github.javaparser.ast.Modifier.Keyword getKeyWord(Integer modifier){
        return modifierMap.get(modifier).getKeyword();
    }
}
