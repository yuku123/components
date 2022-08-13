package com.zifang.util.compile.bytecode.resolver.parser.util;

import com.zifang.util.compile.bytecode.resolver.parser.struct.Field;

public class Descriptor {

    public String descriprField(Field field) {
        StringBuilder s = new StringBuilder();
        if ((field.getAccessFlag() & 0x00000001) == 0x000000001) {
            s.append("public ");
        }
        if ((field.getAccessFlag() & 0x00000002) == 0x000000002) {
            s.append("private ");
        }
        if ((field.getAccessFlag() & 0x00000004) == 0x000000004) {
            s.append("protected ");
        }
        if ((field.getAccessFlag() & 0x00000008) == 0x000000008) {
            s.append("static ");
        }
        if ((field.getAccessFlag() & 0x00000010) == 0x000000010) {
            s.append("final");
        }
        if ((field.getAccessFlag() & 0x00000040) == 0x000000040) {
            s.append("volatile");
        }
        if ((field.getAccessFlag() & 0x00000080) == 0x000000080) {
            s.append("transient");
        }
        if ((field.getAccessFlag() & 0x00001000) == 0x000001000) {
            s.append("synthetic ");
        }
        if ((field.getAccessFlag() & 0x00004000) == 0x000004000) {
            s.append("enum ");
        }

        //String descriptor = getString(descriptorIndex);
        String descriptor = "B";

        switch (descriptor) {
            case "B":
                s.append("byte");
                break;
            case "C":
                s.append("char");
                break;
            case "D":
                s.append("double");
                break;
            case "F":
                s.append("float");
                break;
            case "I":
                s.append("int");
                break;
            case "J":
                s.append("long");
                break;
            case "S":
                s.append("short");
                break;
            case "Z":
                s.append("boolean");
                break;
            case "V":
                s.append("void");
                break;
            default:
                s.append(descriptor.substring(1, descriptor.length() - 1).replace('/', '.'));
                break;
        }

        //s.append(" ").append(getString(nameIndex));

        return s.toString();
    }
}
