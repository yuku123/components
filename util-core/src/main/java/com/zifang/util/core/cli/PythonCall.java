package com.zifang.util.core.cli;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PythonCall {

    public static void main(String[] args) throws IOException, InterruptedException {
        String exe = "python3";
        String command = "/home/zifang/workplace/idea_workplace/aa.py";
        String num1 = "1";
        String num2 = "2";
        String[] cmdArr = new String[] {exe,command,num1, num2};
        Process process = Runtime.getRuntime().exec(cmdArr);
        InputStream is = process.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String str = dis.readLine();
        process.waitFor();
        System.out.println(str);
    }
}
