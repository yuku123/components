package com.zifang.util.pandas.num;

import java.util.List;

public class Nums {

    public static final NumRandom random = new NumRandom();

    /**
     * 创建数组
     */
    public static Num array(Object array) {
        return new Num(array);
    }

    /**
     * 创建数组
     */
    public static Num array(List<?> list) {
        return new Num(list.toArray());
    }

    /**
     * 使用维度创建空数组
     */
    public static Num array(int[] shape, DType dType) {
        return null;
    }

    /**
     * 使用维度创建空数组
     */
    public static Num array(int[] shape, Object[] objs, DType dType) {
        return null;
    }

    /**
     * 使用对象序列填充num
     */
    public static Num fill(Num num, Object[] objs) {
        return null;
    }

    /**
     * 在给定间隔内返回均匀间隔的数值
     * 10 -> 0~9
     * 10.0 -> 0.0 ~ 9.0
     * 5,15 -> 5 ~ 14
     * 5.0,12.0,2 -> 5.0~12.0，步长为2
     */
    public static Num aRange(Object... i) {
        return null;
    }


    // inspace()可以用来返回在间隔[开始，停止]上计算的num个均匀间隔的样本：
    // print(np.linspace(10,15,num=20))

    /**
     * 选定区域
     *
     * @param i        开始数值
     * @param j        停止数值
     * @param num      均匀间隔的样本数量
     * @param endPoint 是否包含最后的值
     */
    public static Num linSpace(Number i, Number j, Integer num, Boolean endPoint) {
        return null;
    }

    // #
    // print(np.zeros((3,5),dtype=np.int)) # dtype可以将元素变成整数

    /**
     * 创建数组且用0填充
     */
    public static Num zeros(Integer[] shapes, DType dType) {
        return null;
    }

    // ar2=np.ones(9) # 用1填充
    public static Num ones() {
        return null;
    }

    // print(np.eye(5)) # 中间数是1，其他都是0
    public static Num eye() {
        return null;
    }

    // # 横向连接
    public void hStack() {

    }

    // # 纵向连接
    public void vStack() {

    }

    // print(np.hsplit(ar,2)[0])
    // print(np.vsplit(ar,4))

    public void hsplit() {

    }

    public void vsplit() {

    }
}
