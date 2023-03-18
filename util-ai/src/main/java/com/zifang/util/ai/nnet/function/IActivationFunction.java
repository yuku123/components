package com.zifang.util.ai.nnet.function;

/**
 * 激活函数接口
 */
public interface IActivationFunction {

    Double E = Math.E;

    Double calculate(Double x);
}
