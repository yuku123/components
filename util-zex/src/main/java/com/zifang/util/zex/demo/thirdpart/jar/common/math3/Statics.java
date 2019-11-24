package com.zifang.util.zex.demo.thirdpart.jar.common.math3;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class Statics {

    public static void main(String[] args) {
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        Double[] inputArray = new Double[]{1.0,2.0,3.0};

        // Get a DescriptiveStatistics instance
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.setWindowSize(2);
// Add the data from the array
        for( int i = 0; i < inputArray.length; i++) {
            stats.addValue(inputArray[i]);

        }

        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        double median = stats.getPercentile(50);

        System.out.println(mean);
        System.out.println(std);
        System.out.println(median);

//// Compute some statistics
//        double mean = stats.getMean();
//        double std = stats.getStandardDeviation();
//        double median = stats.getPercentile(50);
//
//        System.out.println(mean);
//        System.out.println(std);
//        System.out.println(median);


    }
}
