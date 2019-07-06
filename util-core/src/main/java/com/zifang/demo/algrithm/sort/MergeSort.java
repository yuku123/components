package com.zifang.demo.algrithm.sort;
/** 
 * 归并排序是建立在归并操作上的一种有效的排序算法,该算法是采用分治法将已有序的子序列合并，最后得到完全有序的序列。 
 * 归并排序是稳定排序，速度仅次于快速排序 
 * 时间复杂度为O(nlogn) 
 * 空间复杂度为O(n) 归并排序需要n空间的临时数组来存储子序列 
 * 归并排序原理： 
 * 将待排序序列分为若干个子序列，，对每个子序列进行排序。 
 * 然后再把相邻的两个有序子序列合并，并排序成为新的有序子序列。 
 * 依次类推，最终把所有子序列合并成一个有序序列。 
 *  
 */  
public class MergeSort {  
    public void sort(int[] arr){  
        sort(arr, 0, arr.length - 1);  
    }  
      
    private void sort(int[] arr, int start, int end){  
        if(start >= end){  
            return;  
        }  
          
        int middle = (start + end) / 2;  
        sort(arr, start, middle);  
        sort(arr, middle + 1, end);  
        merge(arr, start, middle, end);  
    }  
      
    /** 
     * 有序的相邻子序列合并 
     * @param arr 数组 
     * @param start 左边序列开始索引 
     * @param middle 左边序列结束索引，middle+1是右边序列开始索引 
     * @param end 右边序列结束索引 
     */  
    private void merge(int[] arr, int start, int middle, int end){  
        //临时数组长度  
        int tmplen = end - start + 1;  
          
        //临时数组  
        int[] tmp = new int[tmplen];  
          
        //左边数组的索引  
        int left = start;  
          
        //右边数组的索引  
        int right = middle + 1;  
          
        //tmp数组的索引  
        int i = 0;  
          
        while(left <= middle && right <= end){  
            if(arr[left] < arr[right]){  
                tmp[i] = arr[left];  
                left++;  
            }else{  
                tmp[i] = arr[right];  
                right++;  
            }  
              
            i++;  
        }  
          
        while(left <= middle){  
            tmp[i] = arr[left];  
            left++;  
            i++;  
        }  
          
        while(right <= end){  
            tmp[i] = arr[right];  
            right++;  
            i++;  
        }  
          
        for(i = 0; i < tmplen; i++){  
            arr[start + i] = tmp[i];  
        }  
    }  
}  