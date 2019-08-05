package com.zifang.util.core.ioc;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PacketScan {
    public PacketScan() {
    }

    //这是一个抽象方法，由外面实现
    public abstract void dealClass(Class<?> klass);

    private void packetScanner(File curFile, String packName) {
        //如果不是目录就结束方法的调用
        if (!curFile.isDirectory()) {
            return;
        }
        //该方法返回一个抽象路径名数组，表示由该抽象路径名表示的目录中的文件
        File[] files = curFile.listFiles();
        for(File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "");
                //去掉“.class”后就是文件名，路径名加文件名就是类名
                String className = packName + "." + fileName;
                try {
                    //根据类名称得到类类型
                    Class<?> klass = Class.forName(className);
                    dealClass(klass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(file.isDirectory()) {
                //如果该文件是目录就再一次调用此方法，将路径名加文件名（下一次路径）传过去
                //一直这样递归调用，直到是文件为止
                packetScanner(file, packName + "." + file.getName());
            }
        }
    }

    private void scanJarPacket(URL url) throws IOException {
        /**
         * 由API查得：
         * 该方法返回一个URLConnection实例，表示与URL引用的远程对象的URL
         * 如果对于URL的协议（如HTTP或JAR）,则存在一个属于以下软件包或其子包之一的公共专用URLConnection子类：
         * java.long, java.io, java.util, java.net,返回的连接将是该子类
         */
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        /**
         * 由API查得：
         * 返回此连接的JAR文件
         * 如果连接是与JAR文件的条目的连接，则返回JAR文件对象
         */
        JarFile jarFile = jarURLConnection.getJarFile();
        /**
         * 由API查得：
         * 返回zip文件条目的枚举
         */
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();
            //如果它是一个目录或者不是“.class”文件，就跳过
            if (jarEntry.isDirectory() || !jarName.endsWith(".class")) {
                continue;
            }
            String className = jarName.replace(".class", "").replaceAll("/", ".");
            try {
                Class<?> klass = Class.forName(className);
                //如果这个类是注解或者枚举或者接口或者八大基本类型就跳过
                if (klass.isAnnotation()
                        || klass.isEnum()
                        || klass.isInterface()
                        || klass.isPrimitive()) {
                    continue;
                }
                //调用抽象类
                dealClass(klass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //对包扫描方法重载，可以扫描多个包
    public void scanPacket(String[] packetNamees) {
        for(String packetName : packetNamees) {
            scanPacket(packetName);
        }
    }
    //对包扫描方法重载，提供多个类名，可以扫描该类所在的包
    public void scanPacket(Class<?>[] klasses) {
        for(Class<?> klass : klasses) {
            scanPacket(klass);
        }
    }

    public void scanPacket(Class<?> klass) {
        String path = klass.getPackage().getName();
        scanPacket(path);
    }

    public void scanPacket(String packetName) {
        /**
         * 由API查得：
         * 在windows下是\，但在编程语言\是转义字符的起时字符，所以路径中的\通常需要使用\\
         * 如果是/就不需要转义了
         * “\”一般表示本地目录的，比如电脑里的目录。
         * “/”主要表示远程电脑或者网络上的。
         * 这里将Java里的传过来的包名里的“.”转换为“/”，供下面的类加载器调用找到类加载器的资源
         */
        String packetPath = packetName.replace(".", "/");
        /**
         * 由API查得：
         * 返回此Thread的上下文ClassLoader，上下文ClassLoader是由线程的创建者提供，
         * 以便在加载类和资源时在此线程中运行的代码使用。如果不是set，默认是父线程的ClassLoader上下文。
         * 原始线程的上下文ClassLoader通常设置为用于加载应用程序的类加载器。
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            /**
             * 由API查得：
             * 找到具有给定名称的资源，资源可以通过独立于代码位置的方式由类代码访问的一些资源（图像、音频、文本等）
             * 资源的名称是标识资源的“/”分割路径名。
             * 此方法首先将搜索父类加载器的资源；如果父级是null，则会搜索内置到虚拟机的类加载器的路径
             */
            Enumeration<URL> resources = classLoader.getResources(packetPath);
            while(resources.hasMoreElements()) {
                //这里得到的是该类的包路径
                URL url = resources.nextElement();
                //url.getProtocol()获取此url协议的名称,如果是文件输出file;如果是jar包，输出jar
                if (url.getProtocol().equals("jar")) {
                    //如果是“jar”包就调用“jar”包扫描的方法
                    scanJarPacket(url);
                } else {
                    //这里实例化一个文件类型的对象，需要uri类型的参数，因此需要将url转换为uri
                    //这里得到的file对象是windows系统下的包路径
                    File file = new File(url.toURI());
                    //如果这个文件不存在，就继续查找
                    if (!file.exists()) {
                        continue;
                    }
                    //如果是普通包就调用普通包扫描的方法
                    packetScanner(file, packetName);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}