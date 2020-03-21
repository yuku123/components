//package com.zifang.util.core.praser.tes;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//
///**
// * Created by yihui on 2017/5/6.
// */
//public class JsonUtil {
//
//
//    /**
//     * 读取json文件, 并转为特定对象
//     * @param filename
//     * @param typeReference
//     * @param <T>
//     * @return
//     * @throws IOException
//     */
//    public static <T> T read(String filename, TypeReference<T> typeReference) throws IOException {
//
//        try (BufferedReader bf = FileReadUtil.createLineRead(filename)) {
//            StringBuilder stringBuffer = new StringBuilder();
//            String line;
//            while ((line = bf.readLine()) != null) {
//                stringBuffer.append(line);
//            }
//
//            return JSON.parseObject(stringBuffer.toString(), typeReference);
//        }
//    }
//
//}
