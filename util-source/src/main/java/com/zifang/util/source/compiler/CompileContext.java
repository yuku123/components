package com.zifang.util.source.compiler;

import javax.tools.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态 编译 执行的一次上下文
 */
public class CompileContext {

    // 上下文的唯一标识
    public static Long id = System.currentTimeMillis();

    // 获取系统编译器实例
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private CustomerCompileJavaFileManager customerCompileJavaFileManager;

    private CustomerCompileClassLoader classLoader;

    private StandardJavaFileManager standardJavaFileManager;

    private DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

    private List<CharSequenceJavaFileObject> charSequenceJavaFileObjects = new ArrayList<>();


    public void addJavaObject(URI uri, CharSequenceJavaFileObject javaFileObject) {

        this.customerCompileJavaFileManager.addJavaFileObject(uri, javaFileObject);

        charSequenceJavaFileObjects.add(javaFileObject);

    }

    /**
     * 当前的编译器上下文 初始化
     */
    public void initial() {

        // 获取标准的Java文件管理器实例
        standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        // 初始化自定义类加载器
        classLoader = new CustomerCompileClassLoader(Thread.currentThread().getContextClassLoader());
        // 初始化自定义Java文件管理器实例
        customerCompileJavaFileManager = new CustomerCompileJavaFileManager(standardJavaFileManager, classLoader);
    }

    public void compile() {

        // 设置编译参数 - 指定编译版本为JDK1.6以提高兼容性
        List<String> options = new ArrayList<>();
        options.add("-source");
        options.add("1.8");
        options.add("-target");
        options.add("1.8");

        // 初始化一个编译任务实例
        JavaCompiler.CompilationTask compilationTask = compiler.getTask(
                null,
                customerCompileJavaFileManager,
                diagnosticCollector,
                options,
                null,
                charSequenceJavaFileObjects
        );
        // 执行编译任务
        Boolean result = compilationTask.call();

        System.out.println("编译状况：" + result);

    }

    // 从当前的编译上下文获得到这个类
    public Class<?> load(String className) {
        try {
            return this.classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
