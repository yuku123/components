package com.zifang.util.compile;

import com.zifang.util.compile.bytecode.resolver2.ClassFile;
import com.zifang.util.compile.bytecode.resolver2.constantpool.*;
import com.zifang.util.compile.bytecode.resolver2.field.FieldInfo;
import com.zifang.util.compile.bytecode.resolver2.field.FieldTable;
import com.zifang.util.compile.bytecode.resolver2.inter.Interface;
import com.zifang.util.compile.bytecode.resolver2.inter.InterfaceIndex;
import com.zifang.util.compile.bytecode.resolver2.method.MethodInfo;
import com.zifang.util.compile.bytecode.resolver2.method.MethodTable;
import com.zifang.util.compile.bytecode.resolver2.readtype.U1;
import com.zifang.util.compile.bytecode.resolver2.readtype.U2;
import com.zifang.util.compile.bytecode.resolver2.readtype.U4;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;


@Slf4j
public class Testddd {

    @Test
    public void sss() throws FileNotFoundException {
        File file = new File("/Users/zifang/workplace/idea_workplace/components/util-compile/target/classes/com/zifang/util/compile/bytecode/simple2/file/TestClassParse1.class");

        FileInputStream stream=new FileInputStream(file);
        ClassFile.magic=U4.read(stream);//魔数
        ClassFile.minorVersion= U2.read(stream);//主版本号
        ClassFile.majorVersion=U2.read(stream);//次版本号
        ClassFile.constantPoolSize=U2.read(stream);//常量池大小
        short pollSize = ClassFile.constantPoolSize.value;
        log.info("常量池大小为:{}", pollSize);
        //解析常量池
        ConstantPoolInfo poolInfo = parseConstantPool(stream, (short) (pollSize - 1));
        ClassFile.poolInfo=poolInfo;
        int index=1;
        for (AbstractConstantPool abstractConstantPool : poolInfo.getPoolList()) {
            log.info("常量池[{}]:{}",index,abstractConstantPool);
            index++;
        }
        //解析访问标志
        ClassFile.accessFlag=U2.read(stream);
        //解析类索引,父类索引,接口索引(对于接口索引集合，
        // 入口的第一项u2类型的数据为接口计数器（interfaces_count），表示索引表的容量。
        // 如果该类没有实现任何接口，则该计数器值为0，后面接口的索引表不再占用任何字节。)
        ClassFile.classIndex=U2.read(stream);
        ClassFile.superClassIndex=U2.read(stream);
        ClassFile.interfaceIndex=parseInterface(stream);
        //字段表集合
        ClassFile.fieldInfo=parseFieldInfo(stream);
        //方法表
        ClassFile.methodInfo=parseMethodInfo(stream);
        /**
         * 字段表集合, 方法表集合打断点对照常量池看
         */
    }


    public static FieldInfo parseFieldInfo(InputStream stream) {
        FieldInfo fieldInfo = new FieldInfo(U2.read(stream));
        short value = fieldInfo.length.value;
        for (int i = 0; i < value; i++) {
            FieldTable fieldTable = new FieldTable(stream);
            fieldInfo.list.add(fieldTable);
        }
        return fieldInfo;
    }

    public static MethodInfo parseMethodInfo(InputStream stream) {
        MethodInfo methodInfo = new MethodInfo(U2.read(stream));
        short value = methodInfo.length.value;
        for (int i = 0; i < value; i++) {
            MethodTable methodTable = new MethodTable(stream);
            methodInfo.list.add(methodTable);
        }
        return methodInfo;
    }

    public static InterfaceIndex parseInterface(InputStream stream) {
        InterfaceIndex interfaceIndex = new InterfaceIndex(stream);
        short value = interfaceIndex.length.value;//实现接口的数量
        for (int i = 0; i < value; i++) {
            U2 u2 = U2.read(stream);//接口的索引位置
            Interface in = new Interface(u2);
            interfaceIndex.list.add(in);
        }
        return interfaceIndex;
    }


    public static ConstantPoolInfo parseConstantPool(InputStream stream, short pollSize) {
        ConstantPoolInfo poolInfo = new ConstantPoolInfo(pollSize);

        for (int i = 0; i < pollSize; i++) {
            U1 tag = U1.read(stream);
            AbstractConstantPool poll = newConstantPoolInfo(tag, stream);
            poolInfo.getPoolList().add(poll);
        }
        return poolInfo;
    }


    private static AbstractConstantPool newConstantPoolInfo(U1 tag, InputStream inputStream) {
        int tagValue = tag.getValue();
        AbstractConstantPool constantPoolInfo = null;
        switch (tagValue) {
            case AbstractConstantPool.CONSTANT_UTF8_INFO:
                constantPoolInfo = new Utf8Info(AbstractConstantPool.CONSTANT_UTF8_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_INTEGER_INFO:
                constantPoolInfo = new ConstantIntegerInfo(AbstractConstantPool.CONSTANT_INTEGER_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_FLOAT_INFO:
                constantPoolInfo = new ConstantFloatInfo(AbstractConstantPool.CONSTANT_FLOAT_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_LONG_INFO:
                constantPoolInfo = new ConstantLongInfo(AbstractConstantPool.CONSTANT_LONG_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_DOUBLE_INFO:
                constantPoolInfo = new ConstantDoubleInfo(AbstractConstantPool.CONSTANT_DOUBLE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_CLASS_INFO:
                constantPoolInfo = new ClassInfo(AbstractConstantPool.CONSTANT_CLASS_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_STRING_INFO:
                constantPoolInfo = new ConstantClassInfo(AbstractConstantPool.CONSTANT_STRING_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_FIELDREF_INFO:
                constantPoolInfo = new FieldRefInfo(AbstractConstantPool.CONSTANT_FIELDREF_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_METHODREF_INFO:
                constantPoolInfo = new MethodRefInfo(AbstractConstantPool.CONSTANT_METHODREF_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_INTERFACEMETHODREF_INFO:
                constantPoolInfo = new ConstantInterfaceMethodRefInfo(AbstractConstantPool.CONSTANT_INTERFACEMETHODREF_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_NAMEANDTYPE_INFO:
                constantPoolInfo = new ConstantNameAndTypeInfo(AbstractConstantPool.CONSTANT_NAMEANDTYPE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_METHODHANDLE_INFO:
                constantPoolInfo = new ConstantMethodHandleInfo(AbstractConstantPool.CONSTANT_METHODHANDLE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_METHODTYPE_INFO:
                constantPoolInfo = new ConstantMethodTypeInfo(AbstractConstantPool.CONSTANT_METHODTYPE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case AbstractConstantPool.CONSTANT_INVOKEDYNAMIC_INFO:
                constantPoolInfo = new ConstantInvokeDynamicInfo(AbstractConstantPool.CONSTANT_INVOKEDYNAMIC_INFO);
                constantPoolInfo.read(inputStream);
                break;
        }
        return constantPoolInfo;
    }
}
