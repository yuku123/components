package com.zifang.util.compile.bytecode.resolver2.attribute;

import com.zifang.util.compile.bytecode.resolver2.readtype.U1;
import com.zifang.util.compile.bytecode.resolver2.readtype.U2;
import com.zifang.util.compile.bytecode.resolver2.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Code extends AbstractAttribute {
    private U2 maxStack;//最大操作数栈深度
    private U2 maxLocals;//最大的局部变量表
    private U4 codeLength;//字节码指令的长度
    private List<U1> code = new ArrayList<>();//字节码指令
    private U2 exceptionTableLength;//异常表长度
    private List<ExceptionInfo> exceptionTable = new ArrayList<>();//异常表详细信息
    private U2 attributesCount;//属性的数量
    private List<AbstractAttribute> attributes = new ArrayList<>();//Code的属性列表


    public Code(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
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
            ExceptionInfo exceptionInfo = new ExceptionInfo(U2.read(inputStream), U2.read(inputStream), U2.read(inputStream), U2.read(inputStream));
            exceptionTable.add(exceptionInfo);
            exLength--;
        }
        attributesCount = U2.read(inputStream);
        short attrCount = attributesCount.value;
        while (attrCount > 0) {
            AbstractAttribute attributeTable = AttributeFactory.getAttributeTable(inputStream);
            attributes.add(attributeTable);
            attrCount--;
        }
    }

    public class ExceptionInfo {
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

    public U2 getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(U2 maxStack) {
        this.maxStack = maxStack;
    }

    public U2 getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(U2 maxLocals) {
        this.maxLocals = maxLocals;
    }

    public U4 getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(U4 codeLength) {
        this.codeLength = codeLength;
    }

    public List<U1> getCode() {
        return code;
    }

    public void setCode(List<U1> code) {
        this.code = code;
    }

    public U2 getExceptionTableLength() {
        return exceptionTableLength;
    }

    public void setExceptionTableLength(U2 exceptionTableLength) {
        this.exceptionTableLength = exceptionTableLength;
    }

    public List<ExceptionInfo> getExceptionTable() {
        return exceptionTable;
    }

    public void setExceptionTable(List<ExceptionInfo> exceptionTable) {
        this.exceptionTable = exceptionTable;
    }

    public U2 getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(U2 attributesCount) {
        this.attributesCount = attributesCount;
    }

    public List<AbstractAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AbstractAttribute> attributes) {
        this.attributes = attributes;
    }
}
