package com.zifang.util.algrithm.sort;

/**
 * 冒泡排序：
 * 	参与排序的数据，每两个相邻的数据进行比较，如果前一个数大于后一个数，就进行交换，
 * 	否则不进行交换，然后进行下一轮的比较，直至排序完成。
 * 
 * 冒泡排序法：
 * 	n个数据要进行n-1轮比较，每一轮中每两个相邻数据进行比较，若前者大于后者，则进行交换，否则不进行交换
 * 	本轮比较结束之后，参与本轮比较的最大数排在本轮数据的最后一位。
 * 	第j轮进行比较的次数是n-1-j。
 *
 */
public class BubbleSort {
	
	/**
	 * 第一次(i=1)循环 l-1
	 * 第二次(i=2)循环 l-2
	 * 第三次(i=3)循环 l-3
	 * ......
	 * 最后一次循环 l-i=1次
	 * @param arr
	 * @return
	 */
	public static int[] bubbleSort(int arr[]){
		int m=0;	//循环次数
		int n=0;	//交换次数
		int l = arr.length;
		boolean flag = true;
		for(int i=1; i<l; i++){
			flag = true;
			for (int j = 0; j < l-i; j++) {
				m++;
				if(arr[j] > arr[j+1]){
					n++;
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
					flag = false;
				}
			}
			//判断是否已经排好序，如果已经拍好序直接退出循环。判断是否排好序条件：内层循环都没有发生过交换
			if(flag){
				break;
			}
		}
		System.out.println("循环次数："+ m +"\t交换次数："+ n);
		return arr;
	}

	public static void main(String[] args) {
//		int[] arrays = new int[]{0,1,2,3,8,7,9,6,4,5};
//		int[] arrays = new int[]{0,1,2,3,4,5,6,7,8,9};	//循环9次，交换0次
		int[] arrays = new int[]{9,8,7,6,5,4,3,2,1,0};	//循环45次,交换45次
		
		int[] arrSort = bubbleSort(arrays);
		for (int i = 0; i < arrSort.length; i++) {
			System.out.println(arrSort[i]);
		}
	}
}
