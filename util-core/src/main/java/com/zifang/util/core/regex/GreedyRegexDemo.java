package com.zifang.util.core.regex;


/**
 * 实例解析Java程序中正则表达式的贪婪模式匹配
 * 
 * 贪婪模式又叫最大匹配,X?、X*、X+、X{n，}都是最大匹配,例如你要用“<.+>”去匹配“a<tr>aava </tr>abb”,
 * 也许你所期待的结果是想匹配“<tr>”,但是实际结果却会匹配到“<tr>aava </tr>”,下面我们就来看具体看一下贪婪模式的使用.
 * 
 * 贪婪模式(Greedy)：
 *
 *
 * 数量表示符默认采用贪婪模式，除非另有表示。贪婪模式的表达式会一直匹配下去，直到无法匹配为止。
 * 如果你发现表达式匹配的结果与预期的不符，很有可能是因为——你以为表达式只会匹配前面几个字符，而实际上它是贪婪模式，
 * 所以会一直匹配下去。
 * 贪婪与非贪婪，加上?为非贪婪：
 * java 正则表达式默认用的是greedy贪婪匹配模式既是这种类型(.*)的最长匹配，如果需要最短匹配则改为(.*?)即是勉强匹配模式。
 */
public class GreedyRegexDemo {

	/**
	 * 输出结果：
	 * aaa"bbb"ccc"ddd"eee
	 * aaa@eee
	 */
	public void test1(){
		String str = "aaa\"bbb\"ccc\"ddd\"eee"; 
		System.out.println(str);
		str = str.replaceAll("\"(.*)\"", "@"); 
		System.out.println(str); 
	}
	
	/**
	 * 输出结果：
	 * aaa"bbb"ccc"ddd"eee
	 * aaa@ccc@eee
	 */
	public  void test2(){
		String str = "aaa\"bbb\"ccc\"ddd\"eee"; 
		System.out.println(str); 
     
	   str = str.replaceAll("\"(.*?)\"", "@"); 
	   System.out.println(str); 
			     
	}
	
	/**
	 * 原理分析：
	 * 如果是贪婪匹配模式，正则表达式引擎会一直匹配到字符串最后，当匹配为false时，通过回溯的方式，倒退找到倒数第一个匹配位置，
	 * 返回匹配结果如果是勉强匹配模式，正则表达式引擎会匹配到符合pattern的末尾位置那个字符，然后再往后走一步，
	 * 发现匹配为false，又回溯到找到回退的最近一个匹配为true的位置，返回结果。
	 */
	public static void main(String[] args) {
		new GreedyRegexDemo().test1();
		new GreedyRegexDemo().test2();

	}
}
