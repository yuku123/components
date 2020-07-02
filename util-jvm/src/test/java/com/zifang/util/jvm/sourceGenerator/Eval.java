package com.zifang.util.jvm.sourceGenerator;

import com.zifang.util.jvm.compiler.StringJavaFileObject;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Eval {

    private Class<?> compile(String className, String javaCodes) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        StringJavaFileObject srcObject = new StringJavaFileObject(className, javaCodes);
        Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(srcObject);
        String flag = "-d";
        String outDir = "";
        try {
            File classPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            outDir = classPath.getAbsolutePath() + File.separator;
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        Iterable<String> options = Arrays.asList(flag, outDir);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, fileObjects);
        boolean result = task.call();
        if (result == true) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Object run(String method, String codes) {

        String className = "com.test.Eval";
        StringBuilder sb = new StringBuilder();
        sb.append("package com.test;");
        sb.append("\n public class Eval{\n ");
        sb.append(codes);
        sb.append("\n}");

        Class<?> clazz = compile(className, sb.toString());
        try {
            // 生成对象
            Object obj = clazz.newInstance();
            Class<? extends Object> cls = obj.getClass();
            // 调用main方法
            Method m = clazz.getMethod(method, String[].class);
            Object invoke = m.invoke(obj, new Object[]{new String[]{}});
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object eval() {
        Eval eval = new Eval();
        String method = "main";

        String codes = "public static void main(String[]args){" +
                "System.out.print(\"hello world\"); }";

        eval.run(method, codes);
        return null;
    }


    public static void main(String[] args) {
        eval();
    }
}