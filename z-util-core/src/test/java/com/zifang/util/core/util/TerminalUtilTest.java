package com.zifang.util.core.util;

import org.junit.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * TerminalUtil 测试。
 * <p>
 * 全部用例走 /bin/sh 与 POSIX 小工具，macOS 与 Linux 通用，不依赖个人目录。
 */
public class TerminalUtilTest {

    private static final long LARGE_OUTPUT_BYTES = 512 * 1024;

    @Test
    public void execCapturesStdoutAndExitCode() throws Exception {
        TerminalUtil.Result result = TerminalUtil.exec(
                Arrays.asList("sh", "-c", "echo hello; echo world"));

        assertEquals(0, result.exitCode());
        assertEquals("hello\nworld\n", result.stdout());
        assertEquals("", result.stderr());
    }

    @Test
    public void execKeepsNonZeroExitCode() throws Exception {
        TerminalUtil.Result result = TerminalUtil.exec(Arrays.asList("sh", "-c", "exit 3"));

        assertEquals(3, result.exitCode());
    }

    @Test
    public void execCapturesStderrSeparately() throws Exception {
        TerminalUtil.Result result = TerminalUtil.exec(
                Arrays.asList("sh", "-c", "echo to-out; echo to-err 1>&2; exit 1"));

        assertEquals(1, result.exitCode());
        assertEquals("to-out\n", result.stdout());
        assertEquals("to-err\n", result.stderr());
    }

    /**
     * 回归：旧实现只读 stdout，stderr 无人消费时子进程写满管道即永久阻塞。
     */
    @Test(timeout = 60000)
    public void execDoesNotDeadlockOnLargeStdoutAndStderr() throws Exception {
        TerminalUtil.Result result = TerminalUtil.exec(Arrays.asList(
                "sh", "-c",
                "head -c " + LARGE_OUTPUT_BYTES + " /dev/zero | tr '\\0' 'a'"
                        + "; head -c " + LARGE_OUTPUT_BYTES + " /dev/zero | tr '\\0' 'b' 1>&2"));

        assertEquals(LARGE_OUTPUT_BYTES, result.stdout().trim().length());
        assertEquals(LARGE_OUTPUT_BYTES, result.stderr().trim().length());
    }

    @Test
    public void execPassesArgumentsWithoutShellSplitting() throws Exception {
        TerminalUtil.Result result = TerminalUtil.exec(
                Arrays.asList("echo", "a b", "c"));

        assertEquals("a b c\n", result.stdout());
    }

    @Test
    public void execReadsUtf8Output() throws Exception {
        TerminalUtil.Result result = TerminalUtil.exec(Arrays.asList("echo", "中文路径/测试"));

        assertEquals("中文路径/测试\n", result.stdout());
    }

    @Test
    public void execKillsProcessOnTimeout() throws Exception {
        long start = System.nanoTime();
        try {
            TerminalUtil.exec(Arrays.asList("sleep", "30"), Duration.ofMillis(500));
            fail("超时应当抛 TimeoutException");
        } catch (TerminalUtil.TimeoutException e) {
            assertTrue("异常信息应指明是哪条命令超时: " + e.getMessage(),
                    e.getMessage().contains("sleep"));
        }
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        assertTrue("应尽快返回而非等满 30 秒，实际 " + elapsedMs + "ms", elapsedMs < 10000);
    }

    @Test
    public void execThrowsIoExceptionWhenCommandMissing() {
        try {
            TerminalUtil.exec(Collections.singletonList("no-such-command-xyz-abc"));
            fail("命令不存在应当抛 IOException");
        } catch (IOException expected) {
            // 预期
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }
}