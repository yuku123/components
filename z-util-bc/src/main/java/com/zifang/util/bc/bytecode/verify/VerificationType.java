package com.zifang.util.bc.bytecode.verify;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

/**
 * 校验类型（JVMS §4.7.4 verification_type_info）
 * <p>
 * 栈帧里出现的类型只有 8 种标签。两种带参数：OBJECT 指向常量池里的
 * CONSTANT_Class_info，UNINITIALIZED 指向一条 new 指令的偏移——
 * 后者正是「栈帧计算比你想的难」的那部分：new 之后的对象在 invokespecial &lt;init&gt;
 * 跑完之前不是任何类型。
 * <p>
 * 注意本类按栈帧格式存 LONG/DOUBLE 为<b>一个</b>元素，而局部变量槽位里它们占两个槽。
 * 两者的换算见 {@link Frame}。
 */
public final class VerificationType {

    public static final int TOP = 0;
    public static final int INTEGER = 1;
    public static final int FLOAT = 2;
    public static final int DOUBLE = 3;
    public static final int LONG = 4;
    public static final int NULL = 5;
    public static final int UNINITIALIZED_THIS = 6;
    public static final int OBJECT = 7;
    public static final int UNINITIALIZED = 8;

    private static final VerificationType TOP_TYPE = new VerificationType(TOP, 0);
    private static final VerificationType INT_TYPE = new VerificationType(INTEGER, 0);
    private static final VerificationType FLOAT_TYPE = new VerificationType(FLOAT, 0);
    private static final VerificationType DOUBLE_TYPE = new VerificationType(DOUBLE, 0);
    private static final VerificationType LONG_TYPE = new VerificationType(LONG, 0);
    private static final VerificationType NULL_TYPE = new VerificationType(NULL, 0);
    private static final VerificationType UNINIT_THIS_TYPE = new VerificationType(UNINITIALIZED_THIS, 0);

    private final int tag;
    /** OBJECT 时为 CONSTANT_Class_info 下标，UNINITIALIZED 时为 new 指令偏移 */
    private final int value;

    private VerificationType(int tag, int value) {
        this.tag = tag;
        this.value = value;
    }

    /**
     * 占位类型（未定义或合并后不可用）
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType top() {
        return TOP_TYPE;
    }

    /**
     * int 类型
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType integer() {
        return INT_TYPE;
    }

    /**
     * float 类型
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType floatType() {
        return FLOAT_TYPE;
    }

    /**
     * double 类型（占两个槽）
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType doubleType() {
        return DOUBLE_TYPE;
    }

    /**
     * long 类型（占两个槽）
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType longType() {
        return LONG_TYPE;
    }

    /**
     * null 字面量类型
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType nullType() {
        return NULL_TYPE;
    }

    /**
     * 尚未调用 super() 的 this
     *
     * @return VerificationType类型返回值
     */
    public static VerificationType uninitializedThis() {
        return UNINIT_THIS_TYPE;
    }

    /**
     * 引用类型
     *
     * @param classIndex CONSTANT_Class_info 下标
     * @return VerificationType类型返回值
     */
    public static VerificationType object(int classIndex) {
        return new VerificationType(OBJECT, classIndex);
    }

    /**
     * 引用一个未完成初始化的对象
     *
     * @param newPc 产生它的 new 指令偏移
     * @return VerificationType类型返回值
     */
    public static VerificationType uninitialized(int newPc) {
        return new VerificationType(UNINITIALIZED, newPc);
    }

    /**
     * 从字节流读一个校验类型
     *
     * @param inputStream 输入流
     * @return VerificationType类型返回值
     */
    public static VerificationType read(InputStream inputStream) {
        int tag = new ByteReader(inputStream).readU1().getValue();
        switch (tag) {
            case TOP:
                return top();
            case INTEGER:
                return integer();
            case FLOAT:
                return floatType();
            case DOUBLE:
                return doubleType();
            case LONG:
                return longType();
            case NULL:
                return nullType();
            case UNINITIALIZED_THIS:
                return uninitializedThis();
            case OBJECT:
                return object(U2.read(inputStream).intValue());
            case UNINITIALIZED:
                return uninitialized(U2.read(inputStream).intValue());
            default:
                throw new ClassVerifyException("未知校验类型标签: " + tag, -1);
        }
    }

    /**
     * 写出本类型
     *
     * @param out 输出流
     * @throws IOException 写出失败
     */
    public void write(DataOutput out) throws IOException {
        out.writeByte(tag);
        if (tag == OBJECT || tag == UNINITIALIZED) {
            out.writeShort(value);
        }
    }

    /**
     * 编码字节数
     *
     * @return int类型返回值
     */
    public int contentSize() {
        return tag == OBJECT || tag == UNINITIALIZED ? 3 : 1;
    }

    public int getTag() {
        return tag;
    }

    /**
     * OBJECT 的类下标或 UNINITIALIZED 的 new 偏移，其余标签为 0
     *
     * @return int类型返回值
     */
    public int getValue() {
        return value;
    }

    /**
     * 是否占两个局部变量槽
     *
     * @return boolean类型返回值
     */
    public boolean isWide() {
        return tag == LONG || tag == DOUBLE;
    }

    /**
     * 槽宽：long/double 为 2，其余为 1
     *
     * @return int类型返回值
     */
    public int slotSize() {
        return isWide() ? 2 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VerificationType)) {
            return false;
        }
        VerificationType other = (VerificationType) o;
        return tag == other.tag && value == other.value;
    }

    @Override
    public int hashCode() {
        return tag * 31 + value;
    }

    @Override
    public String toString() {
        switch (tag) {
            case TOP:
                return "top";
            case INTEGER:
                return "int";
            case FLOAT:
                return "float";
            case DOUBLE:
                return "double";
            case LONG:
                return "long";
            case NULL:
                return "null";
            case UNINITIALIZED_THIS:
                return "uninitializedThis";
            case OBJECT:
                return "object#" + value;
            case UNINITIALIZED:
                return "uninitialized@" + value;
            default:
                return "tag" + tag;
        }
    }
}