package com.zifang.util.core.demo.temp.tool.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * ����־û�������
 * 
 * @author linkwechat linkwechat@foxmail.com
 * @version 1.0
 */
public class PersistenceUtils {


    /**
     * ��ȡ�־û�����
     * 
     * @param filePath
     *            �ļ�·��
     * @return Object
     */
    public static Object readObject(String filePath) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            obj = ois.readObject();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Throwable t) {
                	t.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Throwable t) {
                	t.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * д��־û�����
     * 
     * @param obj
     *            �־û�����
     * @param filePath
     *            �ļ�·��
     * @return boolean
     */
    public static boolean writeObject(Object obj, String filePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs()) {
                        return false;
                    }
                }
                if (!file.createNewFile()) {
                    return false;
                }
            }
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
        } catch (Throwable t) {
        	t.printStackTrace();
            return false;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Throwable t) {
                	t.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Throwable t) {
                	t.printStackTrace();
                }
            }
        }
        return true;
    }
}