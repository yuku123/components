package com.zifang.util.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 子进程执行工具。
 * <p>
 * 命令以参数列表传入，不做字符串拼接——需要 shell 语法（管道、重定向）时由调用方
 * 显式写 {@code exec(Arrays.asList("sh", "-c", cmd))}，让 shell 这一步在调用点可见。
 *
 * @author zifang
 */
public final class TerminalUtil {

    private TerminalUtil() {
    }

    /**
     * 执行命令并等它自然结束。
     *
     * @param command 命令与参数，不含重定向或管道
     * @return 执行结果，含退出码与 stdout / stderr
     * @throws IOException          命令无法启动
     * @throws InterruptedException 等待期间线程被中断
     */
    public static Result exec(List<String> command) throws IOException, InterruptedException {
        return run(command, null);
    }

    /**
     * 执行命令，超时则强杀进程并抛异常。
     *
     * @param command 命令与参数，不含重定向或管道
     * @param timeout 超时时长，为 null 时等同 {@link #exec(List)}
     * @return 执行结果，含退出码与 stdout / stderr
     * @throws IOException          命令无法启动
     * @throws InterruptedException 等待期间线程被中断
     * @throws TerminalUtil.TimeoutException 超时未退出，进程已被强杀
     */
    public static Result exec(List<String> command, Duration timeout)
            throws IOException, InterruptedException {
        return run(command, timeout);
    }

    private static Result run(List<String> command, Duration timeout)
            throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();

        // stdout 与 stderr 必须与进程并行排空：任一侧等到 waitFor 之后才读，子进程
        // 写满管道缓冲区后就会卡在写上永远不退出，形成死锁。
        StreamDrainer stdoutDrainer = new StreamDrainer(process.getInputStream(), "terminal-util-stdout");
        StreamDrainer stderrDrainer = new StreamDrainer(process.getErrorStream(), "terminal-util-stderr");

        int exitCode;
        try {
            if (timeout == null) {
                exitCode = process.waitFor();
            } else if (process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                exitCode = process.exitValue();
            } else {
                process.destroyForcibly();
                process.waitFor();
                throw new TimeoutException("命令执行超时（" + timeout + "）并已被强杀: " + command);
            }
        } finally {
            stdoutDrainer.await();
            stderrDrainer.await();
        }

        return new Result(exitCode, stdoutDrainer.content(), stderrDrainer.content());
    }

    private static final class StreamDrainer implements Runnable {

        private final InputStream stream;
        private final Thread thread;
        private volatile String content = "";

        StreamDrainer(InputStream stream, String threadName) {
            this.stream = stream;
            this.thread = new Thread(this, threadName);
            this.thread.setDaemon(true);
            this.thread.start();
        }

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                // 进程被强杀时流会异常关闭，已读到的部分仍然有效
            }
            content = sb.toString();
        }

        void await() throws InterruptedException {
            thread.join();
        }

        String content() {
            return content;
        }
    }

    /**
     * 命令在给定时间内未退出，进程已被强杀。
     * <p>
     * 非受检异常：只有带 timeout 的调用点才可能遇到，不该让所有调用方都为此写 catch。
     */
    public static final class TimeoutException extends RuntimeException {

        TimeoutException(String message) {
            super(message);
        }
    }

    /**
     * 子进程执行结果。
     * <p>
     * stdout / stderr 为原始文本，按行拼接且每行以 {@code \n} 结尾，不做 trim。
     * 进程非零退出不算失败，由调用方比对 {@link #exitCode()}。
     */
    public static final class Result {

        private final int exitCode;
        private final String stdout;
        private final String stderr;

        Result(int exitCode, String stdout, String stderr) {
            this.exitCode = exitCode;
            this.stdout = stdout;
            this.stderr = stderr;
        }

        public int exitCode() {
            return exitCode;
        }

        public String stdout() {
            return stdout;
        }

        public String stderr() {
            return stderr;
        }

        @Override
        public String toString() {
            return "Result{exitCode=" + exitCode
                    + ", stdout=" + stdout.trim()
                    + ", stderr=" + stderr.trim() + '}';
        }
    }
}