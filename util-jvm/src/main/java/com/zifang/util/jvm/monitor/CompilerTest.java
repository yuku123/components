package com.zifang.util.jvm.monitor;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;

public class CompilerTest extends ClassLoader{
    public static void main(String[] args) throws IOException {
        //编译class
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manager = compiler.getStandardFileManager(null,null,null);
        Iterable iterable = manager.getJavaFileObjects("");
        JavaCompiler.CompilationTask task = compiler.getTask(null,manager,null,null,null,iterable);
        task.call();
        manager.close();
    }

    public void a(){
        //从class loader 里面搞出来
        //defineClass()
    }
}
