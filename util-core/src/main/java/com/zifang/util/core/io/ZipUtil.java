package com.zifang.util.core.io;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩、解压工具类。文件压缩格式为zip
 *
 * @author zifang
 */
public class ZipUtil {

    /**
     * 文件后缀名
     */
    private static final String ZIP_FILE_SUFFIX = ".zip";

    /**
     * 压缩文件
     *
     * @param resourcePath 源文件
     * @param targetPath   目的文件,保存文件路径
     */
    public static void zipFile(String resourcePath, String targetPath) {
        File resourcesFile = new File(resourcePath);
        File targetFile = new File(targetPath);

        //目的文件不存在，则新建
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        //文件名
        String targetName = resourcesFile.getName() + ZIP_FILE_SUFFIX;

        ZipOutputStream out = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(targetPath + "//" + targetName);
            out = new ZipOutputStream(new BufferedOutputStream(outputStream));

            compressedFile(out, resourcesFile, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compressedFile(ZipOutputStream out, File file, String dir) {
        FileInputStream fis = null;
        try {
            if (file.isDirectory()) {
                // 得到文件列表信息
                File[] files = file.listFiles();
                // 将文件夹添加到下一级打包目录
                out.putNextEntry(new ZipEntry(dir + "/"));

                dir = dir.length() == 0 ? "" : dir + "/";

                for (int i = 0; i < files.length; i++) {
                    compressedFile(out, files[i], dir + files[i].getName());
                }
            } else {
                fis = new FileInputStream(file);
                out.putNextEntry(new ZipEntry(dir));
                // 进行写操作
                int j = 0;
                byte[] buffer = new byte[1024];
                while ((j = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, j);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void zipFolder(String folder, String targetFolder, String zipFileName) {

        File zipFolder = new File(folder);

        if (!zipFolder.isDirectory()) {
            throw new RuntimeException("folder：" + folder + " is not a Folder");
        }

        if (!zipFolder.exists()) {
            throw new RuntimeException("folder：" + folder + " is not Exist");
        }
    }


    /**
     * 压缩文件
     */
    public static void zipFile(String srcFilePath, String destZipFilePath, String fileName) throws Exception {
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
}
