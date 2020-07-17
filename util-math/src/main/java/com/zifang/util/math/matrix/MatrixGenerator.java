package com.zifang.util.math.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 矩阵的构造者 提供很多方式进行生产矩阵
 * */
public class MatrixGenerator {



    public Matrix array(List<? extends Object> list){
        Matrix numpyArray = new Matrix();
        return numpyArray;
    }

    public Matrix array(double[] arrys){
        List<? extends Object> list =  Arrays.asList(arrys);
        Matrix numpyArray = new Matrix();
        return numpyArray;
    }

//    public  NumpyArray array(E[] arrys,String type){
//        List<? extends Object> list =  Arrays.asList(arrys);
//        NumpyArray numpyArray = new NumpyArray();
//        numpyArray.setArray(list);
//        return numpyArray;
//    }


    /**
     * 生成全为零矩阵
     *
     * [ 0 0 0
     *   0 0 0
     *   0 0 0]
     * */
    public static Matrix zeros(Integer dimension){
        return null;
    }

    public void empty(){}

    public void arrange(){}

    public void arrange(Integer start,Integer end){}

    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("a");
        arrayList.add("n");
        MatrixGenerator numpy = new MatrixGenerator();
        numpy.array(arrayList);
    }

}
