package com.zifang.demo.jdk.java.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 捕获组疑惑了好长时间，终于弄明白了，呵呵，赞一个。
 * 
 * 
 * 捕获组是把多个字符当一个单独单元进行处理的方法，它通过对括号内的字符分组来创建。
 *	例如，正则表达式(dog) 创建了单一分组，组里包含"d"，"o"，和"g"。
 *	捕获组是通过从左至右计算其开括号来编号。例如，在表达式（（A）（B（C））），有四个这样的组：
 *	((A)(B(C)))
 *	(A)
 *	(B(C))
 *	(C)
 *	可以通过调用matcher对象的groupCount方法来查看表达式有多少个分组。groupCount方法返回一个int值，表示matcher对象当前有多个捕获组。
 *	还有一个特殊的组（组0），它总是代表整个表达式。该组不包括在groupCount的返回值中。
 *
 *
 */
public class MatcherGroupDemo {

	public static void test1() {
		String text = "<textarea rows=\"20\" cols=\"70\">nexus maven repository index properties updating index central</textarea>";
		String reg = "<textarea.*?>.*?</textarea>";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(text);
		while (m.find()) {
			System.out.println(m.group());
		}
	}
	
	/**
	 * 捕获组匹配
	 */
	public static void test2(){
        String text = "<textarea rows=\"20\" cols=\"70\">nexus maven repository index properties updating index central</textarea>";  
        //下面的正则表达式中共有四个捕获组：(<textarea.*?>)、(.*?)、(</textarea>)和整个匹配到的内容  
        String reg = "(<textarea.*?>)(.*?)(</textarea>)";    
        Pattern p = Pattern.compile(reg);  
        Matcher m = p.matcher(text);  
        while (m.find()) {  
            System.out.println(m.group(0)); // 整个匹配到的内容  
            System.out.println(m.group(1)); // (<textarea.*?>)  
            System.out.println(m.group(2)); // (.*?)  
            System.out.println(m.group(3)); // (</textarea>)  
        }  
	}
	
	/**
	 * 非捕获组匹配 
	 * 使用  【?:】标识非捕获组
	 */
	public static void test3(){
		String text = "<textarea rows=\"20\" cols=\"70\">nexus maven repository index properties updating index central</textarea>";  
        // 下面的正则表达式中共有二个捕获组：(.*?)和整个匹配到的内容，两个非捕获组:(?:</textarea>)和(?:<textarea.*?>)  
        String reg = "(?:<textarea.*?>)(.*?)(?:</textarea>)";  
        Pattern p = Pattern.compile(reg);  
        Matcher m = p.matcher(text);  
        while (m.find()) {  
            System.out.println(m.group(0)); // 整个匹配到的内容  
            System.out.println(m.group(1)); // (.*?)  
        }  
	}
	
	public static void test4(){
		// 按指定模式在字符串查找
    	String line = "This order was placed for QT3000! OK?";
//    	String pattern = "(.*)(\\d+)(.*)";	//贪婪模式
    	String pattern = "(.*?)(\\d+)(.*)"; //非贪婪模式

    	// 创建 Pattern 对象
    	Pattern r = Pattern.compile(pattern);

    	// 现在创建 matcher 对象
      	Matcher m = r.matcher(line);
      	if (m.find( )) {
      		System.out.println("Found value: " + m.group(0) );
      		System.out.println("Found value: " + m.group(1) );
      		System.out.println("Found value: " + m.group(2) );
      		System.out.println("Found value: " + m.group(3) );
      	} else {
      		System.out.println("NO MATCH");
      	}
	}
	
	public static void main(String[] args) throws Exception {
		/**
		 * 运行结果：
		 *	<textarea rows="20" cols="70">nexus maven repository index properties updating index central</textarea>  
		 */
		test1();
		
		/**
		 * 现在，如果我只想匹配到<textarea>内的文本内容即“nexus maven repository index properties updating index central”，怎么做呢？
		 * 这时候就要用到捕获组了。上述代码中“<textarea.*?>.*?</textarea>”最中间的“.*?”是匹配内容的正则表达式，只需要将它用括号括起来，就是一个捕获组了。
		 */
		test2();
		
		/**
		 * 从上述代码得出结论：正则表达式中每个"()"内的部分算作一个捕获组，每个捕获组都有一个编号，从1,2...，编号0代表整个匹配到的内容。
		 *	至于非捕获组，只需要将捕获组中"()"变为"(?:)"即可，代码说话：
		 */
		test3();
		
		
		/**
		 * 贪婪模式和非贪婪模式
		 */
		test4();
	}
}
