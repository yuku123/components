package com.zifang.util.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by yihui on 2017/5/8.
 */
public class PropertiesUtil {


    /**
     * 读取properties文件的内容
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static Properties read(String fileName) throws IOException {
        try (InputStream inputStream = FileReadUtil.getStreamByFileName(fileName)) {
            Properties pro = new Properties();
            pro.load(inputStream);
            return pro;
        }
    }

}
