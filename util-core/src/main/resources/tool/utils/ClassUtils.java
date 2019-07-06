package com.zifang.util.core.demo.temp.tool.utils;

/**
 * Class������
 * 
 * @author jijs
 * @version 1.0
 */
public class ClassUtils {

    /**
     * ��ȡ���õ�����
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
     * ��ȡ���õķ�����
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