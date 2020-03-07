package com.zifang.util.core.io;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipTest {
    /**
     * 压缩
     */
    public static void zipFile(String srcFilePath, String destZipFilePath,
                               String fileName) throws Exception {
        File file = new File(destZipFilePath);
        int leng = 0;
        byte[] b = new byte[1024];
        // 压缩
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));

        // 被压缩的文件
        FileInputStream fis = new FileInputStream(srcFilePath);
        // 在压缩包中的路径
        ZipEntry z1 = new ZipEntry(fileName);
        zos.putNextEntry(z1);

        while ((leng = fis.read(b)) != -1) {
            zos.write(b, 0, leng);
        }
        zos.close();
        fis.close();
    }

    public static void unZip(String fileName, String unZipDir) throws Exception {
        // 先判断目标文件夹是否存在，如果不存在则新建，如果父目录不存在也新建
        File f = new File(unZipDir);
        if (!f.exists()) {
            f.mkdirs();
        }
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        ZipEntry entry;
        ZipFile zipfile = new ZipFile(fileName);
        Enumeration e = zipfile.entries();
        while (e.hasMoreElements()) {
            entry = (ZipEntry) e.nextElement();
            // System.out.println("Extracting: " + entry);
            is = new BufferedInputStream(zipfile.getInputStream(entry));
            int count;
            byte[] data = new byte[1024];
            FileOutputStream fos = new FileOutputStream(unZipDir + "/"
                    + entry.getName());
            // System.out.println("entry.getName(): " + entry.getName());
            dest = new BufferedOutputStream(fos, 1024);
            while ((count = is.read(data, 0, 1024)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
            is.close();
        }
    }

    public static void main(String[] args) {
        ZipFile zipFile;
        {
            File file2 = new File(".");
            System.out.println(file2.getAbsolutePath());
            try {
                zipFile("file.doc", "file.zip", "file.doc");
                unZip("file.zip", "d:/test/");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
