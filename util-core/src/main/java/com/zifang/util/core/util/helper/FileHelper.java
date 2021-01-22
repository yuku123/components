//package com.zifang.util.core.util.helper;
//
//import lombok.extern.slf4j.Slf4j;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 一些操作文件的便捷方法
// */
//@Slf4j
//public class FileHelper {
//
//
//    /**
//     * 处理并检索文件
//     *
//     * @param path
//     * @param handler
//     * @return
//     */
//    public static List<File> listFileWithHandler(File path, ObjectProcess<File, File> handler) {
//        List<File> list = new ArrayList<>();
//        if (path != null && path.exists() && path.isDirectory()) {
//            File[] files = path.listFiles();
//            if (files == null || files.length == 0) {
//                return list;
//            }
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    list.addAll(listFileWithHandler(file, handler));
//                } else {
//                    File ff = handler.process(file);
//                    if (ff != null) {
//                        list.add(ff);
//                    }
//                }
//            }
//        }
//        return list;
//    }
//
//
//    ///////////////////////////////////////////////////////////////////////
//    //其他杂项方法
//
//
//
//    /**
//     * 文件压缩支持文件和文件夹
//     *
//     * @throws Exception
//     */
//    public static boolean zipDeCompress(File file, String zipFile) {
//        try {
//            ZIPUtil.deCompress(file, zipFile);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 文件压缩
//     *
//     * @param zipFile
//     * @param path
//     * @return
//     */
//    public static boolean zipUnCompress(File zipFile, String path) {
//        try {
//            ZIPUtil.unCompress(zipFile, path);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 获取文件内容，内容经过BASE64编码
//     *
//     * @param URL
//     * @return
//     * @throws IOException
//     */
//    public static String getSource(String URL) {
//        try {
//            File file = new File(URL);
//            FileInputStream is = new FileInputStream(file);
//            byte[] bytes = new byte[(int) file.length()];
//            int len = 0;
//            while ((len = is.read(bytes)) != -1) {
//                is.read(bytes);
//            }
//            is.close();
//            return Base64.encodeToString(bytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 将BASE64的字符串恢复成文件
//     *
//     * @param filename
//     * @param content
//     * @return
//     */
//    public static boolean sourceFile(String filename, String content) {
//        File file = new File(filename);
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            byte[] bytes = Base64.decode(content);
//            fos.write(bytes);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    /**
//     * 将文件转换为base64的字符串
//     *
//     * @param URL
//     * @param size 每个字符串的长度（字节）
//     * @return
//     * @throws IOException
//     */
//    public static List<String> getSource(String URL, int size) {
//        List<String> list = new ArrayList<>();
//        try (InputStream fis = new FileInputStream(URL)) {
//            byte[] buf = new byte[size];
//            int len = 0;
//            while ((len = fis.read(buf)) != -1) {
//                if (len < size) {
//                    byte[] bs = new byte[len];
//                    System.arraycopy(buf, 0, bs, 0, len);
//                    String str = (Base64.encodeToString(bs));
//                    list.add(str);
//                } else {
//                    String str = (Base64.encodeToString(buf));
//                    list.add(str);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    /**
//     * 将BASE64的字符串恢复成文件
//     *
//     * @param filename
//     * @param contents
//     * @return
//     */
//    public static boolean sourceFile(String filename, List<String> contents) {
//        File file = new File(filename);
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            for (String str : contents) {
//                byte[] bytes = Base64.decode(str);
//                fos.write(bytes);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//}
