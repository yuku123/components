package com.zifang.util.core.io;

import com.zifang.util.core.lang.StringUtil;

import java.io.*;

/**
 * @author zifang
 */
public class IOUtil {

    public static final int EOF = -1;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static String read(InputStream in, String charsetName) throws IOException {
        FastByteArrayOutputStream out = read(in);
        return StringUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }


    /**
     * 从流中读取内容，读到输出流中，读取完毕后并不关闭流
     *
     * @param in 输入流
     * @return 输出流
     */
    public static FastByteArrayOutputStream read(InputStream in) throws IOException {
        return read(in, true);
    }

    /**
     * 从流中读取内容，读到输出流中，读取完毕后并不关闭流
     */
    public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IOException {
        final FastByteArrayOutputStream out;
        if (in instanceof FileInputStream) {
            // 文件流的长度是可预见的，此时直接读取效率更高
            try {
                out = new FastByteArrayOutputStream(in.available());
            } catch (IOException e) {
                throw e;
            }
        } else {
            out = new FastByteArrayOutputStream();
        }
        try {
            copy(in, out);
        } finally {
            if (isClose) {
                close(in);
            }
        }
        return out;
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//
//    public static byte[] read(InputStream inputStream) throws IOException {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int num = inputStream.read(buffer);
//            while (num != -1) {
//                baos.write(buffer, 0, num);
//                num = inputStream.read(buffer);
//            }
//            baos.flush();
//            return baos.toByteArray();
//        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//        }
//    }


    public static int copy(InputStream input, OutputStream output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
