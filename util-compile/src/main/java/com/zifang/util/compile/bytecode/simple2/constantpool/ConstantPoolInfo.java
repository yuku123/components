package com.zifang.util.compile.bytecode.simple2.constantpool;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.util.ArrayList;
import java.util.List;

public class ConstantPoolInfo {
    private U2 poolSize;
    private List<AbstractConstantPool> poolList;

    public ConstantPoolInfo(short poolSize){
        this.poolSize=new U2(poolSize);
        poolList=new ArrayList<AbstractConstantPool>(poolSize);
    }

    public U2 getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(U2 poolSize) {
        this.poolSize = poolSize;
    }

    public List<AbstractConstantPool> getPoolList() {
        return poolList;
    }

    public void setPoolList(List<AbstractConstantPool> poolList) {
        this.poolList = poolList;
    }

    @Override
    public String toString() {
        return "ConstantPoolInfo{" +
                "poolSize=" + poolSize.value +
                ", poolList=" + poolList +
                '}';
    }
}
