package com.zifang.util.zex.demo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class replace {

    public static void main(String[] args) {
        Pattern p = Pattern.compile("(\\$\\{)([\\w]+)(\\})");
        Matcher m = p.matcher("one cat ${two} cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String group = m.group(2);//规则中${值}中的 值 一样 的数据不
            System.out.println("符合规则中第二个的值"+group);
            //下一步是替换并且把替换好的值放到sb中
            m.appendReplacement(sb, "dog");
        }
        //把符合的数据追加到sb尾
        m.appendTail(sb);
        System.out.println(sb.toString());


 //下边是从其他人的理解
 //https://www.cnblogs.com/jsStudyjj/p/6145623.html


        String str = "Hello,World! in Java.";
        Pattern pattern = Pattern.compile("W(or)(ld!)");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            System.out.println("Group 0:"+matcher.group(0));//得到第0组——整个匹配
            System.out.println("Group 1:"+matcher.group(1));//得到第一组匹配——与(or)匹配的,str中有和我的规则中的第一个括号中的数据一样的
            System.out.println("Group 2:"+matcher.group(2));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式,str中有和我的规则中的第二个括号中的数据一样的
            System.out.println("Start 0:"+matcher.start(0)+" End 0:"+matcher.end(0));//总匹配的索引
            System.out.println("Start 1:"+matcher.start(1)+" End 1:"+matcher.end(1));//第一组匹配的索引
            System.out.println("Start 2:"+matcher.start(2)+" End 2:"+matcher.end(2));//第二组匹配的索引
            System.out.println(str.substring(matcher.start(0),matcher.end(1)));//从总匹配开始索引到第1组匹配的结束索引之间子串——Wor
        }
    }
}