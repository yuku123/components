package com.zifang.util.bc.bytecode.writer;

import com.zifang.util.bc.bytecode.model.ClassFile;
import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.field.FieldInfo;
import com.zifang.util.bc.bytecode.model.method.MethodInfo;
import com.zifang.util.bc.bytecode.model.readtype.U1;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * class 文件写出器
 * <p>
 * 按 JVMS §4.1 的固定顺序落盘：magic、版本、常量池、访问标志、this/super、
 * 接口表、字段表、方法表、类属性表。常量池以原池为基底追加新项，
 * 因此模型里的下标可以直接原样写出。
 */
public class ClassWriter {

    private final ClassFile classFile;
    private final ConstantPoolHelper helper;
    private final ConstantPoolBuilder poolBuilder;

    /**
     * @param classFile 待写出的类模型
     */
    public ClassWriter(ClassFile classFile) {
        this.classFile = classFile;
        this.helper = new ConstantPoolHelper(classFile.getPoolInfo().getPoolList());
        this.poolBuilder = new ConstantPoolBuilder()
                .seed(classFile.getPoolInfo().getPoolList(), helper);
    }

    /**
     * 写出为字节数组
     *
     * @return byte[]类型返回值
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(8192);
        try (DataOutputStream out = new DataOutputStream(buffer)) {
            writeTo(out);
        } catch (IOException e) {
            throw new IllegalStateException("写出 class 文件失败: " + classFile.getClassName(), e);
        }
        return buffer.toByteArray();
    }

    /**
     * 写出到指定路径
     *
     * @param path 目标文件路径
     * @throws IOException 写出失败
     */
    public void writeTo(String path) throws IOException {
        java.nio.file.Files.write(java.nio.file.Paths.get(path), toByteArray());
    }

    private void writeTo(DataOutputStream out) throws IOException {
        out.writeInt(0xCAFEBABE);
        out.writeShort(classFile.getMinorVersion().intValue());
        out.writeShort(classFile.getMajorVersion().intValue());
        writeConstantPool(out);
        out.writeShort(classFile.getAccessFlag().intValue());
        out.writeShort(classFile.getClassIndex().intValue());
        out.writeShort(classFile.getSuperClassIndex().intValue());

        List<com.zifang.util.bc.bytecode.model.inter.Interface> interfaces = classFile.getInterfaceIndex().list;
        out.writeShort(interfaces.size());
        for (com.zifang.util.bc.bytecode.model.inter.Interface iface : interfaces) {
            out.writeShort(iface.index.intValue());
        }

        writeFields(out);
        writeMethods(out);

        List<AbstractAttribute> attributes = classFile.getAttributes();
        out.writeShort(attributes.size());
        for (AbstractAttribute a : attributes) {
            a.write(out);
        }
    }

    private void writeConstantPool(DataOutputStream out) throws IOException {
        List<AbstractConstantPool> pool = poolBuilder.build();
        out.writeShort(poolBuilder.constantPoolCount());
        for (AbstractConstantPool entry : pool) {
            if (entry == null) {
                // long/double 之后的空槽
                continue;
            }
            entry.write(out);
        }
    }

    private void writeFields(DataOutputStream out) throws IOException {
        List<FieldInfo> fields = classFile.getFields();
        out.writeShort(fields.size());
        for (FieldInfo field : fields) {
            out.writeShort(field.getAccessFlags().intValue());
            out.writeShort(field.getNameIndex().intValue());
            out.writeShort(field.getDescriptorIndex().intValue());
            List<AbstractAttribute> attributes = field.getAttributes();
            out.writeShort(attributes.size());
            for (AbstractAttribute a : attributes) {
                a.write(out);
            }
        }
    }

    private void writeMethods(DataOutputStream out) throws IOException {
        List<MethodInfo> methods = classFile.getMethods();
        out.writeShort(methods.size());
        for (MethodInfo method : methods) {
            out.writeShort(method.getAccessFlags().intValue());
            out.writeShort(method.getNameIndex().intValue());
            out.writeShort(method.getDescriptorIndex().intValue());
            List<AbstractAttribute> attributes = method.getAttributes();
            out.writeShort(attributes.size());
            for (AbstractAttribute a : attributes) {
                a.write(out);
            }
        }
    }

    /**
     * 供变更方登记新常量后取得新下标
     *
     * @return ConstantPoolBuilder类型返回值
     */
    public ConstantPoolBuilder poolBuilder() {
        return poolBuilder;
    }

    /**
     * 把 Code 属性的指令字节摊平成裸字节数组
     *
     * @param code Code 属性
     * @return byte[]类型返回值
     */
    public static byte[] codeBytes(Code code) {
        byte[] out = new byte[code.getCode().size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = code.getCode().get(i).getValue();
        }
        return out;
    }

    /**
     * 把裸字节写回 Code 属性的指令列表
     *
     * @param code  Code 属性
     * @param bytes 指令字节
     */
    public static void setCodeBytes(Code code, byte[] bytes) {
        List<U1> list = new java.util.ArrayList<>(bytes.length);
        for (byte b : bytes) {
            list.add(new U1(b));
        }
        code.setCode(list);
    }

    /**
     * 语义解析器
     *
     * @return ConstantPoolHelper类型返回值
     */
    public ConstantPoolHelper helper() {
        return helper;
    }
}
