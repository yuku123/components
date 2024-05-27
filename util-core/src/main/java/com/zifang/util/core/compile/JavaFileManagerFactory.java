package com.zifang.util.core.compile;


import javax.tools.StandardJavaFileManager;
import java.util.logging.Logger;

public class JavaFileManagerFactory {

    private static final Logger log = Logger.getLogger(JavaFileManagerFactory.class.getName());

    public static CustomerJavaFileManager getJavaFileManager(StandardJavaFileManager standardManager) {
        return new CommonJavaFileManager(standardManager);
    }
}
