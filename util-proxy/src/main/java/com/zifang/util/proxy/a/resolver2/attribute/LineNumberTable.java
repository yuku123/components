package com.zifang.util.proxy.a.resolver2.attribute;

import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//LineNumberTable属性用于描述Java源码行号与字节码行号（字节码的偏移量）之间的对应关系。
public class LineNumberTable extends AbstractAttribute {
    private U2 lineNumTableLength;
    private List<LineNumberInfo> lineNumberTable = new ArrayList<>();

    public LineNumberTable(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        lineNumTableLength = U2.read(inputStream);
        short value = lineNumTableLength.value;
        while (value > 0) {
            LineNumberInfo lineNumberInfo = new LineNumberInfo(U2.read(inputStream), U2.read(inputStream));
            lineNumberTable.add(lineNumberInfo);
            value--;
        }
    }


    public class LineNumberInfo {
        private U2 startPc;//字节码行号
        private U2 lineNumber;//java源码行号

        public LineNumberInfo(U2 startPc, U2 lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }

        public U2 getStartPc() {
            return startPc;
        }

        public void setStartPc(U2 startPc) {
            this.startPc = startPc;
        }

        public U2 getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(U2 lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
