package com.zifang.util.core.util;

import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * class 相关的工具类
 * */
public class ClassUtil {

    /**
     * 将class导出到文件目录上
     * */
    public static void saveClassFile(Class clazz) {
        //生成class的字节数组，此处生成的class与proxy.newProxyInstance中生成的class除了代理类的名字不同，其它内容完全一致
        byte[] classFile = ProxyGenerator.generateProxyClass(clazz.getSimpleName(), clazz.getInterfaces());
        String path = clazz.getResource(".").getPath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path + clazz.getSimpleName() + ".class");
            fos.write(classFile);//保存到磁盘
            fos.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void aa(Class clazz) {
        byte[] a = ProxyGenerator.generateProxyClass("",new Class[]{clazz});
        FileOutputStream  os = null;
        try {
            os = new FileOutputStream("aa.class");
            os.write(a);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
