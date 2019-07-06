package com.zifang.util.core.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TerminalUtil {

    public static void run(String cmd){
        List list = new ArrayList<>();
        list.add("sh");
        list.add("-c");
        list.add(cmd);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(list);
            Process process = processBuilder.start();
            process.waitFor();
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String runAndGetReturn(String cmd) throws IOException {
        StringBuffer sb = new StringBuffer();
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line+"\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(TerminalUtil.runAndGetReturn("ls -la"));
    }
}
