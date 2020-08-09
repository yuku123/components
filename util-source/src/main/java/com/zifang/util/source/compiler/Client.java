package com.zifang.util.source.compiler;

import javax.tools.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

    static String SOURCE_CODE = "package com.zifang.util.source.compiler;\n" +
            "\n" +
            "public class JdkDynamicCompileHelloService {\n" +
            "\n" +
            "    public void test() {\n" +
            "        System.out.println(\"吃饭\");\n" +
            "    }\n" +
            "}";

    // 编译诊断收集器
    static DiagnosticCollector<JavaFileObject> DIAGNOSTIC_COLLECTOR = new DiagnosticCollector<>();

    public static void main(String[] args) throws Exception {
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
        String packageName = "com.zifang.util.source.compiler";
        String className = "JdkDynamicCompileHelloService";
        String qualifiedName = packageName + "." + className;
        // 构建Java源文件实例
        CharSequenceJavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, SOURCE_CODE);
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
    }
}