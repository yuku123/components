package com.zifang.util.core.util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class TerminalUtilTest {


    @Test
    public void test1() throws IOException {
        System.out.println(TerminalUtil.runAndGetReturn("python3 /home/zifang/workplace/idea_workplace/aa.py 1 2"));
    }

    @Test
    public void test2_1() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // String baseFolder = "I:\\书籍合集\\13000本";
        String baseFolder = "I:\\书籍合集\\合集";

        doFolder(baseFolder, executor);
    }

    @Test
    public void test2_2() throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // String baseFolder = "I:\\书籍合集\\精排版";
        String baseFolder = "/Volumes/Elements SE/书籍合集/精排版";

        for(File file : new File(baseFolder).listFiles()){
            if(file.isDirectory()){
                 if(file.getName().contains(" ")){
                     file.renameTo(new File(file.getParent() + "\\" + file.getName().replace(" ","_")));
                 }
            }
        }

        doFolder(baseFolder, executor);
    }

    @Test
    public void test2() throws IOException {


        // 创建一个单线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String baseFolder = "/Volumes/Elements SE/书籍合集/【罗辑思维】1-5季书籍";
        // String baseFolder = "I:\\书籍合集\\【罗辑思维】1-5季书籍";

        doFolder(baseFolder, executor);
    }

    private void doFolder(String baseFolder, ExecutorService executor) {

        for(File file : new File(baseFolder).listFiles()){

            if(file.isDirectory()){
                doFolder(file.getAbsolutePath(), executor);
            } else {

                file = preHandle(file);

                if(file.getName().endsWith(".epub") || file.getName().endsWith(".mobi")){
                    transAndSafeDelete(file);
                }
            }
        }
    }

    private File preHandle(File file) {

        if(file.isFile()){
            if(file.getName().contains(" ")){
                file.renameTo(new File(file.getParent() + "\\" + file.getName().replace(" ","_")));
            }
        }

        return file;
    }

    private void transAndSafeDelete(File file) {
        String originName = file.getAbsolutePath();
        String targetName = file.getAbsolutePath().substring(0, originName.lastIndexOf(".")) +".pdf";


        String command = String.format("ebook-convert \"%s\" \"%s\"", originName, targetName);
        System.out.println(command);

        Process process = null;
        try {
            // TerminalUtil.runAndGetPrint(command,60L);


            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ebook-convert",
                    originName,
                    targetName
            );

            process = processBuilder.start();
            boolean pass = false;
            try {
                pass = process.waitFor(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(!pass){
                throw new RuntimeException();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("timeout: " + command);
            process.destroy();
            return ;
        }

        // 开始check
        File file1  = new File(targetName);
        if(file1.exists() && file1.length() > 1000){
            new File(originName).delete();
        }
    }

}
