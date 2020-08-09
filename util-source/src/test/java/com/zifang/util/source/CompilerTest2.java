package com.zifang.util.source;

import com.zifang.util.core.util.FileUtil;
import com.zifang.util.source.compiler.CharSequenceJavaFileObject;
import com.zifang.util.source.compiler.CompileContext;
import com.zifang.util.source.compiler.CustomerCompileJavaFileManager;
import com.zifang.util.source.compiler.CustomerCompileClassLoader;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerTest2 {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {


        CompileContext compileContext = new CompileContext();

        String className = "A";
        String packageName = "com.zifang.util.sssss";
        String qualifiedName = packageName + "." + className;
        String codeFragment = FileUtil.readFile("A.java");

        // 构建Java源文件实例
        CharSequenceJavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, codeFragment);
        URI uri = CustomerCompileJavaFileManager.fromLocation(
                StandardLocation.SOURCE_PATH,
                packageName,
                className + CharSequenceJavaFileObject.JAVA_EXTENSION
        );

        compileContext.initial(); // 编译上下文初始化
        compileContext.addJavaObject(uri,javaFileObject); // 为编译上下增添编译单元
        compileContext.compile(); // 执行编译

        Class<?> klass = compileContext.load(qualifiedName); // 从编译上下文加载类
        Object o = klass.newInstance();
        klass.getMethod("test").invoke(o);
    }
}
