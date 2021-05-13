package com.zifang.util.source.compiler;

import com.zifang.util.core.util.FileUtil;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        String className = "A";
        String packageName = "com.zifang.util.sssss";
        String qualifiedName = packageName + "." + className;
        String codeFragment = FileUtil.readFile("A.java");

        // 获取系统编译器实例
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 设置编译参数 - 指定编译版本为JDK1.6以提高兼容性
        List<String> options = new ArrayList<>();
        options.add("-source");
        options.add("1.8");
        options.add("-target");
        options.add("1.8");

        // 源代码验证信息集合
        DiagnosticCollector<JavaFileObject> DIAGNOSTIC_COLLECTOR = new DiagnosticCollector<>();
        // 获取标准的Java文件管理器实例
        StandardJavaFileManager manager = compiler.getStandardFileManager(DIAGNOSTIC_COLLECTOR, null, null);
        // 初始化自定义类加载器
        CustomerCompileClassLoader classLoader = new CustomerCompileClassLoader(Thread.currentThread().getContextClassLoader());
        // 初始化自定义Java文件管理器实例
        CustomerCompileJavaFileManager fileManager = new CustomerCompileJavaFileManager(manager, classLoader);

        // 构建Java源文件实例
        CharSequenceJavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, codeFragment);
        URI uri = URI.create(qualifiedName); // 没什么特殊的作用

        // 添加Java源文件实例到自定义Java文件管理器实例中
        fileManager.addJavaFileObject(uri, javaFileObject);

        // 初始化一个编译任务实例
        JavaCompiler.CompilationTask compilationTask = compiler.getTask(
                null,
                fileManager,
                DIAGNOSTIC_COLLECTOR,
                options,
                null,
                Arrays.asList(javaFileObject)
        );
        // 执行编译任务
        Boolean result = compilationTask.call();
        System.out.println(String.format("编译[%s]结果:%s", qualifiedName, result));
        Class<?> klass = classLoader.loadClass(qualifiedName);
        Object o = klass.newInstance();
        klass.getMethod("test").invoke(o);
    }
}
