package com.zifang.util.source;

import com.zifang.util.core.util.FileUtil;
import com.zifang.util.source.compiler.CharSequenceJavaFileObject;
import com.zifang.util.source.compiler.CompileContext;
import com.zifang.util.source.compiler.CustomerCompileJavaFileManager;
import com.zifang.util.source.compiler.JdkDynamicCompileClassLoader;

import javax.tools.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompilerTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        DiagnosticCollector<JavaFileObject> DIAGNOSTIC_COLLECTOR = new DiagnosticCollector<>();

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
        // 获取标准的Java文件管理器实例
        StandardJavaFileManager manager = compiler.getStandardFileManager(DIAGNOSTIC_COLLECTOR, null, null);
        // 初始化自定义类加载器
        JdkDynamicCompileClassLoader classLoader = new JdkDynamicCompileClassLoader(Thread.currentThread().getContextClassLoader());
        // 初始化自定义Java文件管理器实例
        CustomerCompileJavaFileManager fileManager = new CustomerCompileJavaFileManager(manager, classLoader);

        // 构建Java源文件实例
        CharSequenceJavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, codeFragment);
        // 添加Java源文件实例到自定义Java文件管理器实例中
        fileManager.addJavaFileObject(
                StandardLocation.SOURCE_PATH,
                packageName,
                className + CharSequenceJavaFileObject.JAVA_EXTENSION,
                javaFileObject
        );
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



//        CompileContext compileContext = new CompileContext();
//
//
//
//        CharSequenceJavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, codeFragment);
//
//        compileContext.addJavaObject(javaFileObject);
    }
}
