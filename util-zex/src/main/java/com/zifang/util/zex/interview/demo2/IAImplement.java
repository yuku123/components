package com.zifang.util.zex.interview.demo2;

import lombok.Data;

@Data
public class IAImplement implements IA ,IB{

    private String className ;
    private String method ;
    private String returns;

    private String controlStr;
    public IAImplement(String controlStr){
        this.controlStr = controlStr;
        className = controlStr.split("[$]")[0];
        method = controlStr.split("[$]")[1].split("=")[0];
        returns = controlStr.split("[$]")[1].split("=")[1];
    }
    @Override
    public String getName() {
        return returns;
    }

    @Override
    public String getYear() {
        return null;
    }

    @Override
    public IB getIB(String b) {
        return null;
    }

    @Override
    public String getIBName() {
        return null;
    }
}
