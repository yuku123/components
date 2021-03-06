package com.zifang.util.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author zifang
 */
@Slf4j
public class FileUtil {

    /**
     * 读取文件，可以读到jar内的文件
     */
    public static String readFile(String fileName) {
        InputStream fin = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        StringBuffer buffer = null;
        try {
            buffer = new StringBuffer();
            while ((strTmp = buffReader.readLine()) != null) {
                buffer.append(strTmp).append("\n");
            }
            buffReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 逐行处理
     *
     * @param file     handler file
     * @param encoding file encoding
     */
    public static void handlerWithLine(File file, String encoding, Consumer<String> consumer) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            log.error("handler error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 逐行处理
     *
     * @param file     需要处理的文件
     * @param encoding 文件编码
     * @param result   接受处理结果的集合
     * @param <E>
     */
    public static <E> void processWithLine(File file, String encoding, Collection<E> result, Function<String, ? extends E> mapper) {
        if (result == null) {
            log.info("receive collection is null");
            return;
        }
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                E tmpLine = mapper.apply(line);
                if (tmpLine != null) {
                    result.add(tmpLine);
                }
            }
        } catch (IOException e) {
            log.error("process error:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 创建文件支持多级目录
     *
     * @param path 需要创建的文件
     * @return 是否成功
     */
    public static boolean createFile(String path) {
        if (path != null && path.length() > 0) {
            try {
                File file = new File(path);
                if (!file.getParentFile().exists() && file.getParentFile().mkdirs()) {
                    return file.createNewFile();
                }
            } catch (Exception e) {
                log.error("create file exception :" + path + ",Exception" + e.getMessage());
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 获取文件后缀名
     *
     * @param file file
     * @return file's suffix
     */

    public static String suffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    /**
     * 获取文件的hash
     *
     * @param file     file
     * @param HashTyle MD5,SHA-1,SHA-256
     * @return
     */
    public static String fileHash(File file, String HashTyle) {
        try (InputStream fis = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance(HashTyle);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] md5Bytes = md.digest();
            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            log.error("get filehash error" + e.getMessage());
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 利用字节特征探测文件编码
     *
     * @param file 需要处理的文件
     * @return UTF-8 Unicode UTF-16BE GBK or null
     */
    public static String simpleEncoding(File file) {
        try {
            return "";//@todo
            //return FileImpl.guestFileEncoding(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//
//    /**
//     * 获取文件content-type
//     * @param file
//     * @return
//     */
//    public static String contentType(String file) {
//        String contentType = null;
//        try {
//            contentType = new MimetypesFileTypeMap().getContentType(new File(file));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return contentType;
//    }

    /**
     * 读取文件的内容
     * 读取指定文件的内容
     *
     * @param path 为要读取文件的绝对路径
     * @return 以行读取文件后的内容。
     */
    public static final String getFileContent(String path) {
        String filecontent = "";
        if (!path.startsWith("/")) {
            path = FileUtil.class.getResource("/").getPath() + path;
        }
        try {
            File f = new File(path);
            if (f.exists()) {
                FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr); //建立BufferedReader对象，并实例化为br
                String line = br.readLine(); //从文件读取一行字符串
                //判断读取到的字符串是否不为空
                while (line != null) {
                    filecontent += line + "\n";
                    line = br.readLine(); //从文件中继续读取一行数据
                }
                br.close(); //关闭BufferedReader对象
                fr.close(); //关闭文件
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filecontent;
    }

    public static final void gennerateFile(String filePath, String fileContent) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        } else {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(fileContent);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String decideTmpFolder() {
        String folderOld = System.getProperty("java.io.tmpdir");
        if (folderOld.endsWith("/")) {
            return folderOld;
        } else {
            return folderOld + "/";
        }
    }

    public static String decideTempUsableFolder() {
        return decideTmpFolder() + "use_" + System.currentTimeMillis() + "/";
    }

    public static void mkdir(String workFolder) {
        File file = new File(workFolder);
        if (file.exists()) {
            file.delete();
        } else {
            file.mkdir();
        }
    }

    /**
     * 清空这个文件夹下所有文件
     * <p>
     * 如果这个文件夹不存在则创造
     */
    public static boolean cleanFolder(String fileFolder) {


        File file = new File(fileFolder);

        if (!file.exists()) {
            return false;
        }

        if (!file.isDirectory()) {
            throw new RuntimeException("传入参数不为文件夹");
        }

        if (file.exists()) {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(File::delete);
        } else {
            file.mkdirs();
        }
        return true;
    }

    /**
     * 检测String代表的文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }


    /**
     * 递归删除一个文件夹
     */
    public static void deleteDir(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i]);
                }
                file.delete();
            }
        }
    }

    /**
     * Read an input stream into a string
     */
    public final static String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * Read an input stream into a byte[]
     */
    public static final byte[] stream2Byte(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = is.read(b, 0, b.length)) != -1) {
            baos.write(b, 0, len);
        }
        byte[] buffer = baos.toByteArray();
        return buffer;
    }

    /**
     * InputStream 转为 byte
     */
    public static final byte[] inputStream2Byte(InputStream inStream) throws Exception {
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }

    /**
     * byte 转为 InputStream
     *
     * @return InputStream
     * @throws Exception
     */
    public static final InputStream byte2InputStream(byte[] b) throws Exception {
        return new ByteArrayInputStream(b);
    }

    /**
     * 将流另存为文件
     */
    public static final void streamSaveAsFile(InputStream is, File outfile) {
        try (FileOutputStream fos = new FileOutputStream(outfile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取文件的行数
     *
     * @param file 统计的文件
     * @return 文件行数
     */
    public static int lineCounts(File file) {
        try (LineNumberReader rf = new LineNumberReader(new FileReader(file))) {
            long fileLength = file.length();
            rf.skip(fileLength);
            return rf.getLineNumber();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file 需要出来的文件
     * @return 包含所有行的list
     */
    public static List<String> readLines(File file) {
        List<String> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file     需要处理的文件
     * @param encoding 指定读取文件的编码
     * @return 包含所有行的list
     */
    public static List<String> readLines(File file, String encoding) {
        List<String> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String readContents(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            Long filelength = file.length();
            byte[] filecontent = new byte[filelength.intValue()];
            if (in.read(filecontent) > 0) {
                return new String(filecontent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建文件支持多级目录
     *
     * @param file 需要创建的文件
     * @return 是否成功, 如果存在则返回成功
     */
    public final static boolean createFiles(File file) {
        if (file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            if (!file.exists()) {
                return file.mkdirs();
            }
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 创建文件支持多级目录
     *
     * @param file 需要创建的文件
     * @return 是否成功, 如果存在则返回成功
     * @para isReNew 存在的时候是否重新创建
     */
    public final static boolean createFiles(File file, boolean isReNew) {
        if (file.exists()) {
            if (isReNew) {
                if (file.delete()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
        if (file.isDirectory()) {
            if (!file.exists()) {
                return file.mkdirs();
            }
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    try {
                        return file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 写文件
     *
     * @param file 需要处理的函数
     * @param str  添加的子字符串
     * @return 是否成功
     */
    public static boolean write(File file, String str) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            randomFile.writeBytes(str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写文件
     *
     * @param file     需要处理的文件
     * @param str      添加的字符串
     * @param encoding 指定写入的编码
     * @return 是否成功
     */
    public static boolean write(File file, String str, String encoding) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            randomFile.write((str).getBytes(encoding));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写文件
     *
     * @param file 需要处理的函数
     * @param str  添加的子字符串
     * @return 是否成功
     */
    public static boolean addWrite(File file, String str) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(str);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 写文件
     *
     * @param file     需要处理的文件
     * @param str      添加的字符串
     * @param encoding 指定写入的编码
     * @return 是否成功
     */
    public static boolean addWrite(File file, String str, String encoding) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write((str).getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 复制文件
     *
     * @param resource 源文件
     * @param target   目标文件
     * @return 是否成功
     */
    public static boolean copy(File resource, File target) throws IOException {
        if (resource == null) {
            log.error("copy  resource is null");
            return false;
        }
        if (resource.isFile()) {
            return copyFile(resource, target);
        }
        File[] files = resource.listFiles();
        if (files == null || files.length == 0) {
            return target.mkdirs();
        }
        target.mkdirs();
        for (File file : files) {
            String targetFilePath = file.getAbsolutePath().substring(resource.getAbsolutePath().length());
            File targetFile = new File(target + "/" + targetFilePath);
            if (file.isDirectory()) {
                targetFile.mkdirs();
                copy(file, targetFile);
            } else {
                copyFile(file, targetFile);
            }
        }
        return true;

    }

    /**
     * 复制文件
     * 通过该方式复制文件文件越大速度越是明显
     *
     * @param file       需要处理的文件
     * @param targetFile 目标文件
     * @return 是否成功
     */
    public static boolean copyFile(File file, File targetFile) throws IOException {
        log.debug("copy file resource:{} ,target:{}", file.getAbsolutePath(), targetFile.getAbsolutePath());
        int BUFFER_SIZE = 1024 * 1024;
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        targetFile.createNewFile();
        try (
                FileInputStream fin = new FileInputStream(file);
                FileOutputStream fout = new FileOutputStream(targetFile)
        ) {
            FileChannel in = fin.getChannel();
            FileChannel out = fout.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
            return true;
        } catch (IOException e) {
            throw e;
        }
    }


    ///////////////////////////////////////////////////////////////////////
    // 删除文件的方法

    /**
     * 快速清空一个超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean cleanFile(File file) {
        try (
                FileWriter fw = new FileWriter(file)
        ) {
            fw.write("");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除一个目录
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public static boolean delete(File file) {
        if (file == null)
            return false;

        if (file.isFile())
            return file.delete();

        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return file.delete();
        }
        for (File ff : files) {
            if (file.isDirectory()) {
                delete(ff);
            } else {
                if (ff.length() > 1024 * 1024 * 1024) {
                    cleanFile(ff);
                }
                ff.delete();
            }
        }
        return file.delete();
    }


    ///////////////////////////////////////////////////////////////////////
    // 检索文件的方法

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public static List<File> listFile(File path) {
        List<File> list = new ArrayList<>();
        if (path != null && path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            if (files == null || files.length == 0) {
                return list;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }


    /**
     * 获取指定目录下的特点文件,通过后缀名过滤
     *
     * @param dirPath  需要处理的文件
     * @param postfixs 文件后缀
     * @return 返回文件列表
     */
    public static List<File> listFileSuffix(File dirPath, String postfixs) {
        List<File> list = new ArrayList<>();
        if (dirPath != null && dirPath.exists() && dirPath.isDirectory()) {
            File[] files = dirPath.listFiles();
            if (files == null || files.length == 0) {
                return list;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileSuffix(file, postfixs));
                } else {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(postfixs.toLowerCase())) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在指定的目录下按照文件名查找文件
     *
     * @param dirPath  搜索的目录
     * @param fileName 搜索的文件名
     * @return 返回文件列表
     */
    public static List<File> listFileName(File dirPath, String fileName) {
        List<File> list = new ArrayList<>();
        if (dirPath != null && dirPath.exists() && dirPath.isDirectory()) {
            File[] files = dirPath.listFiles();
            if (files == null || files.length == 0) {
                return list;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileName(file, fileName));
                } else {
                    String Name = file.getName();
                    if (Name.equals(fileName)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在指定的目录下按照文件名查找文件忽略文件带下
     *
     * @param dirPath  搜索的目录
     * @param fileName 搜索的文件名
     * @return 返回文件列表
     */
    public static List<File> listFileNameIgnoreCase(File dirPath, String fileName) {
        List<File> list = new ArrayList<>();
        if (dirPath != null && dirPath.exists() && dirPath.isDirectory()) {
            File[] files = dirPath.listFiles();
            if (files == null || files.length == 0) {
                return list;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileName(file, fileName));
                } else {
                    String Name = file.getName();
                    if (Name.equalsIgnoreCase(fileName)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path   需要处理的文件
     * @param filter 处理文件的filter
     * @return 返回文件列表
     */
    public static List<File> listFileFilter(File path, Predicate<File> filter) {
        List<File> list = new ArrayList<>();
        if (path != null && path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            if (files == null || files.length == 0) {
                return list;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileFilter(file, filter));
                } else {
                    if (filter.test(file)) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }


    /**
     * 罗列指定目录下的文件名符合正则表达式的文件
     *
     * @param dirPath 搜索的目录
     * @param pattern 正则表达式
     * @return 返回文件列表
     */
    public static List<File> listFileNameReg(File dirPath, Pattern pattern) {
        List<File> list = new ArrayList<>();
        if (dirPath != null && dirPath.exists() && dirPath.isDirectory()) {
            File[] files = dirPath.listFiles();
            if (files == null || files.length == 0) {
                return list;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    list.addAll(listFileNameReg(file, pattern));
                } else {
                    if (pattern.matcher(file.getName()).matches()) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }
}
