package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;
import com.zifang.util.bc.bytecode.verify.Frame;
import com.zifang.util.bc.bytecode.verify.StackMapFrameEntry;
import com.zifang.util.bc.bytecode.verify.VerificationType;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * StackMapTable 属性（JVMS §4.7.4）
 * <p>
 * 类型校验器需要的栈帧表。改过方法体就必须重算这张表，否则类加载直接 VerifyError，
 * 所以本属性既要能读出原始帧（供反编译与诊断用），也要能按算出来的新帧写出。
 * <p>
 * 没被改过就原样写回原始字节：帧是相对增量编码的，属性里没有方法的隐式初始帧，
 * 读回来的帧只是拿空帧近似展开的，再编码回去既不逐位一致也可能丢掉局部变量。
 */
public class StackMapTableAttribute extends AbstractAttribute {

    private final List<StackMapFrameEntry> frames = new ArrayList<>();

    /** 读到的原始属性体；帧被替换后不再使用 */
    private byte[] rawBody;

    /** 帧是否已被重新计算过 */
    private boolean framesReplaced;

    public StackMapTableAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    /**
     * 解析栈帧表
     *
     * @param inputStream 输入流
     */
    @Override
    public void read(InputStream inputStream) {
        try {
            rawBody = new com.zifang.util.bc.bytecode.model.readtype.ByteReader(inputStream)
                    .readFully(getAttributeLength().getValue());
            readFrames(new java.io.ByteArrayInputStream(rawBody));
        } catch (IOException e) {
            throw new IllegalStateException("读取 StackMapTable 帧失败", e);
        }
    }

    private void readFrames(InputStream inputStream) throws IOException {
        int numberOfEntries = U2.read(inputStream).intValue();
        int previousOffset = -1;
        // 编码基准是方法的隐式初始帧（由参数与 this 决定），属性里没有这个信息，
        // 因此以空帧近似——只影响还原出的局部变量表，写出时首帧一律写全量帧，不依赖基准。
        Frame previousFrame = Frame.empty();
        for (int i = 0; i < numberOfEntries; i++) {
            int tag = inputStream.read();
            if (tag < 0) {
                throw new IOException("读取 StackMapTable 帧时提前结束");
            }
            if (tag <= 63) {
                frames.add(new StackMapFrameEntry(previousOffset + tag + 1, previousFrame.copy()));
            } else if (tag <= 127) {
                VerificationType item = VerificationType.read(inputStream);
                Frame frame = previousFrame.copy();
                frame.getStack().add(item);
                frames.add(new StackMapFrameEntry(previousOffset + tag - 64 + 1, frame));
            } else if (tag == 247) {
                int delta = U2.read(inputStream).intValue();
                VerificationType item = VerificationType.read(inputStream);
                Frame frame = previousFrame.copy();
                frame.getStack().add(item);
                frames.add(new StackMapFrameEntry(previousOffset + delta + 1, frame));
            } else if (tag >= 248 && tag <= 250) {
                int delta = U2.read(inputStream).intValue();
                Frame frame = previousFrame.copy();
                frame.chopLocals(251 - tag);
                frames.add(new StackMapFrameEntry(previousOffset + delta + 1, frame));
            } else if (tag == 251) {
                int delta = U2.read(inputStream).intValue();
                frames.add(new StackMapFrameEntry(previousOffset + delta + 1, previousFrame.copy()));
            } else if (tag >= 252 && tag <= 254) {
                int delta = U2.read(inputStream).intValue();
                Frame frame = previousFrame.copy();
                frame.appendLocals(Frame.readLocals(tag - 251, inputStream));
                frames.add(new StackMapFrameEntry(previousOffset + delta + 1, frame));
            } else {
                int delta = U2.read(inputStream).intValue();
                int localsCount = U2.read(inputStream).intValue();
                List<VerificationType> locals = Frame.readLocals(localsCount, inputStream);
                int stackCount = U2.read(inputStream).intValue();
                List<VerificationType> stack = Frame.readLocals(stackCount, inputStream);
                frames.add(new StackMapFrameEntry(previousOffset + delta + 1, new Frame(locals, stack)));
            }
            previousOffset = frames.get(frames.size() - 1).getOffset();
            previousFrame = frames.get(frames.size() - 1).getFrame();
        }
    }

    /**
     * 已解析出的栈帧
     *
     * @return List类型返回值
     */
    public List<StackMapFrameEntry> getFrames() {
        return frames;
    }

    /**
     * 用重新算出的帧替换整张表
     *
     * @param newFrames 新帧（需按 offset 升序）
     */
    public void setFrames(List<StackMapFrameEntry> newFrames) {
        frames.clear();
        frames.addAll(newFrames);
        framesReplaced = true;
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        if (rawBody != null && !framesReplaced) {
            out.write(rawBody);
            return;
        }
        out.writeShort(frames.size());
        StackMapFrameEntry.StackMapFrameEncoder.writeAll(frames, out);
    }

    @Override
    public int contentSize() {
        if (rawBody != null && !framesReplaced) {
            return rawBody.length;
        }
        // 逐帧精确计算代价高且容易与编码规则脱节，用缓冲写一遍量长度最稳
        try {
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            java.io.DataOutputStream out = new java.io.DataOutputStream(buffer);
            writeBody(out);
            out.flush();
            return buffer.size();
        } catch (IOException e) {
            throw new IllegalStateException("计算 StackMapTable 长度失败", e);
        }
    }
}