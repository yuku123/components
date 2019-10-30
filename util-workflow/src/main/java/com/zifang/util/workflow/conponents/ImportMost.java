package com.zifang.util.workflow.conponents;

import org.apache.log4j.Logger;

public class ImportMost {
    private static Logger logger = Logger.getLogger(ImportMost.class);

    public static void main(String[] args) {
        // debug级别的信息
        logger.debug("This is a debug");
        // info级别的信息
        logger.info("This is a info");
        // error级别的信息
        logger.error("This is a error");
    }
}