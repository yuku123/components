package com.zifang.util.compile.compliler;

import lombok.extern.slf4j.Slf4j;


import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public abstract class CFJavaFileManager extends ForwardingJavaFileManager {

    public Map<String, BytesJavaFileObject> getFileObjectHashMap() {
        return fileObjectHashMap;
    }

    /**
     * 保存编译后Class文件的对象
     */
    private Map<String, BytesJavaFileObject> fileObjectHashMap = new HashMap<>();

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    protected CFJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    /**
     * 将JavaFileObject对象的引用交给JavaCompiler，让它将编译好后的Class文件装载进来
     */
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
        log.info("isSameFile : {}", a);

        return super.isSameFile(a, b);
    }

    /**
     * 处理 编译命令的参数，例如-encoding， -classpath 等
     *
     * @param current   参数名，如 -classpath
     * @param remaining 参数值列表
     * @return
     */
    @Override
    public boolean handleOption(String current, Iterator remaining) {
        return super.handleOption(current, remaining);
    }

    @Override
    public int isSupportedOption(String option) {
        log.info("isSupportedOption : {}", option);

        return super.isSupportedOption(option);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        log.info("getJavaFileForInput : {}", location);

        return super.getJavaFileForInput(location, className, kind);
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        log.info("getFileForInput : {}", location);

        return super.getFileForInput(location, packageName, relativeName);
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        log.info("getFileForOutput : {}", location);

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
