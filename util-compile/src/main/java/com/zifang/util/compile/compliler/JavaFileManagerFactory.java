package com.zifang.util.compile.compliler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.StandardJavaFileManager;

public class JavaFileManagerFactory {

    private static final Logger logger = LoggerFactory.getLogger(JavaFileManagerFactory.class);

    public static CFJavaFileManager getJavaFileManager(StandardJavaFileManager standardManager) {
//        Class clazz = JavaFileManagerFactory.class.getClassLoader().getClass();
//        if ("org.springframework.boot.loader.LaunchedURLClassLoader".equals(clazz.getName())
//                || "com.taobao.pandora.boot.loader.LaunchedURLClassLoader".equals(clazz.getName())) {
//
//            logger.info("using SpringBootJavaFileManager classLoader:{}", clazz);
//            return new SpringBootJavaFileManager(standardManager);
//        }
//        //spring-boot idea环境启动的场景，使用CommonJavaFileManager
//        return new CommonJavaFileManager(standardManager);
        return null;
    }
}
