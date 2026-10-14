package com.zifang.util.bc.bytecode.verify;

/**
 * 字节码校验失败
 * <p>
 * 帧计算的前提是字节码本身可校验。方法体被改坏（栈深不匹配、分支汇合处类型冲突、
 * 未初始化的 new 结果逃逸）时，JVM 会在类加载阶段抛 VerifyError，
 * 与其等到 defineClass 时拿一个说不清的 VerifyError，不如在写字节时就把位置报出来。
 */
public class ClassVerifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message 失败原因
     * @param pc      出问题的指令偏移，-1 表示不针对具体指令
     */
    public ClassVerifyException(String message, int pc) {
        super(pc >= 0 ? message + "（pc=" + pc + "）" : message);
    }
}