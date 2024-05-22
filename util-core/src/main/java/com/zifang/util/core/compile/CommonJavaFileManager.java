package com.zifang.util.core.compile;

import javax.tools.JavaFileManager;

public class CommonJavaFileManager extends CustomerJavaFileManager {
    protected CommonJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }
}
