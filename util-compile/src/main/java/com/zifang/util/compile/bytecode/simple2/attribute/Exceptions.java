package com.zifang.util.compile.bytecode.simple2.attribute;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;
import com.zifang.util.compile.bytecode.simple2.readtype.U4;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Exceptions extends AbstractAttribute {
    private U2 numberOfExceptions;//方法throw的异常数量;
    private List<U2> exceptionIndexTable = new ArrayList<>(numberOfExceptions.value);//指向常量池中constant_class_table型常量的索引;


    public Exceptions(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }

    public U2 getNumberOfExceptions() {
        return numberOfExceptions;
    }

    public void setNumberOfExceptions(U2 numberOfExceptions) {
        this.numberOfExceptions = numberOfExceptions;
    }

    public List<U2> getExceptionIndexTable() {
        return exceptionIndexTable;
    }

    public void setExceptionIndexTable(List<U2> exceptionIndexTable) {
        this.exceptionIndexTable = exceptionIndexTable;
    }
}
