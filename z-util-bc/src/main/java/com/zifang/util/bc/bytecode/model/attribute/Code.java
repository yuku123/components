package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.readtype.U1;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Code属性
 * <p>
 * 方法的字节码指令存储在这个属性中。
 */
public class Code extends AbstractAttribute {
    private U2 maxStack;
    private U2 maxLocals;
    private U4 codeLength;
    private List<U1> code = new ArrayList<>();
    private U2 exceptionTableLength;
    private List<ExceptionInfo> exceptionTable = new ArrayList<>();
    private U2 attributesCount;
    private List<AbstractAttribute> attributes = new ArrayList<>();
    private List<AbstractConstantPool> poolList;

    /**
     * Code方法。
     * * @param attributeNameIndex U2类型参数
     *
     * @param attributeLength U4类型参数
     * @param poolList        ListAbstractConstantPool类型参数
     */
    public Code(U2 attributeNameIndex, U4 attributeLength, List<AbstractConstantPool> poolList) {
        super(attributeNameIndex, attributeLength);
        this.poolList = poolList;
    }

    @Override
    /**
     * read方法。
     *      * @param inputStream InputStream类型参数
     */
    public void read(InputStream inputStream) {
        maxStack = U2.read(inputStream);
        maxLocals = U2.read(inputStream);
        codeLength = U4.read(inputStream);
        int value = codeLength.value;
        while (value > 0) {
            code.add(U1.read(inputStream));
            value--;
        }
        exceptionTableLength = U2.read(inputStream);
        short exLength = exceptionTableLength.value;
        while (exLength > 0) {
            ExceptionInfo exceptionInfo = new ExceptionInfo(
                    U2.read(inputStream),
                    U2.read(inputStream),
                    U2.read(inputStream),
                    U2.read(inputStream)
            );
            exceptionTable.add(exceptionInfo);
            exLength--;
        }
        attributesCount = U2.read(inputStream);
        short attrCount = attributesCount.value;
        while (attrCount > 0) {
            AbstractAttribute attributeTable = AttributeFactory.getAttributeTable(inputStream, poolList);
            if (attributeTable != null) {
                attributes.add(attributeTable);
            }
            attrCount--;
        }
    }

    /**
     * getMaxStack方法。
     *
     * @return U2类型返回值
     */
    public U2 getMaxStack() {
        return maxStack;
    }

    /**
     * getMaxLocals方法。
     *
     * @return U2类型返回值
     */
    public U2 getMaxLocals() {
        return maxLocals;
    }

    /**
     * 替换指令字节，同时同步 code_length
     *
     * @param code 新的指令列表
     */
    public void setCode(List<U1> code) {
        this.code = code;
        this.codeLength = new U4(code.size());
    }

    /**
     * getCodeLength方法。
     *
     * @return U4类型返回值
     */
    public U4 getCodeLength() {
        return codeLength;
    }

    /**
     * getCode方法。
     *
     * @return List<U1>类型返回值
     */
    public List<U1> getCode() {
        return code;
    }

    /**
     * getExceptionTableLength方法。
     *
     * @return U2类型返回值
     */
    public U2 getExceptionTableLength() {
        return exceptionTableLength;
    }

    /**
     * getExceptionTable方法。
     *
     * @return List<ExceptionInfo>类型返回值
     */
    public List<ExceptionInfo> getExceptionTable() {
        return exceptionTable;
    }

    /**
     * getAttributesCount方法。
     *
     * @return U2类型返回值
     */
    public U2 getAttributesCount() {
        return attributesCount;
    }

    /**
     * 解析时用的常量池（用于方法体异常处理/属性引用等场景的二次解析）。
     */
    public List<AbstractConstantPool> getPoolList() {
        return poolList;
    }

    /**
     * getAttributes方法。
     *
     * @return List<AbstractAttribute>类型返回值
     */
    public List<AbstractAttribute> getAttributes() {
        return attributes;
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        maxStack.write(out);
        maxLocals.write(out);
        out.writeInt(code.size());
        for (U1 b : code) {
            b.write(out);
        }
        out.writeShort(exceptionTable.size());
        for (ExceptionInfo e : exceptionTable) {
            e.writeBody(out);
        }
        out.writeShort(attributes.size());
        for (AbstractAttribute a : attributes) {
            a.write(out);
        }
    }

    @Override
    public int contentSize() {
        int n = 2 + 2 + 4 + code.size() + 2 + exceptionTable.size() * 8 + 2;
        for (AbstractAttribute a : attributes) {
            n += 6 + a.contentSize();
        }
        return n;
    }

    /**
     * 异常表项
     */
    public static class ExceptionInfo {
        private U2 startPc;
        private U2 endPc;
        private U2 handlerPc;
        private U2 catchPc;

        public ExceptionInfo(U2 startPc, U2 endPc, U2 handlerPc, U2 catchPc) {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.catchPc = catchPc;
        }

        void writeBody(DataOutput out) throws IOException {
            startPc.write(out);
            endPc.write(out);
            handlerPc.write(out);
            catchPc.write(out);
        }

        public U2 getStartPc() {
            return startPc;
        }

        public void setStartPc(U2 startPc) {
            this.startPc = startPc;
        }

        public U2 getEndPc() {
            return endPc;
        }

        public void setEndPc(U2 endPc) {
            this.endPc = endPc;
        }

        public U2 getHandlerPc() {
            return handlerPc;
        }

        public void setHandlerPc(U2 handlerPc) {
            this.handlerPc = handlerPc;
        }

        public U2 getCatchPc() {
            return catchPc;
        }

        public void setCatchPc(U2 catchPc) {
            this.catchPc = catchPc;
        }
    }
}
