package com.zifang.demo.jdk.java.lang;

import java.util.Random;

/**
 * Java位运算是针对于整型数据类型的二进制进行的移位操作。主要包括位与、位或、位非，有符号左移、有符号右移，无符号右移等等。需要注意一点的是，不存在无符号左移<<<运算符。根据位运算的概念规定，我们首先需要弄明白两个问题，java有哪些数据类型是整型数据类型和各数字进制之间转换问题。Java整型数据类型有：byte、char、short、int、long。要把它们转换成二进制的原码形式，必须明白他们各占几个字节。我们都知道，一个字节占8位。
 * 	数据类型                           所占位数
 * 	byte         8 
 *  boolean      8
 *  short        16
 *  int          32 
 *  long         64 
 *  float        32 
 *  double       64 
 *  char         16
 * 还需要明白一点的是：计算机表示数字正负不是用+ -加减号来表示，而是用最高位数字来表示，0表示正，1表示负
 * 所以比如-4用二进制原码表示就是1111 1111 1111 1111 1111 1111 1111 1100
 *
 *
 */
public class BitwiseDemo {

	/**
	 * 下面根据实例一个一个的来说明各种位运算的运算规则：
	 * 	位与&(真真为真 真假为假 假假为假)
	 * 	4&6
	 * 	0000 0000 0000 0000 0000 0000 0000 0100
	 * 	0000 0000 0000 0000 0000 0000 0000 0110
	 * 	0000 0000 0000 0000 0000 0000 0000 0100
	 * 	结果：4
	 */
	public static void test1(){
		System.out.println(Integer.toBinaryString(4));
		System.out.println(Integer.toBinaryString(6));
		System.out.println(4&6);
	}
	
	/**
	 * 位或|(真真为真 真假为真 假假为假)
	 * 	4|6
	 * 	0000 0000 0000 0000 0000 0000 0000 0100
	 * 	0000 0000 0000 0000 0000 0000 0000 0110
	 * 	0000 0000 0000 0000 0000 0000 0000 0110
	 * 	结果：6
	 */
	public static void test2(){
		System.out.println(Integer.toBinaryString(4));
		System.out.println(Integer.toBinaryString(6));
		System.out.println(4|6);
	}
	
	/**
	 * 位非~(取反码)【注：Java中正数的最高位为0，负数最高位为1，即最高位决定正负符号】
	 * 	~4
	 * 	0000 0000 0000 0000 0000 0000 0000 0100
	 * 	1111 1111 1111 1111 1111 1111 1111 1011
	 * 	解码：先取反码，再补码
	 * 	0000 0000 0000 0000 0000 0000 0000 0100
	 * 	0000 0000 0000 0000 0000 0000 0000 0101
	 * 	结果：-5
	 */
	public static void test3(){
		System.out.println(Integer.toBinaryString(4));
		System.out.println(Integer.toBinaryString(~4));
		System.out.println(~4);
	}
	
	/**
	 * 位异或^(真真为假 真假为真 假假为假)
	 * 	4^6
	 * 	0000 0000 0000 0000 0000 0000 0000 0100
	 * 	0000 0000 0000 0000 0000 0000 0000 0110
	 * 	0000 0000 0000 0000 0000 0000 0000 0010
	 * 	结果：2
	 */
	public static void test4(){
		System.out.println(Integer.toBinaryString(4));
		System.out.println(Integer.toBinaryString(6));
		System.out.println(4^6);
	}
	
	/**
	 * 有符号右移>>(若正数,高位补0,负数,高位补1)
	 * 	-4>>2
	 * 	1111 1111 1111 1111 1111 1111 1111 1100   原码
	 * 	1111 1111 1111 1111 1111 1111 1111 1111   右移，最左边空出两位按规则负数空位补1
	 * 	0000 0000 0000 0000 0000 0000 0000 0000   解码
	 * 	0000 0000 0000 0000 0000 0000 0000 0001   补码(补码即最后一位+1)
	 * 	结果：-1
	 */
	public static void test5(){
		System.out.println(Integer.toBinaryString(-4));
		System.out.println(Integer.toBinaryString(2));
		System.out.println(-4>>2);
	}
	
