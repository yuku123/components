package com.zifang.util.core.compile;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class StringJavaFileObject<T> extends SimpleJavaFileObject {

    private T content;

    public StringJavaFileObject(String className, T content) {
        super(URI.create("string:///" + className.replace('.', '/')
                + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    @Override
    public String getCharContent(boolean ignoreEncodingErrors) {
        return content.toString();
    }
}
