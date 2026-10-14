package com.zifang.util.bc.bytecode.verify;

import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * StackMapTable 的一条栈帧项（JVMS §4.7.4）
 * <p>
 * 属性里同一条语义帧有七种编码（same / same_locals_1_stack_item / chop / append / full 及其
 * extended 变体），能用短的就不用长的。短编码要求 offset_delta ≤ 63，
 * 而 chop/append 又取决于与上一帧的局部变量差异——所以本类只保存
 * 「目标偏移 + 语义帧」，编码统一交给 {@link StackMapFrameEncoder}。
 */
public final class StackMapFrameEntry {

    private final int offset;
    private final Frame frame;

    /**
     * @param offset 目标指令偏移
     * @param frame  该偏移处的语义帧
     */
    public StackMapFrameEntry(int offset, Frame frame) {
        this.offset = offset;
        this.frame = frame;
    }

    public int getOffset() {
        return offset;
    }

    public Frame getFrame() {
        return frame;
    }

    @Override
    public String toString() {
        return "@" + offset + " " + frame;
    }

    /**
     * 帧编码器
     * <p>
     * 只依赖 JVMS §4.7.4 的帧类型表：标签取值本身编码了 offset_delta 与局部变量差异，
     * 所以「选哪种帧」等价于「用哪一段字节把 delta 和差异说清楚」。
     */
    public static final class StackMapFrameEncoder {

        private StackMapFrameEncoder() {
        }

        /**
         * 写出整张栈帧表
         *
         * @param entries 已按 offset 升序排列的帧项
         * @param out     输出流
         * @throws IOException 写出失败
         */
        public static void writeAll(List<StackMapFrameEntry> entries, DataOutput out) throws IOException {
            Frame previous = null;
            int previousOffset = -1;
            for (StackMapFrameEntry entry : entries) {
                int delta = entry.getOffset() - previousOffset - 1;
                if (delta < 0) {
                    throw new ClassVerifyException("栈帧偏移非递增: " + previousOffset + " -> " + entry.getOffset(),
                            entry.getOffset());
                }
                if (previous == null) {
                    // 首帧必须写全量：短编码的基准是方法的隐式初始帧，而属性里没有它，
                    // JVM 解码时却会拿真初始帧去接，猜错基准就会多出局部变量直接被判格式错
                    out.writeByte(255);
                    out.writeShort(delta);
                    entry.getFrame().write(out);
                } else {
                    writeOne(out, delta, entry.getFrame(), previous);
                }
                previousOffset = entry.getOffset();
                previous = entry.getFrame();
            }
        }

        private static void writeOne(DataOutput out, int delta, Frame frame, Frame previous) throws IOException {
            boolean shortDelta = delta < 64;
            boolean sameLocals = frame.sameLocals(previous);
            int prevLocalsSize = previous.localsSize();
            int diff = frame.localsSize() - prevLocalsSize;

            // 先处理非 full 的短编码，逐条对齐 §4.7.4 的标签语义
            if (!frame.emptyStack() && frame.singleStackItem() && sameLocals) {
                if (shortDelta) {
                    out.writeByte(64 + delta);
                } else {
                    out.writeByte(247);
                    out.writeShort(delta);
                }
                frame.writeStackItem(out);
                return;
            }
            if (frame.emptyStack() && sameLocals) {
                if (shortDelta) {
                    out.writeByte(delta);
                } else {
                    out.writeByte(251);
                    out.writeShort(delta);
                }
                return;
            }
            if (frame.emptyStack() && diff > 0 && diff <= 3) {
                // append_frame：标签 252..254 隐含追加 1..3 项，只写追加的那部分
                out.writeByte(251 + diff);
                out.writeShort(delta);
                frame.writeLocalsFrom(out, prevLocalsSize);
                return;
            }
            if (frame.emptyStack() && diff < 0 && diff >= -3) {
                // chop_frame：标签 248..250 隐含砍掉 3..1 项，只写 delta
                out.writeByte(248 - diff);
                out.writeShort(delta);
                return;
            }
            out.writeByte(255);
            out.writeShort(delta);
            frame.write(out);
        }
    }
}