	/**
	 * 有符号左移<<(若正数,高位补0,负数,高位补1)
	 * 	-4<<2
	 * 	1111 1111 1111 1111 1111 1111 1111 1100   原码
	 * 	1111 1111 1111 1111 1111 1111 1111 0000   左移，最右边空出两位补0
	 * 	0000 0000 0000 0000 0000 0000 0000 1111   解码
	 * 	0000 0000 0000 0000 0000 0000 0001 0000   补码
	 * 	结果：-16
	 */
	public static void test6(){
		System.out.println(Integer.toBinaryString(-4));
		System.out.println(Integer.toBinaryString(2));
		System.out.println(-4<<2);
	}

	/**
	 * 无符号右移>>>(不论正负,高位均补0)
	 * -4>>>2
	 * 1111 1111 1111 1111 1111 1111 1111 1100   原码
	 * 0011 1111 1111 1111 1111 1111 1111 1111   右移(由于高位均补0，故>>>后的结果一定是正数)
	 * 结果：1073741823
	 */
	public static void test7(){
		System.out.println(Integer.toBinaryString(-4));
		System.out.println(Integer.toBinaryString(2));
		System.out.println(-4>>>2);
	}

	/**
	 * 1.  判断int型变量a是奇数还是偶数    
	 *      a&1  = 0 偶数 
	 *      a&1 =  1 奇数 
	 */
	public static void demo1(){
		int num = new Random().nextInt(100);
		if((num&1)==0){
			System.out.println("偶数:"+num);
		}else{
			System.out.println("奇数:"+num);
		}
	}
	
	/**
	 * 2.  求平均值，比如有两个int类型变量x、y,首先要求x+y的和，再除以2，但是有可能x+y的结果会超过int的最大表示范围，所以位运算就派上用场啦。
	 *       (x&y)+((x^y)>>1); 
	 */
	public static void demo2(){
		int x = new Random().nextInt(100);
		int y = new Random().nextInt(100);
		System.out.println("x="+x);
		System.out.println("y="+y);
		System.out.println("平均数为："+(x&y)+((x^y)>>1));
	}
	
	/**
	 * 3.  对于一个大于0的整数，判断它是不是2的几次方
	 *     ((x&(x-1))==0)&&(x!=0)； 
	 */
	public static void demo3(){
		int x = new Random().nextInt(100);
		boolean flag = ((x&(x-1))==0)&&(x!=0);
		System.out.println(x+"判断它是不是2的几次方:"+flag);
	}
	
	/**
	 * 4.  比如有两个int类型变量x、y,要求两者数字交换，位运算的实现方法：性能绝对高效
	 *     x ^= y; 
	 *     y ^= x; 
	 *     x ^= y; 
	 */
	public static void demo4(){
		int x = new Random().nextInt(100);
		int y = new Random().nextInt(100);
		System.out.println("---------------交换前-----------------");
		System.out.println("x="+x);
		System.out.println("y="+y);
		x ^= y; 
		y ^= x; 
		x ^= y;
		System.out.println("---------------交换后-----------------");
		System.out.println("x="+x);
		System.out.println("y="+y);
	}
	
	/**
	 * 5. 求绝对值
	 *     int abs( int x ) 
	 *     { 
	 *         int y ; 
	 *         y = x >> 31 ; 
	 *         return (x^y)-y ;        //or: (x+y)^y 
	 *     }
	 */
	public static void demo5(){
		int x = -100;
		int y = 0;
		y= x >> 31;
		int result = (x^y)-y;
		System.out.println(x+"的绝对值为："+result);
		
	}
	
	/**
	 * 6.  取模运算，采用位运算实现：
	 *      a % (2^n) 等价于 a & (2^n - 1) 
	 */
	public static void demo6(){
		
	}
	
	/**
	 * 7.  乘法运算   采用位运算实现
	 *      a * (2^n) 等价于 a << n
	 */
	public static void demo7(){
		
	}
	
