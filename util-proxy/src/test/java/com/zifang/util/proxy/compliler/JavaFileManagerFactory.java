package com.zifang.util.proxy.compliler;

import javax.tools.StandardJavaFileManager;
import java.util.logging.Logger;

public class JavaFileManagerFactory {

    private static final Logger log = Logger.getLogger(JavaFileManagerFactory.class.getName());

    public static CFJavaFileManager getJavaFileManager(StandardJavaFileManager standardManager) {
//        Class clazz = JavaFileManagerFactory.class.getClassLoader().getClass();
//        if ("org.springframework.boot.loader.LaunchedURLClassLoader".equals(clazz.getName())
//                || "com.taobao.pandora.boot.loader.LaunchedURLClassLoader".equals(clazz.getName())) {
//
//            log.info("using SpringBootJavaFileManager classLoader:{}", clazz);
//            return new SpringBootJavaFileManager(standardManager);
//        }
//        //spring-boot idea环境启动的场景，使用CommonJavaFileManager
//        return new CommonJavaFileManager(standardManager);
        return null;
    }
}
