package com.zifang.util.core.lang;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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
 */

/**
 * 由于数据类型所占字节是有限的，而位移的大小却可以任意大小，所以可能存在位移后超过了该数据类型的表示范围，于是有了这样的规定：
 * 如果为int数据类型，且位移位数大于32位，则首先把位移位数对32取模，不然位移超过总位数没意义的。所以4>>32与4>>0是等价的。
 * 如果为long类型，且位移位数大于64位，则首先把位移位数对64取模，若没超过64位则不用对位数取模。
 * 如果为byte、char、short，则会首先将他们扩充到32位，然后的规则就按照int类型来处理。
 */
public class Bits {

    static final int MAXIMUM_CAPACITY = 1 << 30;


    public String bitArray(int i){
        return Integer.toBinaryString(i);
    }

    public String bitArray(long i){
        return Long.toBinaryString(i);
    }

    public String bitArray(float i){
        return Float.toHexString(i);
    }

    public String bitArray(double i){
        return Double.toHexString(i);
    }


    /**
     * long 转 byte数组
     */
    public static byte[] long2Bytes(long longa) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (long) (((longa) >> i * 8) & 0xff); // 移位和清零
        return bytes;
    }

    /**
     * 1. 字节转10进制
     * 	直接使用(int)类型转换。
     */
    public static int byte2Int(byte b) {
        int r = (int) b;
        return r;
    }

    /**
     * 2. 10进制转字节
     *  直接使用(byte)类型转换。
     */
    public static byte int2Byte(int i) {
        byte r = (byte) i;
        return r;
    }

    /**
     * 3. 字节数组转16进制字符串
     * 对每一个字节，先和0xFF做与运算，然后使用Integer.toHexString()函数，如果结果只有1位，需要在前面加0。
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    /**
     * 4. 16进制字符串转字节数组
     * 这个比较复杂，每一个16进制字符是4bit，一个字节是8bit，所以两个16进制字符转换成1个字节，对于第1个字符，
     * 转换成byte以后左移4位，然后和第2个字符的byte做或运算，这样就把两个字符转换为1个字节。
     *
     * @param hex
     * @return
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 5. 字节数组转字符串
     *  直接使用new String()。
     *
     * @param b
     * @return
     * @throws Exception
     */
    public static String bytes2String(byte[] b) throws Exception {
        String r = new String(b, StandardCharsets.UTF_8);
        return r;
    }

    /**
     * 6. 字符串转字节数组 直接使用getBytes()。
     *
     * @param s
     * @return
     */
    public static byte[] string2Bytes(String s) {
        byte[] r = s.getBytes();
        return r;
    }

    /**
     * 7. 16进制字符串转字符串
     *  先转换成byte[]，再转换成字符串。
     *
     * @param hex
     * @return
     * @throws Exception
     */
    public static String hex2String(String hex) throws Exception {
        String r = bytes2String(hexString2Bytes(hex));
        return r;
    }

    /**
     * 8. 字符串转16进制字符串
     *  先转换为byte[]，再转换为16进制字符串。
     *
     * @param s
     * @return
     * @throws Exception
     */
    public static String string2HexString(String s) throws Exception {
        String r = bytes2HexString(string2Bytes(s));
        return r;
    }

    /**
     * 下面根据实例一个一个的来说明各种位运算的运算规则：
     * 	位与&(真真为真 真假为假 假假为假)
     * 	4&6
     * 	0000 0000 0000 0000 0000 0000 0000 0100
     * 	0000 0000 0000 0000 0000 0000 0000 0110
     * 	0000 0000 0000 0000 0000 0000 0000 0100
     * 	结果：4
     */
    @Test
    public void test1(){
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
     * 位异或^(真真为假 真假为真 假假为假)  只有不一样才能为真
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

    public static boolean isOdd(int i){
        return (i&1)!=0;
    }
    public static boolean isEven(int i){
        return !isOdd(i);
    }

    public static int avg(int x,int y){
        return (x&y)+((x^y)>>1);
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

//    public static void main(String[] args) throws Exception {
//        byte b1 = (byte) 45;
//        System.out.println("1.字节转10进制:" + byte2Int(b1));
//
//        int i = 89;
//        System.out.println("2.10进制转字节:" + int2Byte(i));
//
//        byte[] b2 = new byte[] { (byte) 0xFF, (byte) 0x5F, (byte) 0x6, (byte) 0x5A };
//        System.out.println("3.字节数组转16进制字符串:" + bytes2HexString(b2));
//
//        String s1 = new String("1DA47C");
//        System.out.println("4.16进制字符串转字节数组:" + Arrays.toString(hexString2Bytes(s1)));
//
//        System.out.println("5.字节数组转字符串:" + bytes2String(b2));
//
//        System.out.println("6.字符串转字节数组:" + Arrays.toString(string2Bytes(s1)));
//
//        System.out.println("7.16进制字符串转字符串:" + hex2String(s1));
//
//        String s2 = new String("Hello!");
//        System.out.println("8.字符串转16进制字符串:" + string2HexString(s2));
//
//
//
//        //位与&(真真为真 真假为假 假假为假)
//        test1();
//        //位或|(真真为真 真假为真 假假为假)
////		test2();
//        //位非~(取反码)【注：Java中正数的最高位为0，负数最高位为1，即最高位决定正负符号】
////		test3();
//        //位异或^(真真为假 真假为真 假假为假)
////		test4();
//        //有符号右移>>(若正数,高位补0,负数,高位补1)
////		test5();
//        //有符号左移<<(若正数,高位补0,负数,高位补1)
////		test6();
//        //无符号右移>>>(不论正负,高位均补0)
////		test7();
//
//        //判断奇数偶数
//        demo1();
//
//        //求平均值
//        demo2();
//
//        //判断它是不是2的几次方
//        demo3();
//
//        //两数交换位置
//        demo4();
//
//        //求绝对值
//        demo5();
//
//        //...
//    }

    /**
     * hashMap中拿到的源码，获得cap以上，最接近cap的2的倍数
     * */
    static final int tableSizeFor(int cap) {
        // 扩容门槛为传入的初始容量往上取最近的2的n次方
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
