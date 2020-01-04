package com.zifang.util.core.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerminalUtil {

    /**
     * @param cmd  the command that include pipeline executions such as >
     * */
    public static void runCommandAsShScript(String cmd){
        List<String> list = new ArrayList<>();
        list.add("sh");
        list.add("-c");
        list.add(cmd);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(list);
            Process process = processBuilder.start();
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runExec(String []cmdarray) {
        System.out.println(Arrays.toString(cmdarray));
        Process pro1;
        try {
            pro1 = Runtime.getRuntime().exec(cmdarray,null);
            pro1.waitFor();
            pro1.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param command let os to execute the command and return the result of command
     *                if command do print something
     *
     * @return String the result-print-out that when you execute in os
     * */
    public static String runAndGetReturn(String command) throws IOException {
        StringBuffer sb = new StringBuffer();
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line+"\n");
        }
        return sb.toString();
    }

    /**
     * @param command let os to execute the command and return the result of command
     *                if command do print something
     *
     * @return String the result-print-out that when you execute in os,and in java console it will print out
     * */
    public static String runAndPrintLog(String command) throws IOException {
        StringBuffer sb = new StringBuffer();
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line+"\n");
            System.out.println(line+"\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(TerminalUtil.runAndGetReturn("python3 /home/zifang/workplace/idea_workplace/aa.py 1 2"));
    }
}
