package com.zifang.util.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * 写文件工具类
 * <p/>
 * Created by yihui on 2017/5/17.
 */
public class FileWriteUtil {

    public enum WriteType {
        BUFFER,
        WRITER
    }


    private Object output;

    private WriteType currentType;


    public static FileWriteUtil newInstance(WriteType writeType, String filename, boolean isAppend) throws IOException {
        return new FileWriteUtil(writeType, filename, isAppend);
    }


    private FileWriteUtil(WriteType writeType, String filename, boolean isAppend) throws IOException {
        currentType = writeType;

        if (writeType == WriteType.BUFFER) {
            output = new BufferedOutputStream(new FileOutputStream(filename, isAppend));
        } else if (writeType == WriteType.WRITER) {
            output = new OutputStreamWriter(new FileOutputStream(filename, isAppend), Charset.forName("UTF-8"));
        }
    }


    public FileWriteUtil write(String data) throws IOException {
        if (currentType == WriteType.BUFFER) {
            ((BufferedOutputStream) output).write(data.getBytes());
            ((BufferedOutputStream) output).flush();
        } else {
            ((OutputStreamWriter) output).write(data);
            ((OutputStreamWriter) output).flush();
        }

        return this;
    }


    public void close() throws IOException {
        if (currentType == WriteType.BUFFER) {
            ((BufferedOutputStream) output).close();
        } else {
            ((OutputStreamWriter) output).close();
        }
    }
}
