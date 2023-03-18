package com.zifang.util.ai.nnet.function;

/**
 * 阈值函数
 */
public class HardLimitThreshold implements IActivationFunction {
    @Override
    public Double calculate(Double x) {
        return x < 0D ? 0D : 1D;
    }
}
