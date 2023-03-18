package com.zifang.util.compile.sorce2.compiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 存放代码相关的类
 */
public class SourceJavaFileObject extends SimpleJavaFileObject {


    private String sourceCode;

    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     *
     * @param uri  the URI for this file object
     * @param kind the kind of this file object
     */
    protected SourceJavaFileObject(URI uri, Kind kind) {
        super(uri, kind);
    }

    public SourceJavaFileObject(String className, String sourceCode) {
        super(fromClassName(className + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    private static URI fromClassName(String className) {
        try {
            return new URI(className);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(className, e);
        }
    }
}
