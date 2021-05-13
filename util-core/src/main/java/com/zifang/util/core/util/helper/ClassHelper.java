//package com.zifang.util.core.util.helper;
//
//
//import com.zifang.util.core.util.AssertUtil;
//import com.zifang.util.core.util.ClassLoaderUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.util.*;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//
///**
// * <h6>Description:<h6>
// * <p>Java Class与反射相关的一些工具类</p>
// */
//@Slf4j
//public final class ClassHelper {
//
//
//    public final static String[] getField(String className, boolean extendsField) {
//        Class classz = loadClass(className);
//        Field[] fields = classz.getFields();
//        Set<String> set = new HashSet<>();
//        if (fields != null) {
//            for (Field f : fields) {
//                set.add(f.getName());
//            }
//        }
//        if (extendsField) {
//            Field[] fieldz = classz.getDeclaredFields();
//            if (fieldz != null) {
//                for (Field f : fieldz) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//    public final static String[] getPublicField(String className, boolean extendsField) {
//        Class classz = loadClass(className);
//        Set<String> set = new HashSet<>();
//        Field[] fields = classz.getDeclaredFields();
//        if (fields != null) {
//            for (Field f : fields) {
//                String modifier = Modifier.toString(f.getModifiers());
//                if (modifier.startsWith("public")) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        if (extendsField) {
//            Field[] fieldz = classz.getFields();
//            if (fieldz != null) {
//                for (Field f : fieldz) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//    public final static String[] getProtectedField(String className) {
//        Class classz = loadClass(className);
//        Set<String> set = new HashSet<>();
//        Field[] fields = classz.getDeclaredFields();
//        if (fields != null) {
//            for (Field f : fields) {
//                String modifier = Modifier.toString(f.getModifiers());
//                if (modifier.startsWith("protected")) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//    public final static String[] getPrivateField(String className) {
//        Class classz = loadClass(className);
//        Set<String> set = new HashSet<>();
//        Field[] fields = classz.getDeclaredFields();
//        if (fields != null) {
//            for (Field f : fields) {
//                String modifier = Modifier.toString(f.getModifiers());
//                if (modifier.startsWith("private")) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//    public final static String[] getPublicMethod(String className, boolean extendsMethod) {
//        Class classz = loadClass(className);
//        Method[] methods;
//        if (extendsMethod) {
//            methods = classz.getMethods();
//        } else {
//            methods = classz.getDeclaredMethods();
//        }
//        Set<String> set = new HashSet<>();
//        if (methods != null) {
//            for (Method f : methods) {
//                String modifier = Modifier.toString(f.getModifiers());
//                if (modifier.startsWith("public")) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//
//    public final static String[] getProtectedMethod(String className, boolean extendsMethod) {
//        Class classz = loadClass(className);
//        Method[] methods;
//        if (extendsMethod) {
//            methods = classz.getMethods();
//        } else {
//            methods = classz.getDeclaredMethods();
//        }
//        Set<String> set = new HashSet<>();
//        if (methods != null) {
//            for (Method f : methods) {
//                String modifier = Modifier.toString(f.getModifiers());
//                if (modifier.startsWith("protected")) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//    public final static String[] getPrivateMethod(String className) {
//        Class classz = loadClass(className);
//        Method[] methods = classz.getDeclaredMethods();
//        Set<String> set = new HashSet<>();
//        if (methods != null) {
//            for (Method f : methods) {
//                String modifier = Modifier.toString(f.getModifiers());
//                if (modifier.startsWith("private")) {
//                    set.add(f.getName());
//                }
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//    public final static String[] getMethod(String className, boolean extendsMethod) {
//        Class classz = loadClass(className);
//        Method[] methods;
//        if (extendsMethod) {
//            methods = classz.getMethods();
//        } else {
//            methods = classz.getDeclaredMethods();
//        }
//        Set<String> set = new HashSet<>();
//        if (methods != null) {
//            for (Method f : methods) {
//                set.add(f.getName());
//            }
//        }
//        return set.toArray(new String[set.size()]);
//    }
//
//
//
//    public final static void setter(Object obj, String att, Object value, Class<?> type)
//            throws InvocationTargetException, IllegalAccessException {
//        try {
//            String name = att.substring(0, 1).toUpperCase() + att.substring(1);
//            Method met = obj.getClass().getMethod("set" + name, type);
//            met.invoke(obj, value);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * 获取指定目录下所有的类名
//     *
//     * @param classPath class文件路径
//     * @param jarPath jar文件路径
//     */
////    public final static List<String> getClassName(String classPath, String jarPath) {
////        List<String> fileNames = new ArrayList<>();
////        List<File> jarList = FileHelper.listFileSuffix(new File(jarPath), "jar");
////        for(File file:jarList){
////            fileNames.addAll(getClassNameByJar(file.getAbsolutePath()));
////        }
////        //fileNames.addAll(getClassNameByFile(classPath,true));
////
////        return fileNames;
////    }
//
//
//    public final static List<String> getClassNameByFile(String filePath, boolean childPackage) {
//        List<String> myClassName = new ArrayList<>();
//        List<File> files = FileUtil.listFile(filePath, childPackage);
//        for (File file : files) {
//            if (file.getName().endsWith(".class")) {
//                String childFilePath = file.getPath();
//                int index = filePath.replaceAll("\\\\", ".").length();
//                childFilePath = childFilePath.replaceAll("\\\\", ".").substring(index+1, childFilePath.length());
//                myClassName.add(childFilePath);
//            }
//        }
//        return myClassName;
//    }
//
//
//    public final static List<String> getClassNameByJar(String jarPath) {
//        List<String> myClassName = new ArrayList<>();
//        try (JarFile jarFile = new JarFile(jarPath)) {
//            Enumeration<JarEntry> entrys = jarFile.entries();
//            while (entrys.hasMoreElements()) {
//                JarEntry jarEntry = entrys.nextElement();
//                String entryName = jarEntry.getName();
//                if (entryName.endsWith(".class")) {
//                    entryName = entryName.replace("/", ".");
//                    myClassName.add(entryName);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return myClassName;
//    }
//
//
//
//
//
//    public final static List<String> getResourceNameByJar(String jarPath) {
//        List<String> resource = new ArrayList<>();
//        try (JarFile jarFile = new JarFile(jarPath)) {
//            Enumeration<JarEntry> entrys = jarFile.entries();
//            while (entrys.hasMoreElements()) {
//                JarEntry jarEntry = entrys.nextElement();
//                String entryName = jarEntry.getName();
//                if (!entryName.endsWith(".class") && !entryName.endsWith("/")) {
//                    resource.add(SysHepler.commandPath(jarPath) + "!" + entryName);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return resource;
//    }
//
//
//    public final static List<String> getResourceNameByJar(String jarPath, String suffix) {
//        List<String> resource = new ArrayList<>();
//        try (JarFile jarFile = new JarFile(jarPath)) {
//            Enumeration<JarEntry> entrys = jarFile.entries();
//            while (entrys.hasMoreElements()) {
//                JarEntry jarEntry = entrys.nextElement();
//                String entryName = jarEntry.getName();
//                if (entryName.endsWith(suffix)) {
//                    resource.add(SysHepler.commandPath(jarPath) + "!" + entryName);
//                }
//            }
//        } catch (IOException e) {
//            log.error(ExceptionUtil.stackTraceToString(e, "com.opslab.util"));
//        }
//        return resource;
//    }
//
//
//    public static List<Class> getLoaderClass(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
//        Class cla = classLoader.getClass();
//        while (cla != ClassLoader.class)
//            cla = cla.getSuperclass();
//        Field field = cla.getDeclaredField("classes");
//        field.setAccessible(true);
//        Vector v = (Vector) field.get(classLoader);
//        List<Class> result = new ArrayList<>();
//        for (int i = 0; i < v.size(); i++) {
//            result.add((Class)v.get(i));
//        }
//        return result;
//    }
//
//
//    public final static String getSuperClass(String className) {
//        Class classz = loadClass(className);
//        Class superclass = classz.getSuperclass();
//        return superclass.getName();
//    }
//
//
//    public final static String[] getSuperClassChian(String className) {
//        Class classz = loadClass(className);
//        List<String> list = new ArrayList<>();
//        Class superclass = classz.getSuperclass();
//        String superName = superclass.getName();
//        if (!"java.lang.Object".equals(superName)) {
//            list.add(superName);
//            list.addAll(Arrays.asList(getSuperClassChian(superName)));
//        } else {
//            list.add(superName);
//        }
//        return list.toArray(new String[list.size()]);
//    }
//
//
//    public final static String[] getInterfaces(String className, boolean extendsInterfaces) {
//        Class classz = loadClass(className);
//        List<String> list = new ArrayList<>();
//        Class[] interfaces = classz.getInterfaces();
//        if (interfaces != null) {
//            for (Class inter : interfaces) {
//                list.add(inter.getName());
//            }
//        }
//        if (extendsInterfaces) {
//            String[] superClass = getSuperClassChian(className);
//            for (String c : superClass) {
//                list.addAll(Arrays.asList(getInterfaces(c, false)));
//            }
//        }
//        return list.toArray(new String[list.size()]);
//    }
//
//    /**
//     * 获取资源文件
//     * @param resourceLocation
//     * @return
//     * @throws FileNotFoundException
//     */
//    public static File getFile(String resourceLocation) throws FileNotFoundException {
//        AssertUtil.notNull(resourceLocation,"Resource location must not be null");
//        if (resourceLocation.startsWith("classpath:")) {
//            String path = resourceLocation.substring("classpath:".length());
//            String description = "class path resource [" + path + "]";
//            ClassLoader cl = ClassLoaderUtil.getContextClassLoader();
//            URL url = cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path);
//            if (url == null) {
//                throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not exist");
//            } else {
//                return getFile(url, description);
//            }
//        } else {
//            try {
//                return getFile(new URL(resourceLocation));
//            } catch (MalformedURLException var5) {
//                return new File(resourceLocation);
//            }
//        }
//    }
//
//    public static File getFile(URL resourceUrl) throws FileNotFoundException {
//        return getFile(resourceUrl, "URL");
//    }
//
//    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
//        AssertUtil.notNull(resourceUrl, "Resource URL must not be null");
//        if (!"file".equals(resourceUrl.getProtocol())) {
//            throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUrl);
//        } else {
//            try {
//                return new File(toURI(resourceUrl).getSchemeSpecificPart());
//            } catch (URISyntaxException var3) {
//                return new File(resourceUrl.getFile());
//            }
//        }
//    }
//
//
//    public static URI toURI(URL url) throws URISyntaxException {
//        return toURI(url.toString());
//    }
//
//    public static URI toURI(String location) throws URISyntaxException {
//        return new URI(StringHelper.replace(location, " ", "%20"));
//    }
//}
