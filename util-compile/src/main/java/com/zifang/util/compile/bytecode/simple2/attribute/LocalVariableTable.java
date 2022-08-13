package com.zifang.util.compile.bytecode.simple2.attribute;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;
import com.zifang.util.compile.bytecode.simple2.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocalVariableTable extends AbstractAttribute {
    private U2 localVariableTableLength;//局部变量槽的数量
    private List<LocalVariableInfo> localVariableTable = new ArrayList<>();//局部变量表的详细信息


    public LocalVariableTable(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        localVariableTableLength = U2.read(inputStream);
        short value = localVariableTableLength.value;
        while (value > 0) {
            LocalVariableInfo localVariableInfo = new LocalVariableInfo(U2.read(inputStream), U2.read(inputStream), U2.read(inputStream)
                    , U2.read(inputStream), U2.read(inputStream));
            localVariableTable.add(localVariableInfo);
            value--;
        }
    }

    public class LocalVariableInfo {
        private U2 startPc;//开始行号
        private U2 length;//偏移量   startPc+length 就是这个局部变量的作用范围
        private U2 nameIndex;  //名字
        private U2 descriptorIndex;//描述(变量类型)
        private U2 index;//位于第几个变量槽


        public LocalVariableInfo(U2 startPc, U2 length, U2 nameIndex, U2 descriptorIndex, U2 index) {
            this.startPc = startPc;
            this.length = length;
            this.nameIndex = nameIndex;
            this.descriptorIndex = descriptorIndex;
            this.index = index;
        }

        public U2 getStartPc() {

            return startPc;
        }

        public void setStartPc(U2 startPc) {
            this.startPc = startPc;
        }

        public U2 getLength() {
            return length;
        }

        public void setLength(U2 length) {
            this.length = length;
        }

        public U2 getNameIndex() {
            return nameIndex;
        }

        public void setNameIndex(U2 nameIndex) {
            this.nameIndex = nameIndex;
        }

        public U2 getDescriptorIndex() {
            return descriptorIndex;
        }

        public void setDescriptorIndex(U2 descriptorIndex) {
            this.descriptorIndex = descriptorIndex;
        }

        public U2 getIndex() {
            return index;
        }

        public void setIndex(U2 index) {
            this.index = index;
        }
    }
}
