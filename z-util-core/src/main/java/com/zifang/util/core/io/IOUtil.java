package com.zifang.util.core.io;

import com.zifang.util.core.lang.StringUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 流与字节之间的搬运。
 * <p>
 * 只管「流 ↔ 字节/字符串」这一件事：读、拷贝、跳过、关闭。
 * 落在文件系统上的读写归 {@code file/}，这里不碰 File 和 Path。
 */
public class IOUtil {

    public static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    // -------------------- 流读取 --------------------

    /**
     * 将输入流读为字符串
     *
     * @param in          输入流
     * @param charsetName 字符编码，null 则使用 UTF-8
     * @return 字符串内容
     * @throws IOException 读取异常
     */
    public static String readString(InputStream in, String charsetName) throws IOException {
        byte[] bytes = readBytes(in);
        Charset charset = StringUtil.isBlank(charsetName)
                ? StandardCharsets.UTF_8 : Charset.forName(charsetName);
        return new String(bytes, charset);
    }

    /**
     * 将输入流读为字符串，默认 UTF-8
     *
     * @param in 输入流
     * @return 字符串内容
     * @throws IOException 读取异常
     */
    public static String readString(InputStream in) throws IOException {
        return readString(in, StandardCharsets.UTF_8.name());
    }

    /**
     * 读取输入流的全部字节，读取完毕后关闭输入流
     *
     * @param in 输入流
     * @return 读到的字节
     * @throws IOException 读取异常
     */
    public static byte[] readBytes(InputStream in) throws IOException {
        return readBytes(in, true);
    }

    /**
     * 读取输入流的全部字节
     *
     * @param in      输入流
     * @param closeIn 读取完毕后是否关闭输入流
     * @return 读到的字节
     * @throws IOException 读取异常
     */
    public static byte[] readBytes(InputStream in, boolean closeIn) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(
                in instanceof FileInputStream ? Math.max(in.available(), DEFAULT_BUFFER_SIZE)
                        : DEFAULT_BUFFER_SIZE);
        try {
            copy(in, out);
        } finally {
            if (closeIn) {
                close(in);
            }
        }
        return out.toByteArray();
    }

    /**
     * 读取Reader所有行
     *
     * @param reader Reader
     * @return 行列表
     * @throws IOException 读取异常
     */
    public static List<String> readLines(Reader reader) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = reader instanceof BufferedReader
                ? (BufferedReader) reader
                : new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 逐行读取流并处理
     *
     * @param in       输入流
     * @param charset  字符编码
     * @param consumer 行处理器
     * @throws IOException 读取异常
     */
    public static void readLines(InputStream in, Charset charset, Consumer<String> consumer) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(in, charset == null ? StandardCharsets.UTF_8 : charset))) {
            String line;
            while ((line = br.readLine()) != null) {
                consumer.accept(line);
            }
        }
    }

    // -------------------- 流复制 --------------------

    /**
     * 将输入流内容复制到输出流
     *
     * @param input  输入流
     * @param output 输出流
     * @return 复制的字节数
     * @throws IOException 读取或写入异常
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 大规模流复制，使用较大缓冲区
     *
     * @param input  输入流
     * @param output 输出流
     * @return 复制的总字节数
     * @throws IOException 读取或写入异常
     */
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    // -------------------- 跳过 --------------------

    /**
     * 跳过指定字节数
     *
     * @param input  输入流
     * @param toSkip 要跳过的字节数
     * @return 实际跳过的字节数
     * @throws IOException 跳过异常
     */
    public static long skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip <= 0) {
            return 0;
        }
        long remain = toSkip;
        while (remain > 0) {
            long skipped = input.skip(remain);
            if (skipped == 0) {
                // skip() 可能返回 0，需用 read() 推进一位
                int b = input.read();
                if (b == EOF) {
                    break;
                }
                skipped = 1;
            }
            remain -= skipped;
        }
        return toSkip - remain;
    }

    // -------------------- 关闭 --------------------

    /**
     * 安全关闭 Closeable，异常被静默吞掉
     *
     * @param closeable 要关闭的对象，null 被忽略
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                if (closeable instanceof ByteArrayInputStream) {
                    // ByteArrayInputStream.close() is a no-op
                    // Skip all remaining bytes to make read() return -1
                    ByteArrayInputStream bais = (ByteArrayInputStream) closeable;
                    bais.skip(Long.MAX_VALUE);
                }
                closeable.close();
            } catch (IOException e) {
                // swallow
            }
        }
    }

    /**
     * 安全关闭 Closeable（别名，语义更明确）
     */
    public static void closeQuietly(Closeable closeable) {
        close(closeable);
    }
}
