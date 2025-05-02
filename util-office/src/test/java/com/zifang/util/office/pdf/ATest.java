package com.zifang.util.office.pdf;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ATest {
    @Test
    public void test(){
        String baseFolder ="/Volumes/Elements SE/out/test";
        String targetFolder = "/Volumes/Elements SE/out/test_output";

        List<String> existedFiles = new ArrayList<>();
        for(File file : new File(targetFolder).listFiles()){
            existedFiles.add(file.getName().replace(".epub.epub.pdf",""));
        }

        for(File file : new File(baseFolder).listFiles()){
            String fileName = file.getName().replace(".epub","");
            if(existedFiles.contains(fileName)){
                //System.out.println(file.getName());
                file.delete();
            }
        }
    }

    @Test
    public void test2(){
        String baseFolder ="/Volumes/Elements SE/out/mock2/test";
        String targetFolder = "/Volumes/Elements SE/out/mock2/test_output";

        List<String> existedFiles = new ArrayList<>();
        for(File file : new File(targetFolder).listFiles()){
            existedFiles.add(file.getName().replace(".azw3.azw3.pdf",""));
        }


















        for(File file : new File(baseFolder).listFiles()){
            String fileName = file.getName().replace(".azw3","");
            if(existedFiles.contains(fileName)){
                //System.out.println(file.getName());
                file.delete();
            }
        }
    }
}
