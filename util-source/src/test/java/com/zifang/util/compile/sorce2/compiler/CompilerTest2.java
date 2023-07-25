package com.zifang.util.compile.sorce2.compiler;

import com.zifang.util.core.io.FileUtil;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

public class CompilerTest2 {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // 编译上下文
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
                className + JavaFileObject.Kind.SOURCE
        );

        compileContext.initial(); // 编译上下文初始化
        compileContext.addJavaObject(uri, javaFileObject); // 为编译上下增添编译单元
        compileContext.compile(); // 执行编译

        Class<?> klass = compileContext.load(qualifiedName); // 从编译上下文加载类
        Object o = klass.newInstance();
        klass.getMethod("test").invoke(o);
    }
}
