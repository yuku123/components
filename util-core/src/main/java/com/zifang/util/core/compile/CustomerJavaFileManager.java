package com.zifang.util.core.compile;


import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class CustomerJavaFileManager extends ForwardingJavaFileManager {

    public Map<String, BytesJavaFileObject> getFileObjectHashMap() {
        return fileObjectHashMap;
    }
    private Map<String, BytesJavaFileObject> fileObjectHashMap = new HashMap<>();
    protected CustomerJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {

        BytesJavaFileObject fileObject = fileObjectHashMap.get(className);
        if (fileObject == null) {
            fileObject = new BytesJavaFileObject(className, kind);
            fileObjectHashMap.put(className, fileObject);
            return fileObject;
        } else {
            return fileObject;
        }
    }

    @Override
    public boolean hasLocation(Location location) {
        return super.hasLocation(location);
    }

    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return super.isSameFile(a, b);
    }

    @Override
    public boolean handleOption(String current, Iterator remaining) {
        return super.handleOption(current, remaining);
    }

    @Override
    public int isSupportedOption(String option) {
        return super.isSupportedOption(option);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        return super.getJavaFileForInput(location, className, kind);
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        return super.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        return super.getFileForOutput(location, packageName, relativeName, sibling);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
