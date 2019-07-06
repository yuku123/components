package com.zifang.demo.algrithm.sort;

import java.util.Stack;

/** 
 * 快速排序 
 * 快速排序是不稳定排序 
 * 快速排序是一种交换排序 
 * 快速排序对序列的操作空间复杂度为O(1)，如果快速排序用递归实现，则递归栈的空间复杂度为O(logn)~O(n)之间。 
 * 最佳时间复杂度O(nlogn) 
 * 平均时间复杂度O(nlogn)  
 * 快速排序是目前基于比较的内部排序中被认为是最好的方法，当待排序的关键字是随机分布时，快速排序的平均时间最短。 
 *  
 * 快速排序原理： 
 * 通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小， 
 * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。 
 */  
public class QuickSort {  
    public void sort(int[] arr){  
        int start = 0, end = arr.length - 1;  
        sort(arr, start, end);  
    }  
      
    /** 
     * 快速排序 
     * @param arr 待排序数组 
     * @param start 排序开始索引 
     * @param end 排序结束索引 
     */  
    public void sort(int[] arr, int start, int end){  
        int split;  
        if(start < end){  
            split = partition(arr, start, end);  
            sort(arr, start, split - 1);  
            sort(arr, split + 1, end);  
        }  
    }  
      
    /** 
     * 对序列进行排序 
     * @param arr 待排序数组 
     * @param start 排序开始索引 
     * @param end 排序结束索引 
     * @return 返回用于比较的基准元素的索引 
     */  
    private int partition(int[] arr, int start, int end){  
        int privot = arr[start];  
          
        while(start < end){  
            while(start < end && arr[end] >= privot){  
                end--;  
            }  
              
            swap(arr, start, end);  
              
            while(start < end && arr[start] <= privot){  
                start++;  
            }  
              
            swap(arr, start, end);  
        }  
          
        return start;  
    }  
      
    private void swap(int[] arr, int i, int j){  
        int tmp = arr[i];  
        arr[i] = arr[j];  
        arr[j] = tmp;  
    }  
      
    /** 
     * 非递归快速排序 
     * 非递归快速排序就是用栈来模拟递归方法保存快速排序的边界 
     * 用栈模拟递归实现快速排序比递归快速排序慢 
     * @param arr 
     */  
    public void sortStack(int[] arr){  
        Stack<Integer> stack = new Stack<Integer>();  
        stack.push(0);  
        stack.push(arr.length - 1);  
          
        int start, end, index;  
          
        while(!stack.empty()){  
            end = stack.pop();  
            start = stack.pop();  
            index = partition(arr, start, end);  
              
            if(start < index - 1){  
                stack.push(start);  
                stack.push(index - 1);  
            }  
              
            if(end > index + 1){  
                stack.push(index + 1);  
                stack.push(end);  
            }  
        }  
    }  
}  