package com.zifang.util.compile.bytecode.a.parser;

import com.zifang.util.compile.bytecode.a.parser.info.ClassFile;
import lombok.Data;

/**
 * @author zifang
 */
@Data
public class ByteCodeParser {

    private byte[] bytes;

    private ClassFile classFile;

    public ClassFile solveClassFile() {
        if(classFile != null){
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
