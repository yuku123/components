package com.zifang.util.source.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * 自定义的JavaFileObject
 * <p>
 * 里面覆盖实现了存放源代码的
 * <p>
 * 里面覆盖四线存储编译之后的字节码
 */
public class CharSequenceJavaFileObject extends SimpleJavaFileObject {

    /**
     * 用来存放编译之后的字节码的域
     */
    private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

    /**
     * 用来存放java源代码的字段
     */
    private final CharSequence sourceCode;

    /**
     * 把当前类当成存储源码的类的方式
     */
    public CharSequenceJavaFileObject(String className, CharSequence sourceCode) {
        super(URI.create(className + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    /**
     * 给编译器那边存储编译之后的字节码
     */
    public CharSequenceJavaFileObject(String fullClassName, Kind kind) {
        super(URI.create(fullClassName), kind);
        this.sourceCode = null;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(getByteCode());
    }

    /**
     * 编译结果回调的OutputStream，回调成功后就能通过下面的getByteCode()方法获取目标类编译后的字节码字节数组
     */
    @Override
    public OutputStream openOutputStream() {
        return byteCode;
    }

    public byte[] getByteCode() {
        return byteCode.toByteArray();
    }
}