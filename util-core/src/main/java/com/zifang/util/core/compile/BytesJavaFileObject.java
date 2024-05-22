package com.zifang.util.core.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class BytesJavaFileObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    public BytesJavaFileObject(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
    }

    public byte[] getBytes() {
        return bos.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return bos;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        bos.close();
    }
}
