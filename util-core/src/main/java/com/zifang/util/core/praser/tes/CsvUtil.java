//package com.zifang.util.core.praser.tes;
//
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.util.List;
//
///**
// * Created by yihui on 2017/5/6.
// */
//public class CsvUtil {
//
//    /**
//     * 读取csv文件, 返回结构化的对象
//     *
//     * @param filename csv 路径 + 文件名, 支持绝对路径 + 相对路径 + 网络文件
//     * @param headers  csv 每列的数据名
//     * @return
//     * @throws IOException
//     */
//    public static List<CSVRecord> read(String filename, String[] headers) throws IOException {
//        try (Reader reader = FileReadUtil.createCharRead(filename)) {
//            CSVParser csvParser = new CSVParser(reader,
//                    CSVFormat.INFORMIX_UNLOAD_CSV.withHeader(headers)
//            );
//
//            return csvParser.getRecords();
//        }
//    }
//}
