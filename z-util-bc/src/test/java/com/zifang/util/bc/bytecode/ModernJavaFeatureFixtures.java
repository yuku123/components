package com.zifang.util.bc.bytecode;

import com.zifang.util.bc.compile.BytesJavaFileObject;
import com.zifang.util.bc.compile.CFJavaCompiler;
import com.zifang.util.bc.compile.MapClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 现编现用的 fixture 源码
 * <p>
 * 解析层的现代特性测试需要真实的 javac 产物（record / sealed / 模块 / 注解默认值 /
 * 类型注解 / 动态常量），手写字节码既繁琐又容易把测试写成对自身实现的复读。
 * 本模块自身按 Java 8 编译，因此 fixture 源码用字符串拼接而非文本块。
 */
final class ModernJavaFeatureFixtures {

    private ModernJavaFeatureFixtures() {
    }

    static Map<String, byte[]> compile(String className, String source) {
        Map<String, BytesJavaFileObject> compiled;
        try {
            compiled = CFJavaCompiler.compile(className, source);
        } catch (Exception e) {
            throw new IllegalStateException("fixture 编译异常: " + className, e);
        }
        if (compiled.isEmpty()) {
            throw new IllegalStateException("fixture 编译失败: " + className);
        }
        Map<String, byte[]> bytes = new LinkedHashMap<>();
        for (Map.Entry<String, BytesJavaFileObject> e : compiled.entrySet()) {
            bytes.put(e.getKey(), e.getValue().getBytes());
        }
        return bytes;
    }

    static byte[] compileOne(String className, String source) {
        return compile(className, source).get(className);
    }

    static Class<?> load(String className, String source) throws Exception {
        return new MapClassLoader(compile(className, source)).loadClass(className);
    }

    /**
     * 带额外 javac 选项编译单类
     * <p>
     * 形参名表（MethodParameters）默认不进 class 文件，必须显式 -parameters 才产出，
     * 而模块自带的 {@code CFJavaCompiler} 固定只传编码与 classpath，因此单独走一次 ToolProvider。
     *
     * @param className 类名
     * @param source    源码
     * @param options   额外 javac 选项
     * @return byte[]类型返回值
     */
    static byte[] compileWithOptions(String className, String source, String... options) {
        Path outputDir = null;
        try {
            outputDir = Files.createTempDirectory("zutil-bc-fixture");
            List<String> args = new ArrayList<>();
            args.add("-d");
            args.add(outputDir.toString());
            for (String option : options) {
                args.add(option);
            }
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
            // 本模块按 Java 8 API 编译，JavaCompiler.run 的带源文件重载是 Java 9 才有的，
            // 因此走 Java 6 就存在的 getTask。
            javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(
                    null, null, new javax.tools.DiagnosticCollector<javax.tools.JavaFileObject>(),
                    args, null,
                    Collections.singletonList(new javax.tools.SimpleJavaFileObject(
                            URI.create("string:///" + className.replace('.', '/') + ".java"),
                            javax.tools.JavaFileObject.Kind.SOURCE) {
                        @Override
                        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                            return source;
                        }
                    }));
            if (!Boolean.TRUE.equals(task.call())) {
                throw new IllegalStateException("fixture 编译失败: " + err.toString());
            }
            return Files.readAllBytes(outputDir.resolve(className.replace('.', '/') + ".class"));
        } catch (IOException e) {
            throw new IllegalStateException("fixture 编译异常: " + className, e);
        } finally {
            deleteRecursively(outputDir);
        }
    }

    private static void deleteRecursively(Path dir) {
        if (dir == null) {
            return;
        }
        try (java.util.stream.Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ignored) {
                    // 临时目录删除失败不影响断言
                }
            });
        } catch (IOException ignored) {
            // 临时目录删除失败不影响断言
        }
    }
}
