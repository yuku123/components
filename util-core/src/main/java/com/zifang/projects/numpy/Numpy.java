package com.zifang.projects.numpy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Numpy<E> {
    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("a");
        arrayList.add("n");
        Numpy numpy = new Numpy();
        numpy.array(arrayList);
    }

    public  NumpyArray array(List<? extends Object> list){
        NumpyArray numpyArray = new NumpyArray();
        numpyArray.setArray(list);
        return numpyArray;
    }

    public  NumpyArray array(E[] arrys){
        List<? extends Object> list =  Arrays.asList(arrys);
        NumpyArray numpyArray = new NumpyArray();
        numpyArray.setArray(list);
        return numpyArray;
    }

//    public  NumpyArray array(E[] arrys,String type){
//        List<? extends Object> list =  Arrays.asList(arrys);
//        NumpyArray numpyArray = new NumpyArray();
//        numpyArray.setArray(list);
//        return numpyArray;
//    }

    public void zeros(){}

    public void empty(){}

    public void arrange(){}

    public void arrange(Integer start,Integer end){}


}
