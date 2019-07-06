package com.zifang.demo.temp.tool.utils;

/**
 * Class工具类
 * 
 * @author jijs
 * @version 1.0
 */
public class ClassUtils {

    /**
     * 获取调用的类名
     * 
     * @return String
     */
    public static String getClassName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String className = e.getClassName();
        return className;
    }

    /**
     * 获取调用的方法名
     * 
     * @return String
     */
    public static String getMethodName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        return methodName;
    }
    
    public static String getFileName() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getFileName();
        return methodName;
    }
    
    public static int getLineNumber() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        int line = e.getLineNumber();
        return line;
    }
    
    public static void main(String[] args) {
		System.out.println(getClassName());
		System.out.println(getMethodName());
		System.out.println(getFileName());
		System.out.println(getLineNumber());
	}
}