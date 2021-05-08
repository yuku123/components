package com.zifang.util.zex.sort;

/**
 * 堆排序 堆排序是一种树形选择排序，是直接选择排序的有效改进，是不稳定的排序
 * 最优时间复杂度O(nlogn)
 * 最坏时间复杂度O(nlogn)
 * 空间复杂度O(1)
 * <p>
 * 堆排序原理：
 * 1、根据序列建立大根堆（或者小根堆）。
 * 2、将堆顶元素R[1]与最后一个元素R[n]交换，得到新的无序区(R1,R2,......Rn-1)和新的有序区(Rn),且满足R[1,2...n-1]<=R[n]。
 * 3、对当前无序区(R1,R2,......Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，
 * 得到新的无序区(R1,R2....Rn-2)和新的有序区(Rn-1,Rn)。 不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成。
 */
public class HeapSort {

    public void sort(int[] arr) {
        int i, len = arr.length;

        //创建大根堆  
        for (i = (len - 1) / 2; i >= 0; i--) {
            adjuestMaxHeap(arr, i, len);
        }

        for (i = len - 1; i > 0; i--) {
            //把大根堆的顶部元素移到arr[i]处，使得arr[i] ~ arr[len - 1]为有序序列  
            swap(arr, 0, i);

            //调整大根堆  
            //大根堆的在数组中的位置为0 ~ (i - 1)  
            adjuestMaxHeap(arr, 0, i);
        }
    }

    /**
     * 调整大根堆
     *
     * @param arr  待排序数组
     * @param i    父节点在数组中的索引
     * @param size 调整堆的长度
     */
    private void adjuestMaxHeap(int[] arr, int i, int size) {
        // 左孩子在数组中的索引  
        int lchild = 2 * i + 1;

        // 右孩子在数组中的索引  
        int rchild = lchild + 1;

        // 临时变量，最大值索引  
        int max = i;

        while (lchild < size) {
            if (arr[lchild] > arr[max]) {
                max = lchild;
            }

            if (rchild < size && arr[rchild] > arr[max]) {
                max = rchild;
            }

            if (max != i) {
                swap(arr, max, i);

                i = max;
                lchild = 2 * i + 1;
                rchild = lchild + 1;
            } else {
                break;
            }
        }
    }

    /**
     * 交换数组中的两个元素
     *
     * @param arr 数组
     * @param i   元素索引
     * @param j   元素索引
     */
    private void swap(int[] arr, int i, int j) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }
}  