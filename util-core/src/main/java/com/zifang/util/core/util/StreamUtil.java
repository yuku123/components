package com.zifang.util.core.util;

import java.io.*;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * 将Iterator类型的数据转化为流操作
 * */
public class StreamUtil {


    /**
     * 转换 iterator 为串行流
     */
    public static <T> Stream<T> streamOf(final Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
    }

    /**
     * Converts interable to a non-parallel stream.
     */
    public static <T> Stream<T> streamOf(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Wraps an iterator as a stream.
     */
    public static <T> Stream<T> parallelStreamOf(final Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), true);
    }

    /**
     * Wraps an iterator as a stream.
     */
    public static <T> Stream<T> parallelStreamOf(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), true);
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
