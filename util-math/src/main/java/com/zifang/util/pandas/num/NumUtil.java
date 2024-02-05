package com.zifang.util.pandas.num;

import java.util.List;

public class NumUtil {

    public static final NumRandom random = new NumRandom();

    /** 创建数组 */
    public static Num array(Object array){
        return new Num(array);
    }

    /** 创建数组 */
    public static Num array(List<?> list){
        return new Num(list.toArray());
    }

    /** 在给定间隔内返回均匀间隔的数值 */
    public static Num aRange(){
        return null;
    }

    // inspace()可以用来返回在间隔[开始，停止]上计算的num个均匀间隔的样本：
    // print(np.linspace(10,15,num=20))
    public static Num linSpace(){
        return null;
    }

    // # 创建数组且用0填充
    // print(np.zeros((3,5),dtype=np.int)) # dtype可以将元素变成整数
    public static Num zeros(){
        return null;
    }

    // ar2=np.ones(9) # 用1填充
    public static Num ones(){
        return null;
    }

    // print(np.eye(5)) # 中间数是1，其他都是0
    public static Num eye(){
        return null;
    }



    // # 横向连接
    public void hStack(){

    }

    // # 纵向连接
    public void vStack(){

    }

    // print(np.hsplit(ar,2)[0])
    // print(np.vsplit(ar,4))

    public void hsplit(){

    }

    public void vsplit(){

    }


    public static void main(String[] args) {

    }
}
