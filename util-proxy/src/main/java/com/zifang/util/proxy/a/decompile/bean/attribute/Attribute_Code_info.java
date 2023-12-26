package com.zifang.util.proxy.a.decompile.bean.attribute;

import com.zifang.util.proxy.a.decompile.bean.ExceptionTable;
import com.zifang.util.proxy.a.decompile.bean.OpcodeAndOperand;

import java.util.List;
import java.util.Map;


public class Attribute_Code_info extends Attribute_info {

    private int attribute_name_index;
    private int attrbute_length;

    private int max_statck;
    private int max_locals;
    private int code_length;
    private Map<Integer, OpcodeAndOperand> codeMap;
    private int exception_table_length;
    private List<ExceptionTable> exceptionTablesList;
    private int attributes_count;
    private List<Attribute_info> attributesList;


    @Override
    public int getAttribute_name_index() {
        return attribute_name_index;
    }

    @Override
    public void setAttribute_name_index(int attribute_name_index) {
        this.attribute_name_index = attribute_name_index;
    }

    @Override
    public int getAttrbute_length() {
        return attrbute_length;
    }

    @Override
    public void setAttrbute_length(int attrbute_length) {
        this.attrbute_length = attrbute_length;
    }

    public int getMax_statck() {
        return max_statck;
    }

    public void setMax_statck(int max_statck) {
        this.max_statck = max_statck;
    }

    public int getMax_locals() {
        return max_locals;
    }

    public void setMax_locals(int max_locals) {
        this.max_locals = max_locals;
    }

    public int getCode_length() {
        return code_length;
    }

    public void setCode_length(int code_length) {
        this.code_length = code_length;
    }

    public Map<Integer, OpcodeAndOperand> getCodeMap() {
        return codeMap;
    }

    public void setCodeMap(Map<Integer, OpcodeAndOperand> codeMap) {
        this.codeMap = codeMap;
    }

    public int getException_table_length() {
        return exception_table_length;
    }

    public void setException_table_length(int exception_table_length) {
        this.exception_table_length = exception_table_length;
    }

    public List<ExceptionTable> getExceptionTablesList() {
        return exceptionTablesList;
    }

    public void setExceptionTablesList(List<ExceptionTable> exceptionTablesList) {
        this.exceptionTablesList = exceptionTablesList;
    }

    public int getAttributes_count() {
        return attributes_count;
    }

    public void setAttributes_count(int attributes_count) {
        this.attributes_count = attributes_count;
    }

    public List<Attribute_info> getAttributesList() {
        return attributesList;
    }

    public void setAttributesList(List<Attribute_info> attributesList) {
        this.attributesList = attributesList;
    }


}