	/**
	 * 8.   除法运算转化成位运算
	 *       a / (2^n) 等价于 a>> n 
	 */
	public static void demo8(){
		
	}
	
	/**
	 * 9.   求相反数
	 *       (~x+1) 
	 */
	public static void demo9(){
		
	}
	
	/**
	 * 10  a % 2 等价于 a & 1 
	 */
	public static void demo10(){
		
	}

	public void f1() {
		byte n = 3;
		if ((n & 1) == 1) {
			// n 是个奇数。
		}

	}

	public void f2() {
		/**
		 *
		 *
		 * 把（1）中的 x 带入 （2）中的 x，有
		 *
		 * y = x^y = (x^y)^y = x^(y^y) = x^0 = x。 x 的值成功赋给了 y。
		 *
		 * 对于（3）,推导如下：
		 *
		 * x = x^y = (x^y)^x = (x^x)^y = 0^y = y。
		 * */
		int x = 2;
		int y = 5;
		x = x ^ y;   // （1）
		y = x ^ y;   // （2）
		x = x ^ y;   // （3）

	}

	//1^2^3^4^5^1^2^3^4 = （1^1)^(2^2)^(3^3)^(4^4)^5= 0^0^0^0^5 = 5。
	int find(int[] arr) {
		int tmp = arr[0];
		for (int i = 1; i < arr.length; i++) {
			tmp = tmp ^ arr[i];
		}
		return tmp;
	}

	//3的n次方
	int pow(int n) {
		int sum = 1;
		int tmp = 3;
		while (n != 0) {
			if ((n & 1) == 1) {
				sum *= tmp;
			}
			tmp *= tmp;
			n = n >> 1;
		}

		return sum;
	}

	//找出不大于N的最大的2的幂指数
	int findN(int n){
		n |= n >> 1;
		n |= n >> 2;
		n |= n >> 4;
		n |= n >> 8; // 整型一般是 32 位，上面我是假设 8 位。
		return (n + 1) >> 1;
	}
	public static void main(String[] args) {
		//位与&(真真为真 真假为假 假假为假)
//		test1.groovy();
		//位或|(真真为真 真假为真 假假为假)
//		test2();
		//位非~(取反码)【注：Java中正数的最高位为0，负数最高位为1，即最高位决定正负符号】
//		test3();
		//位异或^(真真为假 真假为真 假假为假)
//		test4();
		//有符号右移>>(若正数,高位补0,负数,高位补1)
//		test5();
		//有符号左移<<(若正数,高位补0,负数,高位补1)
//		test6();
		//无符号右移>>>(不论正负,高位均补0)
//		test7();
		/**
		 * 学到这里，我想你也可能会问，位运算到底有什么用途或者有哪些场景可以应用到它。因为位运算的运算效率比直接对数字进行加减乘除高很多，所以当出现以下情景且对运算效率要求较高时，可以考虑使用位运算。不过实际工作中，很少用到它，我也不知道为什么很少有人用它，我想应该是它比较晦涩难懂，如果用它来进行一些运算，估计编写的代码的可读性会不强，毕竟我们写的代码不仅仅留给自己一个人看。
		 */
		
		//判断奇数偶数
		demo1();
		
		//求平均值
		demo2();
		
		//判断它是不是2的几次方
		demo3();
		
		//两数交换位置
		demo4();
		
		//求绝对值
		demo5();
		
		//...
	}
}
/**
 * 由于数据类型所占字节是有限的，而位移的大小却可以任意大小，所以可能存在位移后超过了该数据类型的表示范围，于是有了这样的规定：
 * 如果为int数据类型，且位移位数大于32位，则首先把位移位数对32取模，不然位移超过总位数没意义的。所以4>>32与4>>0是等价的。
 * 如果为long类型，且位移位数大于64位，则首先把位移位数对64取模，若没超过64位则不用对位数取模。
 * 如果为byte、char、short，则会首先将他们扩充到32位，然后的规则就按照int类型来处理。
 */