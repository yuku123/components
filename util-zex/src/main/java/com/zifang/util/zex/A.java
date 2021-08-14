package com.zifang.util.zex;

import java.io.File;

public class A {
    public static void main(String[] args) {
        // cd /Applications/calibre.app/Contents/MacOS && ./ebook-convert a  b
        String command = "cd /Applications/calibre.app/Contents/MacOS && ./ebook-convert '%s' '%s' && rm -rf '%s'";
        String base = "/Volumes/Elements/全部";
        int i = 0;
        for(File f: new File(base).listFiles()){
            if(f.getName().endsWith(".mobi")){
                String aa = f.getParent();
                String name = f.getName().split("[.]")[0];
                String o = name + ".epub";
                String commands = String.format(command,f.getAbsoluteFile(), aa+"/"+o,f.getAbsoluteFile());
                System.out.println(commands);
                i++;
            }
        }
        System.out.println(i);
    }
}
