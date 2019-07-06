package com.zifang.util.algrithm.sort;
/** 
 * 直接插入排序 
 * 直接插入排序属于稳定排序 
 * 直接插入排序空间复杂度O(1) 
 * 最优时间复杂度O(n)，当待排序的数组已经排序好时，直接插入排序的时间复杂度为O(n) 
 * 最坏时间复杂度O(n^2)，当待排序的数组是倒序时，直接插入排序的时间复杂度为O(n^2) 
 * 直接插入排序适用于数量比较少的数组排序 
 *  
 * 基本原理： 
 * 将一个记录插入到已排序好的有序序列中，从而得到一个新，记录数增1的有序序列。即：先将序列的第1个记录看成是一个有序的子序列，然后从第2个记录逐个进行插入，直至整个序列有序为止。 
 */  
public class DirectInsertionSort {  
    public void sort(int[] arr){  
        int i, j, tmp;  
  
        //从i=1开始遍历，a[0]为有序序列  
        for(i = 1; i < arr.length; i++){              
            for(j = i - 1; j >= 0; j--){  
                if(arr[j] <= arr[j + 1]){  
                    break;  
                }  
  
                tmp = arr[j];  
                arr[j] = arr[j + 1];  
                arr[j + 1] = tmp;  
            }  
        }  
    }  
} 