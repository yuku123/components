package com.zifang.util.pandas.num;

import com.zifang.util.core.lang.collection.ArraysUtil;

public class Num {

    private Object array;

    public Num(Object array){
        if(!array.getClass().isArray()){
            throw new RuntimeException("param is not A array");
        }
        this.array = array;
    }

    /**
     * 表达数组的维度
     */
    public int nDim() {
        String arrayClassRefNameTemp = array.getClass().getName();
        int dim = 0;
        while (true){
            if(arrayClassRefNameTemp.startsWith("[")){
                dim = dim +1;
                arrayClassRefNameTemp = arrayClassRefNameTemp.substring(1,arrayClassRefNameTemp.length() -1);
            } else {
                break;
            }
        }
        return dim;
    }


//    /** 捕获最终维度数据 */
//    public Object[] slice(){
//
//    }

    /**
     * 数组的维度，对于n行m列的数组 -> (n1,n2,n3,m)
     * */
    public Integer[] shape() {
        Integer[] shape = new Integer[nDim()];

        Object[] current = null;
        for(int i = 0; i < shape.length;i++){

            if(i == 0){
                current = (Object[]) array;
            } else if(i <= shape.length -1){
                for(Object sub : current){
                    if(sub != null){
                        current = (Object[]) sub;
                        break;
                    }
                }
            }

            shape[i] = current.length;

        }
        return shape;
    }


    /**
     * 总共的元素的个数
     * */
    public int size() {
        int dim = nDim();
        Object[] current = new Object[]{};
        for(int i = 0; i < dim; i++){
            Object[] temp = new Object[]{};
            if(i == 0){
                temp = (Object[]) array;
            } else {
                for(int j = 0; j < current.length; j++){
                    Object o = current[j];
                    if(o.getClass().isArray()){
                        Object[] os = (Object[])o;
                        temp = ArraysUtil.join(temp, os);
                    }else {
                        temp = ArraysUtil.join(temp, new Object[]{o});
                    }
                }
            }
            current = temp;
        }
        return current.length;
    }

    /**
     * ar的类型，数值的数据类型
     * */
    public void dType(){

    }

    /**
     * 每个元素的字节大小
     * */
    public void itemSize(){

    }

    // resize在数值不一致时会进行调整，不用像reshape一样数据数量严格对应
    public void reshape(int x, int y, int c){

    }

    // resize在数值不一致时会进行调整，不用像reshape一样数据数量严格对应
    public void resize(int x, int y, int c){

    }

    public void copy(){

    }

    // 数组类型转换 .astype()
    public void asType(){

    }


    // ar=np.arange(6).reshape(2,3)
    //print(ar+10) # 加法
    //print(ar*2) # 减法
    //print(1/(ar+1)) # 除法
    //print(ar**0.5) # 幂
    //# 与标量的运算
    //
    //print(ar.mean()) # 求平均值
    //print(ar.max()) # 求最大值
    //print(ar.min()) # 求最小值
    //print(ar.std()) # 求标准差
    //print(ar.var()) # 求方差
    //print(ar.sum(),np.sum(ar,axis=0))
    //# 求和np.sum()→axis为0，按列求和;axis为1，按行求和
    //print(np.sort(np.array([1,4,3,2,5,6]))) # 排序

    // ar=np.arange(20)
    //print(ar)
    //print(ar[4])
    //print(ar[:3])
    //print(ar[::2])

    // 布尔索引
    // ar=np.arange(12).reshape(3,4)
    //print(ar)
    //i=np.array([True,False,True])
    //j=np.array([True,True,False,True])
    //print(i)
    //print(j)
    //print(ar[i,:]) # 保留第一行与第三行，True的保留
    //print(ar[:,j]) # 保留前两列
    // print(ar>5,type(ar>5)) # 可以得到一个布尔型的数组
    //print(ar[ar>5]) # 选取大于5的数值

    /** 转置 */
    public Num t(){
        return null;
    }

    @Override
    public String toString(){
        return null;
    }
}
