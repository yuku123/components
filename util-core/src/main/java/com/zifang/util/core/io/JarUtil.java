package com.zifang.util.core.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

public class JarUtil {
    public static void unPack(String from , String target) throws IOException {
        // JAR文件和目标目录
        File jarFile = new File(from);
        File destinationDir = new File(target);

        // 确保目标目录存在
        if (!destinationDir.exists()) {
            destinationDir.mkdir();
        }

        // 解压JAR文件
        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream().forEach(entry -> {
                String name = entry.getName();
                File file = new File(destinationDir, name);
                try {
                    if (entry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        file.getParentFile().mkdirs();
                        try (InputStream is = jar.getInputStream(entry);
                             FileOutputStream fos = new FileOutputStream(file)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = is.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}