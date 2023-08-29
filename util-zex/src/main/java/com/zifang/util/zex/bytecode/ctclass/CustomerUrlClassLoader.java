package com.zifang.util.zex.bytecode.ctclass;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义的url的classLoader
 */
@Slf4j
public class CustomerUrlClassLoader extends URLClassLoader {

    /**
     * 扫描过 过程中记录jar名字与下面的类名列表
     */
    private Map<String, List<String>> jarPathClassListMapper = new LinkedHashMap<>();


    public CustomerUrlClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
