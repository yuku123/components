package com.zifang.util.nnet.function;

/**
 * s激活函数
 * */
public class Sigmoid implements IActivationFunction {


    /**
     *
     * f(x) = 1/(1+e^-x)
     *
     * */
    @Override
    public Double calculate(Double x) {
        return 1D/(1+Math.pow(Math.E,-x));
    }
}
