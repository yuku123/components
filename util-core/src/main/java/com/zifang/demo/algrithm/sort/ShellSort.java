package com.zifang.demo.algrithm.sort;
/** 
 * 
 * 希尔排序 
 * 希尔排序又叫缩小增量排序，是直接插入排序算法的一种更高效的改进版本 
 * 希尔排序属于不稳定排序 
 * 希尔排序空间复杂度O(1) 
 * 希尔排序的时间复杂度和其增量序列有关系，平均时间复杂度O(n^1.3) 
 *  
 * 基本原理： 
 * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，待整个序列中的记录“基本有序”时，再对全体记录进行依次直接插入排序。 
 */  
public class ShellSort {  
    public void sort(int[] arr){  
        int i, j, tmp, len = arr.length;  
        //希尔排序步长  
        int step = len / 2;  
          
        //缩小步长，直到步长为0  
        while(step > 0){  
            for(i = step; i < len; i++){  
                //如果新记录小于有序序列中的最大记录，则进行直接插入排序  
                if(arr[i] < arr[i - step]){  
                    tmp = arr[i];  
                    j = i - step;  
                    while(j >= 0 && arr[j] > tmp){  
                        arr[j + step] = arr[j];  
                        j -= step;  
                    }  
                    arr[j + step] = tmp;  
                }  
            }  
              
            step /= 2;  
        }  
    }  
}  