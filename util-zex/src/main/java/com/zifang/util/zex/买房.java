package com.zifang.util.zex;

public class 买房 {
    public static void main(String[] args) {
        int[] 幢们 = new int[]{1,3,9};
        int[] 单元们 = new int[]{1,2,3,4};
        int[] 层们 = new int[]{1,2,3,4,5,6};
        int[] 号们 = new int[]{1,2};


        for(int 幢 : 幢们){
            for(int 单元: 单元们){
                if(幢 == 9 || 幢 == 3){
                    if(单元 == 4){
                        continue;
                    }
                }
                for(int 层: 层们){
                    for(int 号: 号们){
                        System.out.println(幢+" "+单元+" "+ 层+" "+号);
                    }
                }
            }
        }
    }
}
