package com.zifang.util.bc.bytecode.model.annotation;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U1;

import java.io.DataOutput;
import java.io.IOException;

/**
 * 类型注解 type_annotation（4.7.20）
 * <p>
 * 前半段 target_type + target_info 的长度完全由 target_type 决定（见 JVMS 4.7.20），
 * 写错一处就会让后续注解整体错位，这里逐 target_type 精确消费。
 */
public class TypeAnnotation {

    private static final int EMPTY_TARGET = 0x12;
    private static final int METHOD_FORMAL_PARAMETER = 0x13;
    private static final int LOCALVAR_TARGET = 0x15;

    private final U1 targetType;
    private final U1 typePathLength;
    private final Annotation annotation;
    /** target_info 的原始字节，长度由 target_type 决定；type_path 按长度原样保留 */
    private final byte[] targetInfo;
    private final byte[] typePath;

    public TypeAnnotation(U1 targetType, U1 typePathLength, Annotation annotation,
                          byte[] targetInfo, byte[] typePath) {
        this.targetType = targetType;
        this.typePathLength = typePathLength;
        this.annotation = annotation;
        this.targetInfo = targetInfo;
        this.typePath = typePath;
    }

    public static TypeAnnotation read(ByteReader reader) {
        U1 targetType = reader.readU1();
        byte[] targetInfo = readTargetInfo(reader, targetType.getValue() & 0xFF);
        U1 typePathLength = reader.readU1();
        byte[] typePath = reader.readFully((typePathLength.getValue() & 0xFF) * 2);
        Annotation annotation = Annotation.read(reader);
        return new TypeAnnotation(targetType, typePathLength, annotation, targetInfo, typePath);
    }

    /**
     * 按 target_type 消费 target_info，返回其原始字节
     */
    private static byte[] readTargetInfo(ByteReader reader, int targetType) {
        switch (targetType) {
            case EMPTY_TARGET:
            case METHOD_FORMAL_PARAMETER:
                // 这两个 target_type 的 target_info 为空
                return new byte[0];
            case 0x00:                                       // CLASS_TYPE_PARAMETER: u1
                return reader.readFully(1);
            case 0x10:                                       // CLASS_TYPE_PARAMETER_BOUND: u1,u1
            case 0x11:                                       // METHOD_TYPE_PARAMETER_BOUND: u1,u1
                return reader.readFully(2);
            case LOCALVAR_TARGET: {
                // localvar_target: u2 table_length + table_length 组 (start_pc, length, index)
                int tableLength = reader.readU2().intValue();
                return reader.readFully(tableLength * 6);
            }
            case 0x01:                                       // CLASS_EXTENDS: u2 supertype_index
            case 0x14:                                       // THROWS: u2
            case 0x16:                                       // CATCH: u2 catch_type_index
            case 0x17:                                       // OFFSET: u2
                return reader.readFully(2);
            case 0x40:                                       // TYPE_ARGUMENT
            case 0x41:                                       // NEW
            case 0x42:                                       // CAST
            case 0x43:                                       // INSTANCEOF
            case 0x44:                                       // CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT
            case 0x45:                                       // METHOD_INVOCATION_TYPE_ARGUMENT
                // 这六个统一用 type_argument_target: u2 offset, u1 type_argument_index
                return reader.readFully(3);
            default:
                throw new IllegalStateException(
                        "未知的 type_annotation target_type: 0x" + Integer.toHexString(targetType));
        }
    }

    public void write(DataOutput out) throws IOException {
        targetType.write(out);
        out.write(targetInfo);
        typePathLength.write(out);
        out.write(typePath);
        annotation.write(out);
    }

    public int contentSize() {
        return 1 + targetInfo.length + 1 + typePath.length + annotation.contentSize();
    }

    public U1 getTargetType() {
        return targetType;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
}
