package com.zifang.util.compile.compliler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * A Java source that holds the code in a string builder.
 *
 * @author Cay Horstmann
 * @version 1.00 2007-11-02
 */
public class StringBuilderJavaSource extends SimpleJavaFileObject {
    private StringBuilder code;

    /**
     * Constructs a new StringBuilderJavaSource.
     *
     * @param name the name of the source file represented by this file object
     */
    public StringBuilderJavaSource(String name) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
                Kind.SOURCE);
        code = new StringBuilder();
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }

    public void append(String str) {
        code.append(str);
        code.append('\n');
    }
}
