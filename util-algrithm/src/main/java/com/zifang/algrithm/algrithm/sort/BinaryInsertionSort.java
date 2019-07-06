package com.zifang.algrithm.algrithm.sort;

/** 
 * 二分插入排序是直接插入排序的改进版  
 * 二分插入排序是不稳定排序 
 *  
 * 原理： 
 * 将一个记录插入到已排序好的有序序列中，从而得到一个新，记录数增1的有序序列。 
 * 二分插入排序用二分法找出新记录在有序序列中的位置。 
 */  
public class BinaryInsertionSort {  
    public void sort(int[] arr) {  
        int i, j, left, right, center, tmp, len = arr.length;  
          
        for (i = 1; i < len; i++) {  
              
            //如果新记录小于有序序列的最大元素，则用二分法找出新纪录在有序序列中的位置  
            if (arr[i] < arr[i - 1]) {                 
                left = 0;  
                right = i - 1;                
                while (left < right) {  
                    //获取中间位置索引，把有序序列分成两个子序列  
                    center = (right + left) / 2;  
  
                    if (arr[center] < arr[i]) {  
                        //如果新纪录大于中间位置记录，则在右边序列继续进行二分  
                        left = center + 1;  
                    } else {  
                        //如果新纪录小于中间位置记录，则在左边序列继续进行二分  
                        right = center - 1;  
                    }  
                }  
                  
                tmp = arr[i];  
                  
                //把比arr[i]大的记录往后移  
                for (j = i; j > left; j--) {  
                    arr[j] = arr[j - 1];  
                }  
  
                arr[left] = tmp;  
            }  
        }  
    }  
}  
