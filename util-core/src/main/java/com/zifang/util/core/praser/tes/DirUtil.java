package com.zifang.util.core.praser.tes;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by yihui on 2017/5/6.
 */
public class DirUtil {


    /**
     * 递归创建文件夹
     *
     * @param file 由目录创建的file对象
     * @throws FileNotFoundException
     */
    public static void mkDir(File file) throws FileNotFoundException {
        if (file == null) {
            throw new FileNotFoundException();
        }

        if (file.getParentFile().exists()) {
            if (file.exists()) { // 目录存在, 则直接返回
                return;
            }

            if (!file.mkdir()) { // 不存在, 则创建
                throw new FileNotFoundException();
            }
        } else {
            mkDir(file.getParentFile()); // 创建父目录
            if (!file.exists() && !file.mkdir()) {
                throw new FileNotFoundException();
            }
        }
    }

}
