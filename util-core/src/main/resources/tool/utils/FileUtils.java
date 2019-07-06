package com.zifang.demo.temp.tool.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件工具类
 * 
 * @author linkwechat linkwechat@foxmail.com
 * @version 1.0
 */
public class FileUtils {


    /**
     * 默认字符集
     */
    private static String defaultCharset = "UTF-8";

    public static String getDefaultCharset() {
        return defaultCharset;
    }

    public static void setDefaultCharset(String defaultCharset) {
        FileUtils.defaultCharset = defaultCharset;
    }

    /**
     * 读取文件内容（默认为UTF-8文件编码）
     * 
     * @param filePath
     *            文件路径
     * @return List&lt;String&gt;
     */
    public static List<String> readLines(String filePath) {
        return readLines(filePath, defaultCharset);
    }

    /**
     * 读取文件内容
     * 
     * @param filePath
     *            文件路径
     * @param charset
     *            文件编码
     * @return List&lt;String&gt;
     */
    public static List<String> readLines(String filePath, String charset) {
        List<String> lineList = new ArrayList<String>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis, charset);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }
        } catch (Throwable t) {
        	t.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
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
        return lineList;
    }

    /**
     * 写入文件内容（默认为UTF-8文件编码，覆盖已有文件）
     * 
     * @param filePath
     *            文件路径
     * @param text
     *            文件内容
     * @return boolean
     */
    public static boolean writeFile(String filePath, String text) {
        return writeFile(filePath, text, 0, text.length(), defaultCharset, false);
    }

    /**
     * 写入文件内容（覆盖已有文件）
     * 
     * @param filePath
     *            文件路径
     * @param text
     *            文件内容
     * @param charset
     *            文件编码
     * @return boolean
     */
    public static boolean writeFile(String filePath, String text, String charset) {
        return writeFile(filePath, text, 0, text.length(), charset, false);
    }

    /**
     * 写入文件内容（默认为UTF-8文件编码）
     * 
     * @param filePath
     *            文件路径
     * @param text
     *            文件内容
     * @param append
     *            是否追加
     * @return boolean
     */
    public static boolean writeFile(String filePath, String text, boolean append) {
        return writeFile(filePath, text, 0, text.length(), defaultCharset, append);
    }

    /**
     * 写入文件内容
     * 
     * @param filePath
     *            文件路径
     * @param text
     *            文件内容
     * @param charset
     *            文件编码
     * @param append
     *            是否追加
     * @return boolean
     */
    public static boolean writeFile(String filePath, String text, String charset, boolean append) {
        return writeFile(filePath, text, 0, text.length(), charset, append);
    }

    /**
     * 写入文件内容
     * 
     * @param filePath
     *            文件路径
     * @param text
     *            文件内容
     * @param offset
     *            起始位置
     * @param length
     *            写入长度
     * @param charset
     *            文件编码
     * @param append
     *            是否追加
     * @return boolean
     */
    public static boolean writeFile(String filePath, String text, int offset, int length, String charset,
            boolean append) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
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
            fos = new FileOutputStream(file, append);
            osw = new OutputStreamWriter(fos, charset);
            bw = new BufferedWriter(osw);
            bw.write(text, offset, length);
            bw.flush();
        } catch (Throwable t) {
        	t.printStackTrace();
            return false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (Throwable t) {
                	t.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
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