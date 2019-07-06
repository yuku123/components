package com.zifang.util.core.demo.jdk.java.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 索引方法
 *	索引方法提供了有用的索引值，精确表明输入字符串中在哪能找到匹配：
 *	1.	public int start() 
 *		返回以前匹配的初始索引。
 *	2.	public int start(int group)
 *	 	返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引
 *	3.	public int end()
 *		返回最后匹配字符之后的偏移量。
 *	4.	public int end(int group)
 *		返回在以前的匹配操作期间，由给定组所捕获子序列的最后字符之后的偏移量。
 *
 * 查找方法
 *	查找方法用来检查输入字符串并返回一个布尔值，表示是否找到该模式：
 *	1	public boolean lookingAt() 
 *		尝试将从区域开头开始的输入序列与该模式匹配。
 *	2	public boolean find() 
 *		尝试查找与该模式匹配的输入序列的下一个子序列。
 *	3	public boolean find(int start）
 *		重置此匹配器，然后尝试查找匹配该模式、从指定索引开始的输入序列的下一个子序列。
 *	4	public boolean matches() 
 *		尝试将整个区域与模式匹配。
 * 替换方法
 *	替换方法是替换输入字符串里文本的方法：
 *	1	public Matcher appendReplacement(StringBuffer sb, String replacement)
 *		实现非终端添加和替换步骤。
 *	2	public StringBuffer appendTail(StringBuffer sb)
 *		实现终端添加和替换步骤。
 *	3	public String replaceAll(String replacement) 
 *		替换模式与给定替换字符串相匹配的输入序列的每个子序列。
 *	4	public String replaceFirst(String replacement)
 *		替换模式与给定替换字符串匹配的输入序列的第一个子序列。
 *	5	public static String quoteReplacement(String s)
 *		返回指定字符串的字面替换字符串。这个方法返回一个字符串，就像传递给Matcher类的appendReplacement 方法一个字面字符串一样工作。
 *
 *
 */
public class MatcherDemo {
	
	/**
	 * 1.索引方法
	 * start 和 end 方法
	 * 下面是一个对单词"cat"出现在输入字符串中出现次数进行计数的例子：
	 */
	public static void test1(){
		String REGEX = "\\bcat\\b";
	    String INPUT = "cat cat cat cattie cat";
		Pattern p = Pattern.compile(REGEX);
	    Matcher m = p.matcher(INPUT); // 获取 matcher 对象
	    int count = 0;

	    while(m.find()) {
	         count++;
	         System.out.println("Match number "+count);
	         System.out.println("start(): "+m.start());
	         System.out.println("end(): "+m.end());
	    }
	}
	
	/**
	 * 2、查找方法
	 * matches 和lookingAt 方法
	 * matches 和lookingAt 方法都用来尝试匹配一个输入序列模式。它们的不同是matcher要求整个序列都匹配，而lookingAt 不要求。
	 * 这两个方法经常在输入字符串的开始使用。
	 * 我们通过下面这个例子，来解释这个功能：
	 */
	public static void test2(){
		 String REGEX = "foo";
		 String INPUT = "fooooooooooooooooo";
		 Pattern pattern;
		 Matcher matcher;

		 pattern = Pattern.compile(REGEX);
		 matcher = pattern.matcher(INPUT);

		 System.out.println("Current REGEX is: "+REGEX);
		 System.out.println("Current INPUT is: "+INPUT);

		 System.out.println("lookingAt(): "+matcher.lookingAt());
		 System.out.println("matches(): "+matcher.matches());
	}
	
	
	/**
	 * 3、替换方法
	 * replaceFirst 和replaceAll 方法
	 * replaceFirst 和replaceAll 方法用来替换匹配正则表达式的文本。不同的是，replaceFirst 替换首次匹配，replaceAll 替换所有匹配。
	 * 下面的例子来解释这个功能：
	 */
	public static void test3(){
		String REGEX = "dog";
	    String INPUT = "The dog says meow. All dogs say meow.";
	    String REPLACE = "cat";

	    Pattern p = Pattern.compile(REGEX);
	    // get a matcher object
	    Matcher m = p.matcher(INPUT); 
	    INPUT = m.replaceAll(REPLACE);
//	    INPUT = m.replaceFirst(REPLACE);
	    System.out.println(INPUT);
	}
	
	/**
	 * 4、替换方法
	 * appendReplacement 和 appendTail 方法
	 * Matcher 类也提供了appendReplacement 和appendTail 方法用于文本替换：
	 * 看下面的例子来解释这个功能：
	 */
	public static void test4(){
		String REGEX = "a*b";
		String INPUT = "aabfooaabfooAbfoob";
		String REPLACE = "-";
		//设置大小写不敏感
		Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
		// 获取 matcher 对象
		Matcher m = p.matcher(INPUT);
		StringBuffer sb = new StringBuffer();
		while(m.find()){
			System.out.println(m.group());
			m.appendReplacement(sb,REPLACE);
		}
		m.appendTail(sb);
		System.out.println(sb.toString());
	}
	
	
	public static void main(String[] args) {
		/**
		 * 可以看到这个例子是使用单词边界，以确保字母 "c" "a" "t" 并非仅是一个较长的词的子串。它也提供了一些关于输入字符串中匹配发生位置的有用信息。
		 *	Start方法返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引，end方法最后一个匹配字符的索引加1。
		 */
		test1();
		
		//matches 和lookingAt 方法
		test2();
		
		//replaceFirst 和replaceAll 方法
		test3();
		
		//appendReplacement 和 appendTail 方法
		test4();
	}
}
