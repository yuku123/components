package com.zifang.util.zex.jar;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class huigui {

    public static void main(String[] args) {
        OLSMultipleLinearRegression regression =new OLSMultipleLinearRegression();
        double[] y =new double[]{11.0,12.0,13.0,14.0,15.0,16.0};
        double[][] x =new double[6][];
        x[0]=new double[]{0,0,0,0,0};
        x[1]=new double[]{2.0,0,0,0,0};
        x[2]=new double[]{0,3.0,0,0,0};
        x[3]=new double[]{0,0,4.0,0,0};
        x[4]=new double[]{0,0,0,5.0,0};
        x[5]=new double[]{0,0,0,0,6.0};
        regression.newSampleData(y,x);

        double[] beta = regression.estimateRegressionParameters();//beta值

        double[] residuals = regression.estimateResiduals();
        //残余方差
        double[][] parametersVariance = regression.estimateRegressionParametersVariance();
        double regressandVariance = regression.estimateRegressandVariance();
        double rSquared = regression.calculateRSquared();
        //R回归方差
        double sigma = regression.estimateRegressionStandardError();//标准差

    }
}
