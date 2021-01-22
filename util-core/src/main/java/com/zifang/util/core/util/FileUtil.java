package com.zifang.util.core.util;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class FileUtil {

    /**
     * 读取文件，可以读到jar内的文件
     * */
    public static String readFile(String fileName) {
        InputStream fin = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        StringBuffer buffer = null;
        try {
            buffer = new StringBuffer();
            while((strTmp = buffReader.readLine())!=null){
                buffer.append(strTmp).append("\n");
            }
            buffReader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 读取文件的内容
     * 读取指定文件的内容
     *
     * @param path 为要读取文件的绝对路径
     * @return 以行读取文件后的内容。
     */
    public static final String getFileContent(String path) {
        String filecontent = "";
        if(!path.startsWith("/")){
            path = FileUtil.class.getResource("/").getPath()+path;
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

    public static final void gennerateFile(String filePath,String fileContent){
        File file = new File(filePath);
        if(file.exists()){
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
        if(folderOld.endsWith("/")){
            return folderOld;
        }else{
            return folderOld+"/";
        }
    }

    public static String decideTempUsableFolder(){
        return decideTmpFolder()+"use_"+System.currentTimeMillis()+"/";
    }

    public static void mkdir(String workFolder) {
        File file = new File(workFolder);
        if(file.exists()){
            file.delete();
        } else {
            file.mkdir();
        }
    }

    /**
     * 清空这个文件夹下所有文件
     *
     * 如果这个文件夹不存在则创造
     * */
    public static boolean cleanFolder(String fileFolder){


        File file = new File(fileFolder);

        if(!file.exists()){
            return false;
        }

        if(!file.isDirectory()){
            throw  new RuntimeException("传入参数不为文件夹");
        }

        if(file.exists()){
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(File::delete);
        } else {
            file.mkdirs();
        }
        return true;
    }

    /**
     * 检测String代表的文件是否存在
     * */
    public static boolean isFileExist(String filePath){
        return new File(filePath).exists();
    }


    /**
     * 递归删除一个文件夹
     * */
    public static void deleteDir(File file) {
        if(file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if(files == null) {
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
    public  static final byte[] stream2Byte(InputStream is) throws IOException {
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
    public  static final byte[] inputStream2Byte(InputStream inStream) throws Exception {
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
     * @return InputStream
     * @throws Exception
     */
    public  static final InputStream byte2InputStream(byte[] b) throws Exception {
        return new ByteArrayInputStream(b);
    }

    /**
     * 将流另存为文件
     */
    public  static final void streamSaveAsFile(InputStream is, File outfile) {
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
}
