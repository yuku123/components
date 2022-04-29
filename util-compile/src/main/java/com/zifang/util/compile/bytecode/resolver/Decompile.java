package com.zifang.util.compile.bytecode.resolver;


import com.zifang.util.compile.bytecode.a.ConstantPoolItemType;
import com.zifang.util.compile.bytecode.a.parser.ByteCodeParser;
import com.zifang.util.compile.bytecode.a.parser.info.ClassFile;
import com.zifang.util.compile.bytecode.a.parser.util.ByteScanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Decompile {

    private final ByteScanner scanner;

    private final ClassFile classFile;

    private final MethodAttributeHandler methodAttributeHandler;

    public Decompile() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("/Users/zifang/workplace/idea_workplace/bytecode_resolver/src/main/java/com/jiangchunbo/decompiler/SimpleClass.class");
        this.scanner = new ByteScanner(inputStream);
        this.classFile = new ClassFile();
        methodAttributeHandler = new MethodAttributeHandler();
    }

    /**
     * 魔数由 4 个字节组成，十六进制为 0xCAFEBABE
     */
    public void magic() {
        classFile.setMagic(this.scanner.readToInteger(4));
    }

    public void version() {
        /*  */
        classFile.setMinorVersion(this.scanner.readToInteger(2));
        classFile.setMajorVersion(this.scanner.readToInteger(2));
    }

    public void constant() throws Exception {
        int classFileCount = this.scanner.readToInteger(2);

        for (int index = 1; index < classFileCount; ++index) {
            switch (this.scanner.readToInteger(1)) {
                case ConstantPoolItemType.UTF8: {
                    int length = this.scanner.readToInteger(2);
                    String value = this.scanner.readToString(length);
                    classFile.addConstantUtf8(classFile, index, value);
                    break;
                }
                case ConstantPoolItemType.INTEGER: {
                    /* 按照高位在前存储的 int 值 */
                    int intValue = this.scanner.readToInteger(4);
                    classFile.addConstantInteger(classFile, index, intValue);
                    break;
                }
                case ConstantPoolItemType.FLOAT: {
                    float floatValue = Float.intBitsToFloat(this.scanner.readToInteger(4));
                    classFile.addConstantFloat(classFile, index, floatValue);
                    break;
                }
                case ConstantPoolItemType.LONG: {
                    long longValue = this.scanner.readToLong();
                    classFile.addConstantLong(classFile, index++, longValue);
                    break;
                }
                case ConstantPoolItemType.DOUBLE: {
                    double doubleValue = Double.longBitsToDouble(this.scanner.readToLong());
                    classFile.addConstantDouble(classFile, index++, doubleValue);
                    break;
                }
                case ConstantPoolItemType.CLASS: {
                    int classIndex = this.scanner.readToInteger(2);
                    classFile.addConstantClass(classFile, index, classIndex);

                    break;
                }
                case ConstantPoolItemType.STRING: {
                    int stringIndex = this.scanner.readToInteger(2);
                    classFile.addConstantString(classFile, index, stringIndex);

                    break;
                }
                case ConstantPoolItemType.FIELDREF: {
                    int classIndex = this.scanner.readToInteger(2);
                    int nameAndTypeIndex = this.scanner.readToInteger(2);
                    classFile.addConstantFieldref(classFile, index, classIndex, nameAndTypeIndex);

                    break;
                }
                case ConstantPoolItemType.METHODREF: {
                    /* 指向声明方法的类描述符 CONSTANT_Class_Info 的索引项 */
                    /* 指向名称及类型描述符 CONSTANT_NameAndType_Info 的索引项 */
                    int classIndex = this.scanner.readToInteger(2);
                    int nameAndTypeIndex = this.scanner.readToInteger(2);
                    classFile.addConstantMethodref(classFile, index, classIndex, nameAndTypeIndex);

                    break;
                }
                case ConstantPoolItemType.INTERFACE_METHODREF: {
                    int classIndex = this.scanner.readToInteger(2);
                    int nameAndTypeIndex = this.scanner.readToInteger(2);
                    classFile.addConstantMethodref(classFile, index, classIndex, nameAndTypeIndex);

                    break;
                }
                case ConstantPoolItemType.NAME_ANT_TYPE: {
                    int nameIndex = this.scanner.readToInteger(2);
                    int typeIndex = this.scanner.readToInteger(2);

                    classFile.addConstantNameAndType(classFile, index, nameIndex, typeIndex);

                    break;
                }
                case ConstantPoolItemType.METHOD_HANDLE: {
                    int referenceKind = this.scanner.readToInteger(1);
                    int descriptorIndex = this.scanner.readToInteger(2);
                    classFile.addConstantNameAndType(classFile, index, referenceKind, descriptorIndex);

                    break;
                }
                case ConstantPoolItemType.METHOD_TYPE: {
                    int descriptorIndex = this.scanner.readToInteger(2);
                    classFile.addConstantNameAndType(classFile, index, descriptorIndex, descriptorIndex);

                    break;
                }

                case ConstantPoolItemType.INVOKE_DYNAMIC: {
                    int referenceKind = this.scanner.readToInteger(2);
                    int descriptorIndex = this.scanner.readToInteger(2);
                    classFile.addConstantNameAndType(classFile, index, referenceKind, descriptorIndex);

                    break;
                }
                default: {
                    break;
                }

            }
        }
    }

    public void accessFlags() throws Exception {
        classFile.setAccessFlags(this.scanner.readToInteger(2));

    }

    public void thisClass() throws Exception {
        int thisClassIndex = this.scanner.readToInteger(2);

    }

    public void superClass() throws Exception {
        int superClassIndex = this.scanner.readToInteger(2);
    }

    public void interfaces() throws Exception {
        byte[] interfacesLen = new byte[2];
        this.scanner.readToInteger(2);

        int len = interfacesLen[0] << 8 & 0x0000ff00 | interfacesLen[1] & 0x000000ff;
    }

    public void fields() throws Exception {

        int fieldsCount = this.scanner.readToInteger(2);
        for (int i = 0; i < fieldsCount; ++i) {

            int accessFlag = this.scanner.readToInteger(2);
            int nameIndex = this.scanner.readToInteger(2);
            int descriptorIndex = this.scanner.readToInteger(2);
            int attributesCount = this.scanner.readToInteger(2); // 属性表长度
            for (int j = 0; j < attributesCount; ++j) {
                int attributeNameIndex = this.scanner.readToInteger(2);// 属性名索引
                String attributeName = classFile.getString(attributeNameIndex);
                int attributeLength = this.scanner.readToInteger(4);
                switch (attributeName) {
                    case "ConstantValue":
                        int constantValueIndex = this.scanner.readToInteger(2);
                        break;
                    case "Signature":
                        int signatureIndex = this.scanner.readToInteger(2);
                        break;
                    case "Deprecated":
                        System.out.println("过时");
                        break;
                    case "RuntimeVisibleAnnotations":
                    case "RuntimeInvisibleAnnotations":
                    case "RuntimeVisibleTypeAnnotations":
                    case "RuntimeInvisibleTypeAnnotations":
                        int numAnnotations = this.scanner.readToInteger(2); // 注解数量
                        System.out.println("注解数量" + numAnnotations);
                        for (int m = 0; m < numAnnotations; m++) {
                            int typeIndex = this.scanner.readToInteger(2);
                            int numElementValuePairs = this.scanner.readToInteger(2);
                            for (int n = 0; n < numElementValuePairs; n++) {
                                int keyIndex = this.scanner.readToInteger(2);
                                this.scanner.readToString(1);
                                int valueIndex = this.scanner.readToInteger(2);
                            }
                        }
                        break;
                    default:
                        this.scanner.readToString(attributeLength);

                }
            }
            classFile.addFiled(accessFlag, nameIndex, descriptorIndex, attributesCount);
        }
    }

    public void methods() throws Exception {
        int methodCount = this.scanner.readToInteger(2);

        for (int i = 0; i < methodCount; ++i) {
            int accessFlag = this.scanner.readToInteger(2); // 方法访问标志

            int nameIndex = this.scanner.readToInteger(2);

            int descriptorIndex = this.scanner.readToInteger(2);

            int attributesCount = this.scanner.readToInteger(2);

            for (int j = 0; j < attributesCount; ++j) {

                int attributeNameIndex = this.scanner.readToInteger(2);
                int attributeLength = this.scanner.readToInteger(4);

                String attributeName = classFile.getString(attributeNameIndex);
                switch (attributeName) {
                    case "Code":
                        methodAttributeHandler.handleCode();
                        break;
                    case "MethodParameters":
                        int parametersCount = this.scanner.readToInteger(1);
                        System.out.println("参数有" + parametersCount + "个");

                        for (int k = 0; k < parametersCount; k++) {
                            int parameterNameIndex = this.scanner.readToInteger(2);
                            int accessFlags = this.scanner.readToInteger(2);
                        }
                        break;
                    default:
                        this.scanner.readToString(attributeLength);
                }
            }
        }
    }

    private class MethodAttributeHandler {
        
        public void handleCode() {
            int maxStack = scanner.readToInteger(2); // 最大栈
            int maxLocals = scanner.readToInteger(2); // 最大本地变量
            int codeLength = scanner.readToInteger(4); // code 长度
            scanner.readToString(codeLength);

            /**
             * 处理异常表
             */
            int exceptionTableLength = scanner.readToInteger(2); // 异常表长度

            for (int m = 0; m < exceptionTableLength; m++) {
                int startPc = scanner.readToInteger(2);
                int endPc = scanner.readToInteger(2);
                int handlerPc = scanner.readToInteger(2);
                int catchType = scanner.readToInteger(2);
            }

            int codeAttributesCount = scanner.readToInteger(2);
            for (int m = 0; m < codeAttributesCount; m++) {
                int codeAttributeNameIndex = scanner.readToInteger(2);
                int codeAttributeLength = scanner.readToInteger(4);

                String codeAttributeName = classFile.getString(codeAttributeNameIndex);
                switch (codeAttributeName) {
                    case "LineNumberTable":
                        int lineNumberTableLength = scanner.readToInteger(2);
                        for (int n = 0; n < lineNumberTableLength; n++) {
                            int startPc = scanner.readToInteger(2);
                            int lineNumber = scanner.readToInteger(2);
                        }
                        break;
                    case "LocalVariableTable":
                        int localVariableTableLength = scanner.readToInteger(2); // 本地变量表数
                        for (int n = 0; n < localVariableTableLength; n++) {
                            int startPc = scanner.readToInteger(2);
                            int length = scanner.readToInteger(2);
                            int localVariableNameIndex = scanner.readToInteger(2);
                            int localVariableDescriptorIndex = scanner.readToInteger(2);
                            int index = scanner.readToInteger(2);
                        }
                        break;
                    case "StackMapTable":
                    case "RuntimeVisibleTypeAnnotations":
                    case "RuntimeInvisibleTypeAnnotations":
                        scanner.readToString(codeAttributeLength);
                        break;
                }

            }
        }
    }

    public static void main(String[] args) throws Exception {

        FileInputStream inputStream = new FileInputStream("/Users/zifang/workplace/idea_workplace/bytecode_resolver/src/main/java/com/jiangchunbo/decompiler/SimpleClass.class");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);

        ByteCodeParser byteCodeParser = new ByteCodeParser();
        byteCodeParser.setBytes(bytes);
        ClassFile classFile = byteCodeParser.solveClassFile();

        Decompile decompiler = new Decompile();

        // 魔数
        decompiler.magic();

        // 版本号
        decompiler.version();

        // 常量池
        decompiler.constant();

        // 访问标志
        decompiler.accessFlags();

        // 类索引
        decompiler.thisClass();

        // 父类索引
        decompiler.superClass();

        // 接口集合
        decompiler.interfaces();

        decompiler.fields();

        decompiler.methods();
        System.out.println(decompiler.classFile);
    }
}
