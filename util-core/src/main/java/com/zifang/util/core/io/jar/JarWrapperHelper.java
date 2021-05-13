package com.zifang.util.core.io.jar;

//import org.apache.commons.io.IOUtils;
//import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 包裹一个JarFile并对外提供针对jar本身的一系列业务级别操作
 */
public class JarWrapperHelper {

    private JarFile jarFile = null;

    public JarWrapperHelper(String jarFilePath) {
        try {
            jarFile = new JarFile(jarFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JarWrapperHelper(File jarFilePath) {
        try {
            jarFile = new JarFile(jarFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 遍历这个jar包，把所有的entry抠出来放到这里
     */
    public List<JarEntry> getJarEntry() {
        List<JarEntry> jarEntries = new ArrayList<>();
        Enumeration<JarEntry> entrys = jarFile.entries();
        while (entrys.hasMoreElements()) {
            jarEntries.add(entrys.nextElement());
        }
        return jarEntries;
    }

//    /**
//     * 通过文件名进行查找 只会从右侧进行匹配
//     * */
//    public String findPathByFileName(String fileName) {
//        Enumeration<JarEntry> entrys = jarFile.entries();
//        while (entrys.hasMoreElements()) {
//            JarEntry jar = entrys.nextElement();
//            String name = jar.getName();
//            if (name.endsWith(fileName)) {
//                return getContent(name);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 使用文件路径名获得它的byte数组
//     * */
//    public String getContent(String pathInJar) {
//        try {
//            JarEntry entry = jarFile.getJarEntry(pathInJar);
//            if (entry != null) {
//
//                StringWriter writer = new StringWriter();
//
//                FileCopyUtils.copy(new InputStreamReader(jarFile.getInputStream(entry)), writer);
//
//                return IOUtils.toString(new StringReader(writer.toString()));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                jarFile.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    /**
     * 使用文件路径获得字节数组
     */
    public byte[] getByteArrayByPathInJar(String pathInJar) {

        byte[] bytes = new byte[0];
        try {
            JarEntry entry = jarFile.getJarEntry(pathInJar);
            if (entry != null) {
                bytes = toByteArray(jarFile.getInputStream(entry));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 输入流转换为字节数组
     */
    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 将jar里面的内容转换为
     * <p>
     * 路径 -> byte数组
     */
    public Map<String, byte[]> getFileByteArray(Predicate<JarEntry> predicate) {

        Map<String, byte[]> map = new LinkedHashMap<>();

        List<JarEntry> jarEntries = getJarEntry()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());


        for (JarEntry jarEntry : jarEntries) {
            String filePath = jarEntry.getName();
            byte[] bytes = getByteArrayByPathInJar(filePath);
            map.put(filePath, bytes);
        }

        return map;
    }
}
