package com.zifang.util.core.io;//package com.zifang.util.core.praser.tes;
//
//import org.ini4j.Config;
//import org.ini4j.Ini;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by yihui on 2017/5/8.
// */
//public class IniUtil {
//
//
//    /**
//     * ini文件读取
//     * @param fileName 文件名
//     * @return
//     * @throws IOException
//     */
//    public static Ini read(String fileName) throws IOException {
//
//        try (InputStream inputStream = FileReadUtil.getStreamByFileName(fileName)) {
//            Config cfg = new Config();
//            // 设置Section允许出现重复
//            cfg.setMultiSection(true);
//            Ini ini = new Ini();
//            ini.setConfig(cfg);
//
//            ini.load(inputStream);
//
//            return ini;
//
//        }
//
//    }
//
//}
