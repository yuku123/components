package com.zifang.util.bc.bytecode.model.readtype;

/**
 * 字节码读取异常
 * <p>
 * 解析阶段不向外抛 IOException，让「读字节」这一层与解析逻辑解耦。
 */
public class ByteCodeReadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ByteCodeReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ByteCodeReadException(String message) {
        super(message);
    }
}
