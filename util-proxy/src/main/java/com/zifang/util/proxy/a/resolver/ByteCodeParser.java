package com.zifang.util.proxy.a.resolver;

import com.zifang.util.proxy.a.resolver.parser.struct.ClassFile;
import com.zifang.util.proxy.a.resolver.parser.struct.ConstantFloat;
import com.zifang.util.proxy.a.resolver.parser.struct.ConstantInteger;
import com.zifang.util.proxy.a.resolver.parser.struct.ConstantUtf8;
import com.zifang.util.proxy.a.resolver.parser.util.ByteScanner;
import lombok.Data;

import java.io.FileInputStream;

/**
 * @author zifang
 */
@Data
public class ByteCodeParser {

    private ByteScanner scanner;

    private ClassFile classFile;

    private MethodAttributeHandler methodAttributeHandler = new MethodAttributeHandler();


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

    private ClassFile parser() throws Exception {

        this.classFile = new ClassFile();

        // 处理魔数
        handleMagic();

        // 处理版本号
        handleVersion();

        // 常量池
        handleConstantPool();

        // 访问标志
        handleAccessFlags();

        // 类索引
        thisClass();

        // 父类索引
        superClass();

        // 接口集合
        interfaces();

        fields();

        methods();

        return classFile;
    }

    private void handleVersion() {
        classFile.setMinorVersion(scanner.readToInteger(2));
        classFile.setMajorVersion(scanner.readToInteger(2));
    }

    private void handleMagic() {
        classFile.setMagic(scanner.readToInteger(4));
    }


    public void handleConstantPool() {
        int classFileCount = this.scanner.readToInteger(2);

        for (int index = 1; index < classFileCount; ++index) {
            int constantPoolItemType = this.scanner.readToInteger(1);
            if (constantPoolItemType == ConstantPoolItemType.UTF8) {
                int length = this.scanner.readToInteger(2);
                String value = this.scanner.readToString(length);
                classFile.addConstantItem(new ConstantUtf8(classFile, index, value));
            } else if (constantPoolItemType == ConstantPoolItemType.INTEGER) {
                int value = this.scanner.readToInteger(4);
                classFile.addConstantItem(new ConstantInteger(classFile, index, value));
            } else if (constantPoolItemType == ConstantPoolItemType.FLOAT) {
                float value = Float.intBitsToFloat(this.scanner.readToInteger(4));
                classFile.addConstantItem(new ConstantFloat(classFile, index, value));
            } else if (constantPoolItemType == ConstantPoolItemType.LONG) {
                long longValue = this.scanner.readToLong();
                classFile.addConstantLong(classFile, index++, longValue);
            } else if (constantPoolItemType == ConstantPoolItemType.DOUBLE) {
                double doubleValue = Double.longBitsToDouble(this.scanner.readToLong());
                classFile.addConstantDouble(classFile, index++, doubleValue);
            } else if (constantPoolItemType == ConstantPoolItemType.CLASS) {
                int classIndex = this.scanner.readToInteger(2);
                classFile.addConstantClass(classFile, index, classIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.STRING) {
                int stringIndex = this.scanner.readToInteger(2);
                classFile.addConstantString(classFile, index, stringIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.FIELD_REF) {
                int classIndex = this.scanner.readToInteger(2);
                int nameAndTypeIndex = this.scanner.readToInteger(2);
                classFile.addConstantFieldref(classFile, index, classIndex, nameAndTypeIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.METHOD_REF) {
                int classIndex = this.scanner.readToInteger(2);
                int nameAndTypeIndex = this.scanner.readToInteger(2);
                classFile.addConstantMethodref(classFile, index, classIndex, nameAndTypeIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.INTERFACE_METHOD_REF) {
                int classIndex = this.scanner.readToInteger(2);
                int nameAndTypeIndex = this.scanner.readToInteger(2);
                classFile.addConstantMethodref(classFile, index, classIndex, nameAndTypeIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.NAME_ANT_TYPE) {
                int nameIndex = this.scanner.readToInteger(2);
                int typeIndex = this.scanner.readToInteger(2);
                classFile.addConstantNameAndType(classFile, index, nameIndex, typeIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.METHOD_HANDLE) {
                int referenceKind = this.scanner.readToInteger(1);
                int descriptorIndex = this.scanner.readToInteger(2);
                classFile.addConstantNameAndType(classFile, index, referenceKind, descriptorIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.METHOD_TYPE) {
                int descriptorIndex = this.scanner.readToInteger(2);
                classFile.addConstantNameAndType(classFile, index, descriptorIndex, descriptorIndex);
            } else if (constantPoolItemType == ConstantPoolItemType.INVOKE_DYNAMIC) {
                int referenceKind = this.scanner.readToInteger(2);
                int descriptorIndex = this.scanner.readToInteger(2);
                classFile.addConstantNameAndType(classFile, index, referenceKind, descriptorIndex);
            }
        }
    }

    public void handleAccessFlags() throws Exception {
        classFile.setAccessFlags(this.scanner.readToInteger(2));
    }

    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("/Users/zifang/workplace/idea_workplace/bytecode_resolver/src/main/java/com/jiangchunbo/decompiler/SimpleClass.class");
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);

        ByteScanner scanner = new ByteScanner(inputStream);
        ByteCodeParser byteCodeParser = new ByteCodeParser();
        byteCodeParser.setScanner(scanner);
        ClassFile classFile = byteCodeParser.parser();
        System.out.println(classFile);
    }

}
