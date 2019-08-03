package com.zifang.util.core.ioc;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PackageScanner {
    public PackageScanner() {}

    public abstract void dealClass(Class<?> klass);

    //扫描一般的包。
    private void scanPackage(String packageName,File currentfile) {
        File[] filelist = currentfile.listFiles(new FileFilter() {
            //FileFilter是文件过滤器,源代码只写了一个accapt的抽象方法。
            @Override
            public boolean accept(File pathName) {
                if(pathName.isDirectory()) {    //判断是否是目录
                    return true;
                }
                return pathName.getName().endsWith(".class");
            }
        });

        for(File file:filelist) {
            if(file.isDirectory()) {
                scanPackage(packageName + "." + file.getName(),file);
            }else {
                String fileName = file.getName().replace(".class", "");
                String className = packageName + "." + fileName;
                try {
                    Class<?> klass = Class.forName(className);//取出所有的类
                    if(klass.isAnnotation() //不扫描注解类、枚举类、接口和八大基本类型。
                            ||klass.isEnum()
                            ||klass.isInterface()
                            ||klass.isPrimitive()) {
                        continue;
                    }
                    dealClass(klass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //扫描jar包方法。
    private void scanPackage(URL url) throws IOException {
        JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
        JarFile jarfile = urlConnection.getJarFile();
        Enumeration<JarEntry> jarEntries = jarfile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();
            if(!jarName.endsWith(".class")) {
                continue;
            }
            String className = jarName.replace(".class", "").replaceAll("/", ".");

            try {
                Class<?> klass = Class.forName(className);
                if (klass.isAnnotation()
                        || klass.isInterface()
                        || klass.isEnum()
                        || klass.isPrimitive()) {
                    continue;
                }
                dealClass(klass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //用类名扫描
    public void packageScan(Class<?> klass) {
        packageScan(klass.getPackage().getName());
    }

    //用包名进行扫描
    public void packageScan(String packageName) {
        String packOpperPath = packageName.replace(".","/");

        //线程上下文类加载器得到当前的classpath的绝对路径.（动态加载资源）
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            //(Thread.currentThread().getContextClassLoader().getResource(""))
            //(来得到当前的classpath的绝对路径的URI表示法。)
            Enumeration<URL> resources = classloader.getResources(packOpperPath);
            while(resources.hasMoreElements()) {
                //先获得本类的所在位置
                URL url = resources.nextElement();

                //url.getProtocol()是获取URL的HTTP协议。
                if(url.getProtocol().equals("jar")) {
                    //判断是不是jar包
                    scanPackage(url);
                }else {
                    //此方法不会自动将链接中的非法字符转义。
                    //而在File转化成URI的时候，会将链接中的特殊字符如#或!等字符进行编码。
                    File file = new File(url.toURI());
                    if(!file.exists()) {
                        continue;
                    }
                    scanPackage(packageName,file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PackageScanner() {

            @Override
            public void dealClass(Class<?> klass) {
                System.out.println(klass.getName());

            }
        }.packageScan("com.zifang.util.core.function");

    }
}