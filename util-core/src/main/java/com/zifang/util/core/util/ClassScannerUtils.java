package com.zifang.util.core.util;



import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 提供对 包名下的类的扫描工具
 *
 * */
public class ClassScannerUtils {

    private static String CLASS_SUFFIX = ".class";
    private static String JAR = "jar";
    private static String FILE = "file";



    private static String defaultClassPath = ClassScannerUtils.class.getResource("/").getPath();

    public static Set<Class<?>> searchClasses(String packageName){
        return searchClasses(packageName,null);
    }

    public static Set<Class<?>> searchClasses(String packageName, Predicate<Class<?>> predicate){
        return search(packageName,predicate);
    }

    private static Set<Class<?>> search(String packageName, Predicate<Class<?>> predicate) {

        Set<Class<?>> classes = new HashSet<>();

        try {
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();//jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit
                String protocol = url.getProtocol();//jar
                if (JAR.equalsIgnoreCase(protocol)) {
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    if (connection != null) {
                        JarFile jarFile = connection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                if (jarEntryName.contains(CLASS_SUFFIX) && jarEntryName.replaceAll("/", ".").startsWith(packageName)) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                    Class cls = Class.forName(className);
                                    if(predicate == null || predicate.test(cls)){
                                        classes.add(cls);
                                    }
                                }
                            }
                        }
                    }
                }else if(FILE.equalsIgnoreCase(protocol)){
                    Set<Class<?>> set = searchFromFile(packageName,predicate);
                    classes.addAll(set);
                }
            }
        }catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
        return classes;
    }


    private static class ClassSearcher{
        private Set<Class<?>> classPaths = new HashSet<>();

        private Set<Class<?>> doPath(File file,String packageName, Predicate<Class<?>> predicate,boolean flag) {

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if(!flag){
                    packageName = packageName+"."+file.getName();
                }

                for (File f1 : files) {
                    doPath(f1,packageName,predicate,false);
                }
            } else {
                if (file.getName().endsWith(CLASS_SUFFIX)) {
                    try {
                        Class<?> clazz = Class.forName(packageName + "."+ file.getName().substring(0,file.getName().lastIndexOf(".")));
                        if(predicate==null||predicate.test(clazz)){
                            classPaths.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            return classPaths;
        }
    }

    public static Set<Class<?>> searchFromFile(String packageName, Predicate<Class<?>> predicate) {
        //�ȰѰ���ת��Ϊ·��,���ȵõ���Ŀ��classpath
        String classpath = defaultClassPath;
        //Ȼ������ǵİ���basPackת��Ϊ·����
        String basePackPath = packageName.replace(".", File.separator);
        String searchPath = classpath + basePackPath;
        return new ClassSearcher().doPath(new File(searchPath),packageName, predicate,true);
    }

    public static void main(String[] args) throws IOException {

        Set<Class<?>> set = searchClasses("com.google");
        for (Class<?> aClass : set) {
            System.out.println(aClass.getName());
        }

        Set<Class<?>> set1 = searchClasses("com.zifang");
        for (Class<?> aClass : set1) {
            System.out.println(aClass.getName());
        }
    }
}
