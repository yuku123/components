package com.zifang.util.proxy.a.resolver.parser;

import com.zifang.util.proxy.a.resolver.parser.struct.ClassFile;
import lombok.Data;

/**
 * @author zifang
 */
@Data
public class ByteCodeParser {

    private byte[] bytes;

    private ClassFile classFile;

    public ClassFile solveClassFile() {
        if (classFile != null) {
            return classFile;
        } else {
            classFile = new ClassFile();
        }
        // 魔术
        solveMagic(classFile);
        return classFile;
    }

    private void solveMagic(ClassFile classFile) {

    }
}
