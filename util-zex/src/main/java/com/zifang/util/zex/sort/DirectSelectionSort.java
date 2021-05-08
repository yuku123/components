package com.zifang.util.zex.sort;

/**
 * 直接选择排序
 * 直接选择排序是一种不稳定的排序
 * 时间复杂度为 O(n^2)，当记录占用字节数较多时，直接选择排序通常比直接插入排序的执行速度快些。
 * 空间复杂度为O(1)
 * <p>
 * 原理：从未排序序列中找到最小元素，存放到已排序序列的末尾，以此类推，直到所有元素均排序完毕。
 */
public class DirectSelectionSort {
    public void sort(int[] arr) {
        int i, j, min, tmp, len = arr.length;
        for (i = 0; i < len; i++) {
            min = i;

            //从arr[i]~arr[len-1]中找出最小的记录  
            for (j = i + 1; j < len; j++) {
                if (arr[min] > arr[j]) {
                    min = j;
                }
            }

            //如果最小记录不是arr[i]，把arr[i]和arr[min]交换位置  
            //使得arr[0]~arr[i]是有序序列，而且比arr[i+1]~arr[len-1]中任何记录都小  
            if (min != i) {
                tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
    }
}  