package com.zifang.util.zex.bust.chapter6;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTest {
    public static void main(String[] args) {
        log.info("打印info日志");
        log.debug("打印debug日志");
        log.warn("打印warn日志");
        log.error("打印error日志");
    }
}
