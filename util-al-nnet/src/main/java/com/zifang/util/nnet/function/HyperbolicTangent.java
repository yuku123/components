package com.zifang.util.nnet.function;

/**
 * 双曲正切函数
 */
public class HyperbolicTangent implements IActivationFunction {

    /**
     * f(x) = (1-e^(-x)) / (1+e^(x))
     */
    @Override
    public Double calculate(Double x) {
        return (1D - Math.pow(E, -x)) / (1D + Math.pow(E, x));
    }
}
