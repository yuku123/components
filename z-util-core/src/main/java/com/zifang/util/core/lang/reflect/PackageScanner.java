package com.zifang.util.core.lang.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描器。jar 与目录两条分支都按真实的资源 URL 定位，
 * 类以 initialize=false 加载，避免扫描动作触发目标类的静态初始化。
 *
 * @author zifang
 */
public class PackageScanner {

    private static final Logger log = LoggerFactory.getLogger(PackageScanner.class);
    private static final String CLASS_SUFFIX = ".class";
    private static final String JAR = "jar";
    private static final String FILE = "file";

    /***
     * 获得包下的所有类
     *
     * @param packageName 包名
     *
     * @return 该包下的类集合
     *
     * */
    public static Set<Class<?>> searchClasses(String packageName) {
        return searchClasses(packageName, null);
    }

    /***
     * 获得包下的所有类
     *
     * @param packageName 包名
     *
     * @param predicate 过滤器
     *
     * @return 该包下的类集合
     *
     * */
    public static Set<Class<?>> searchClasses(String packageName, Predicate<Class<?>> predicate) {
        Set<Class<?>> set = search(packageName);
        if (predicate == null) {
            return set;
        }
        Set<Class<?>> filtered = new LinkedHashSet<>();
        for (Class<?> clazz : set) {
            if (predicate.test(clazz)) {
                filtered.add(clazz);
            }
        }
        return filtered;
    }

    private static Set<Class<?>> search(String packageName) {
        Set<String> classNames = new LinkedHashSet<>();
        String packagePath = packageName.replace(".", "/");
        ClassLoader loader = classLoader();
        try {
            for (URL url : locatePackage(loader, packagePath)) {
                String protocol = url.getProtocol();
                if (JAR.equalsIgnoreCase(protocol)) {
                    collectFromJar(url, packagePath, classNames);
                } else if (FILE.equalsIgnoreCase(protocol)) {
                    collectFromDirectory(url, packagePath, classNames);
                } else {
                    log.warn("跳过不支持的协议 {}，包 {} 的资源地址 {}", protocol, packageName, url);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("扫描包失败: " + packageName, e);
        }

        Set<Class<?>> classes = new LinkedHashSet<>();
        for (String className : classNames) {
            try {
                classes.add(Class.forName(className, false, loader));
            } catch (ClassNotFoundException | LinkageError e) {
                log.error("扫描包 {} 时类 {} 加载失败，已跳过", packageName, className, e);
            }
        }
        return classes;
    }

    /**
     * 定位包的资源地址。直接按包路径取资源在"类路径被 jar 清单引用"的场景下取不到，
     * 因此再从各个 classpath 根往下拼一次。
     */
    private static Set<URL> locatePackage(ClassLoader loader, String packagePath) throws IOException {
        Set<URL> urls = new LinkedHashSet<>();
        Enumeration<URL> direct = loader.getResources(packagePath);
        while (direct.hasMoreElements()) {
            urls.add(direct.nextElement());
        }
        Enumeration<URL> roots = loader.getResources("");
        while (roots.hasMoreElements()) {
            URL root = roots.nextElement();
            if (!FILE.equalsIgnoreCase(root.getProtocol())) {
                continue;
            }
            File candidate = new File(root.getFile(), packagePath);
            if (candidate.isDirectory()) {
                urls.add(candidate.toURI().toURL());
            }
        }
        return urls;
    }

    private static void collectFromJar(URL url, String packagePath, Set<String> classNames) throws IOException {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        try {
            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            String prefix = packagePath.endsWith("/") ? packagePath : packagePath + "/";
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory() || !name.startsWith(prefix) || !name.endsWith(CLASS_SUFFIX)) {
                    continue;
                }
                classNames.add(name.substring(0, name.length() - CLASS_SUFFIX.length()).replace('/', '.'));
            }
        } finally {
            connection.getJarFile().close();
        }
    }

    private static void collectFromDirectory(URL url, String packagePath, Set<String> classNames) throws IOException {
        String root = URLDecoder.decode(url.getFile(), "UTF-8");
        File directory = new File(root);
        if (!directory.isDirectory()) {
            log.warn("包 {} 的资源地址不是目录，已跳过: {}", packagePath, root);
            return;
        }
        String packageName = packagePath.replace('/', '.');
        collect(directory, packageName, classNames);
    }

    private static void collect(File directory, String packageName, Set<String> classNames) {
        File[] files = directory.listFiles();
        if (files == null) {
            log.warn("目录不可读，已跳过: {}", directory);
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                collect(file, packageName + "." + file.getName(), classNames);
            } else if (file.getName().endsWith(CLASS_SUFFIX)) {
                classNames.add(packageName + "."
                        + file.getName().substring(0, file.getName().length() - CLASS_SUFFIX.length()));
            }
        }
    }

    private static ClassLoader classLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader != null ? loader : PackageScanner.class.getClassLoader();
    }
}