package com.zifang.util.bc.bytecode.model.instruction;

/**
 * JVM 指令表（JVMS §6.5）
 * <p>
 * 写出层要按操作数长度重写常量池下标、帧计算要按指令语义推演栈变化、
 * 反编译器要把字节码还原成语句，三者依赖同一张表，集中在这里只描述一次。
 * <p>
 * 三张表分别是：操作数总字节数、其中是否含常量池下标、以及助记符。
 */
public final class Opcodes {

    private Opcodes() {
    }

    // 单字节操作码（JVMS §6.5 全表 202 项）
    public static final int NOP = 0x00;
    public static final int ACONST_NULL = 0x01;
    public static final int ICONST_M1 = 0x02;
    public static final int ICONST_0 = 0x03;
    public static final int ICONST_1 = 0x04;
    public static final int ICONST_2 = 0x05;
    public static final int ICONST_3 = 0x06;
    public static final int ICONST_4 = 0x07;
    public static final int ICONST_5 = 0x08;
    public static final int LCONST_0 = 0x09;
    public static final int LCONST_1 = 0x0a;
    public static final int FCONST_0 = 0x0b;
    public static final int FCONST_1 = 0x0c;
    public static final int FCONST_2 = 0x0d;
    public static final int DCONST_0 = 0x0e;
    public static final int DCONST_1 = 0x0f;
    public static final int BIPUSH = 0x10;
    public static final int SIPUSH = 0x11;
    public static final int LDC = 0x12;
    public static final int LDC_W = 0x13;
    public static final int LDC2_W = 0x14;
    public static final int ILOAD = 0x15;
    public static final int LLOAD = 0x16;
    public static final int FLOAD = 0x17;
    public static final int DLOAD = 0x18;
    public static final int ALOAD = 0x19;
    public static final int ILOAD_0 = 0x1a;
    public static final int ILOAD_1 = 0x1b;
    public static final int ILOAD_2 = 0x1c;
    public static final int ILOAD_3 = 0x1d;
    public static final int LLOAD_0 = 0x1e;
    public static final int LLOAD_1 = 0x1f;
    public static final int LLOAD_2 = 0x20;
    public static final int LLOAD_3 = 0x21;
    public static final int FLOAD_0 = 0x22;
    public static final int FLOAD_1 = 0x23;
    public static final int FLOAD_2 = 0x24;
    public static final int FLOAD_3 = 0x25;
    public static final int DLOAD_0 = 0x26;
    public static final int DLOAD_1 = 0x27;
    public static final int DLOAD_2 = 0x28;
    public static final int DLOAD_3 = 0x29;
    public static final int ALOAD_0 = 0x2a;
    public static final int ALOAD_1 = 0x2b;
    public static final int ALOAD_2 = 0x2c;
    public static final int ALOAD_3 = 0x2d;
    public static final int IALOAD = 0x2e;
    public static final int LALOAD = 0x2f;
    public static final int FALOAD = 0x30;
    public static final int DALOAD = 0x31;
    public static final int AALOAD = 0x32;
    public static final int BALOAD = 0x33;
    public static final int CALOAD = 0x34;
    public static final int SALOAD = 0x35;
    public static final int ISTORE = 0x36;
    public static final int LSTORE = 0x37;
    public static final int FSTORE = 0x38;
    public static final int DSTORE = 0x39;
    public static final int ASTORE = 0x3a;
    public static final int ISTORE_0 = 0x3b;
    public static final int ISTORE_1 = 0x3c;
    public static final int ISTORE_2 = 0x3d;
    public static final int ISTORE_3 = 0x3e;
    public static final int LSTORE_0 = 0x3f;
    public static final int LSTORE_1 = 0x40;
    public static final int LSTORE_2 = 0x41;
    public static final int LSTORE_3 = 0x42;
    public static final int FSTORE_0 = 0x43;
    public static final int FSTORE_1 = 0x44;
    public static final int FSTORE_2 = 0x45;
    public static final int FSTORE_3 = 0x46;
    public static final int DSTORE_0 = 0x47;
    public static final int DSTORE_1 = 0x48;
    public static final int DSTORE_2 = 0x49;
    public static final int DSTORE_3 = 0x4a;
    public static final int ASTORE_0 = 0x4b;
    public static final int ASTORE_1 = 0x4c;
    public static final int ASTORE_2 = 0x4d;
    public static final int ASTORE_3 = 0x4e;
    public static final int IASTORE = 0x4f;
    public static final int LASTORE = 0x50;
    public static final int FASTORE = 0x51;
    public static final int DASTORE = 0x52;
    public static final int AASTORE = 0x53;
    public static final int BASTORE = 0x54;
    public static final int CASTORE = 0x55;
    public static final int SASTORE = 0x56;
    public static final int POP = 0x57;
    public static final int POP2 = 0x58;
    public static final int DUP = 0x59;
    public static final int DUP_X1 = 0x5a;
    public static final int DUP_X2 = 0x5b;
    public static final int DUP2 = 0x5c;
    public static final int DUP2_X1 = 0x5d;
    public static final int DUP2_X2 = 0x5e;
    public static final int SWAP = 0x5f;
    public static final int IADD = 0x60;
    public static final int LADD = 0x61;
    public static final int FADD = 0x62;
    public static final int DADD = 0x63;
    public static final int ISUB = 0x64;
    public static final int LSUB = 0x65;
    public static final int FSUB = 0x66;
    public static final int DSUB = 0x67;
    public static final int IMUL = 0x68;
    public static final int LMUL = 0x69;
    public static final int FMUL = 0x6a;
    public static final int DMUL = 0x6b;
    public static final int IDIV = 0x6c;
    public static final int LDIV = 0x6d;
    public static final int FDIV = 0x6e;
    public static final int DDIV = 0x6f;
    public static final int IREM = 0x70;
    public static final int LREM = 0x71;
    public static final int FREM = 0x72;
    public static final int DREM = 0x73;
    public static final int INEG = 0x74;
    public static final int LNEG = 0x75;
    public static final int FNEG = 0x76;
    public static final int DNEG = 0x77;
    public static final int ISHL = 0x78;
    public static final int LSHL = 0x79;
    public static final int ISHR = 0x7a;
    public static final int LSHR = 0x7b;
    public static final int IUSHR = 0x7c;
    public static final int LUSHR = 0x7d;
    public static final int IAND = 0x7e;
    public static final int LAND = 0x7f;
    public static final int IOR = 0x80;
    public static final int LOR = 0x81;
    public static final int IXOR = 0x82;
    public static final int LXOR = 0x83;
    public static final int IINC = 0x84;
    public static final int I2L = 0x85;
    public static final int I2F = 0x86;
    public static final int I2D = 0x87;
    public static final int L2I = 0x88;
    public static final int L2F = 0x89;
    public static final int L2D = 0x8a;
    public static final int F2I = 0x8b;
    public static final int F2L = 0x8c;
    public static final int F2D = 0x8d;
    public static final int D2I = 0x8e;
    public static final int D2L = 0x8f;
    public static final int D2F = 0x90;
    public static final int I2B = 0x91;
    public static final int I2C = 0x92;
    public static final int I2S = 0x93;
    public static final int LCMP = 0x94;
    public static final int FCMPL = 0x95;
    public static final int FCMPG = 0x96;
    public static final int DCMPL = 0x97;
    public static final int DCMPG = 0x98;
    public static final int IFEQ = 0x99;
    public static final int IFNE = 0x9a;
    public static final int IFLT = 0x9b;
    public static final int IFGE = 0x9c;
    public static final int IFGT = 0x9d;
    public static final int IFLE = 0x9e;
    public static final int IF_ICMPEQ = 0x9f;
    public static final int IF_ICMPNE = 0xa0;
    public static final int IF_ICMPLT = 0xa1;
    public static final int IF_ICMPGE = 0xa2;
    public static final int IF_ICMPGT = 0xa3;
    public static final int IF_ICMPLE = 0xa4;
    public static final int IF_ACMPEQ = 0xa5;
    public static final int IF_ACMPNE = 0xa6;
    public static final int GOTO = 0xa7;
    public static final int JSR = 0xa8;
    public static final int RET = 0xa9;
    public static final int TABLESWITCH = 0xaa;
    public static final int LOOKUPSWITCH = 0xab;
    public static final int IRETURN = 0xac;
    public static final int LRETURN = 0xad;
    public static final int FRETURN = 0xae;
    public static final int DRETURN = 0xaf;
    public static final int ARETURN = 0xb0;
    public static final int RETURN = 0xb1;
    public static final int GETSTATIC = 0xb2;
    public static final int PUTSTATIC = 0xb3;
    public static final int GETFIELD = 0xb4;
    public static final int PUTFIELD = 0xb5;
    public static final int INVOKEVIRTUAL = 0xb6;
    public static final int INVOKESPECIAL = 0xb7;
    public static final int INVOKESTATIC = 0xb8;
    public static final int INVOKEINTERFACE = 0xb9;
    public static final int INVOKEDYNAMIC = 0xba;
    public static final int NEW = 0xbb;
    public static final int NEWARRAY = 0xbc;
    public static final int ANEWARRAY = 0xbd;
    public static final int ARRAYLENGTH = 0xbe;
    public static final int ATHROW = 0xbf;
    public static final int CHECKCAST = 0xc0;
    public static final int INSTANCEOF = 0xc1;
    public static final int MONITORENTER = 0xc2;
    public static final int MONITOREXIT = 0xc3;
    public static final int WIDE = 0xc4;
    public static final int MULTIANEWARRAY = 0xc5;
    public static final int IFNULL = 0xc6;
    public static final int IFNONNULL = 0xc7;
    public static final int GOTO_W = 0xc8;
    public static final int JSR_W = 0xc9;

    /** 操作数总字节数；switch 用 -1 表示变长 */
    private static final int[] OPERAND_SIZE = new int[256];

    /** 操作数是否携带常量池下标 */
    private static final boolean[] HAS_CONSTANT_INDEX = new boolean[256];

    /** 条件跳转（含 ifnull/ifnonnull） */
    private static final boolean[] CONDITIONAL_JUMP = new boolean[256];

    /** 不含条件跳转与 goto 的转移（jsr 系列） */
    private static final boolean[] UNCONDITIONAL_JUMP = new boolean[256];

    /** 返回与抛异常类指令，指令流终点 */
    private static final boolean[] TERMINATOR = new boolean[256];

    private static final String[] MNEMONICS = new String[256];

    static {
        // 默认：绝大多数指令无操作数
        java.util.Arrays.fill(OPERAND_SIZE, 0);
        java.util.Arrays.fill(MNEMONICS, "unknown");

        // 常量载入
        OPERAND_SIZE[BIPUSH] = 1;
        OPERAND_SIZE[SIPUSH] = 2;
        OPERAND_SIZE[LDC] = 1;
        OPERAND_SIZE[LDC_W] = 2;
        OPERAND_SIZE[LDC2_W] = 2;
        // 局部变量 load/store（带 _n 变体的从 0x1a 起连续 4 组各 4 个，共到 0x4e）
        for (int i = ILOAD; i <= ALOAD; i++) {
            OPERAND_SIZE[i] = 1;
        }
        for (int i = ISTORE; i <= ASTORE; i++) {
            OPERAND_SIZE[i] = 1;
        }
        OPERAND_SIZE[IINC] = 2;
        OPERAND_SIZE[RET] = 1;

        // 条件跳转 0x99-0xa6 与 ifnull/ifnonnull
        for (int i = IFEQ; i <= IF_ACMPEQ + 1; i++) {
            OPERAND_SIZE[i] = 2;
            CONDITIONAL_JUMP[i] = true;
        }
        OPERAND_SIZE[IFNULL] = 2;
        CONDITIONAL_JUMP[IFNULL] = true;
        OPERAND_SIZE[IFNONNULL] = 2;
        CONDITIONAL_JUMP[IFNONNULL] = true;

        OPERAND_SIZE[GOTO] = 2;
        UNCONDITIONAL_JUMP[GOTO] = true;
        OPERAND_SIZE[GOTO_W] = 4;
        UNCONDITIONAL_JUMP[GOTO_W] = true;
        OPERAND_SIZE[JSR] = 2;
        UNCONDITIONAL_JUMP[JSR] = true;
        OPERAND_SIZE[JSR_W] = 4;
        UNCONDITIONAL_JUMP[JSR_W] = true;

        // 变长：switch / wide
        OPERAND_SIZE[TABLESWITCH] = -1;
        OPERAND_SIZE[LOOKUPSWITCH] = -1;
        OPERAND_SIZE[WIDE] = -2;

        // 字段与调用
        for (int i = GETSTATIC; i <= PUTFIELD; i++) {
            OPERAND_SIZE[i] = 2;
            HAS_CONSTANT_INDEX[i] = true;
        }
        for (int i = INVOKEVIRTUAL; i <= INVOKEDYNAMIC; i++) {
            OPERAND_SIZE[i] = (i == INVOKEINTERFACE || i == INVOKEDYNAMIC) ? 4 : 2;
            HAS_CONSTANT_INDEX[i] = true;
        }
        OPERAND_SIZE[NEW] = 2;
        HAS_CONSTANT_INDEX[NEW] = true;
        OPERAND_SIZE[NEWARRAY] = 1;
        OPERAND_SIZE[ANEWARRAY] = 2;
        HAS_CONSTANT_INDEX[ANEWARRAY] = true;
        OPERAND_SIZE[MULTIANEWARRAY] = 3;
        HAS_CONSTANT_INDEX[MULTIANEWARRAY] = true;
        OPERAND_SIZE[CHECKCAST] = 2;
        HAS_CONSTANT_INDEX[CHECKCAST] = true;
        OPERAND_SIZE[INSTANCEOF] = 2;
        HAS_CONSTANT_INDEX[INSTANCEOF] = true;

        HAS_CONSTANT_INDEX[LDC] = true;
        HAS_CONSTANT_INDEX[LDC_W] = true;
        HAS_CONSTANT_INDEX[LDC2_W] = true;

        // 终点指令
        for (int i = IRETURN; i <= RETURN; i++) {
            TERMINATOR[i] = true;
        }
        TERMINATOR[ATHROW] = true;

        String[] names = {
                "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1", "iconst_2", "iconst_3",
                "iconst_4", "iconst_5", "lconst_0", "lconst_1", "fconst_0", "fconst_1", "fconst_2",
                "dconst_0", "dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc2_w",
                "iload", "lload", "fload", "dload", "aload",
                "iload_0", "iload_1", "iload_2", "iload_3",
                "lload_0", "lload_1", "lload_2", "lload_3",
                "fload_0", "fload_1", "fload_2", "fload_3",
                "dload_0", "dload_1", "dload_2", "dload_3",
                "aload_0", "aload_1", "aload_2", "aload_3",
                "iaload", "laload", "faload", "daload", "aaload", "baload", "caload", "saload",
                "istore", "lstore", "fstore", "dstore", "astore",
                "istore_0", "istore_1", "istore_2", "istore_3",
                "lstore_0", "lstore_1", "lstore_2", "lstore_3",
                "fstore_0", "fstore_1", "fstore_2", "fstore_3",
                "dstore_0", "dstore_1", "dstore_2", "dstore_3",
                "astore_0", "astore_1", "astore_2", "astore_3",
                "iastore", "lastore", "fastore", "dastore", "aastore", "bastore", "castore", "sastore",
                "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1", "dup2_x2", "swap",
                "iadd", "ladd", "fadd", "dadd",
                "isub", "lsub", "fsub", "dsub",
                "imul", "lmul", "fmul", "dmul",
                "idiv", "ldiv", "fdiv", "ddiv",
                "irem", "lrem", "frem", "drem",
                "ineg", "lneg", "fneg", "dneg",
                "ishl", "lshl", "ishr", "lshr", "iushr", "lushr",
                "iand", "land", "ior", "lor", "ixor", "lxor",
                "iinc", "i2l", "i2f", "i2d", "l2i", "l2f", "l2d",
                "f2i", "f2l", "f2d", "d2i", "d2l", "d2f",
                "i2b", "i2c", "i2s",
                "lcmp", "fcmpl", "fcmpg", "dcmpl", "dcmpg",
                "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
                "if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt", "if_icmple",
                "if_acmpeq", "if_acmpne",
                "goto", "jsr", "ret", "tableswitch", "lookupswitch",
                "ireturn", "lreturn", "freturn", "dreturn", "areturn", "return",
                "getstatic", "putstatic", "getfield", "putfield",
                "invokevirtual", "invokespecial", "invokestatic", "invokeinterface", "invokedynamic",
                "new", "newarray", "anewarray", "arraylength", "athrow",
                "checkcast", "instanceof", "monitorenter", "monitorexit",
                "wide", "multianewarray", "ifnull", "ifnonnull", "goto_w", "jsr_w"
        };
        for (int i = 0; i < names.length && i < 256; i++) {
            MNEMONICS[i] = names[i];
        }
    }

    /**
     * 操作数总字节数；switch 指令在此返回 -1，需按变长规则单独展开
     *
     * @param opcode 单字节操作码
     * @return int类型返回值
     */
    public static int operandSize(int opcode) {
        return OPERAND_SIZE[opcode & 0xFF];
    }

    /**
     * 操作数是否携带常量池下标
     *
     * @param opcode 单字节操作码
     * @return boolean类型返回值
     */
    public static boolean hasConstantIndex(int opcode) {
        return HAS_CONSTANT_INDEX[opcode & 0xFF];
    }

    /**
     * 是否条件跳转
     *
     * @param opcode 单字节操作码
     * @return boolean类型返回值
     */
    public static boolean isConditionalJump(int opcode) {
        return CONDITIONAL_JUMP[opcode & 0xFF];
    }

    /**
     * 是否无条件跳转（含 jsr）
     *
     * @param opcode 单字节操作码
     * @return boolean类型返回值
     */
    public static boolean isUnconditionalJump(int opcode) {
        return UNCONDITIONAL_JUMP[opcode & 0xFF];
    }

    /**
     * 是否任何形式的跳转
     *
     * @param opcode 单字节操作码
     * @return boolean类型返回值
     */
    public static boolean isJump(int opcode) {
        return isConditionalJump(opcode) || isUnconditionalJump(opcode)
                || (opcode & 0xFF) == TABLESWITCH || (opcode & 0xFF) == LOOKUPSWITCH;
    }

    /**
     * 是否终结指令（return / athrow），其后不再有可达指令
     *
     * @param opcode 单字节操作码
     * @return boolean类型返回值
     */
    public static boolean isTerminator(int opcode) {
        return TERMINATOR[opcode & 0xFF];
    }

    /**
     * 助记符
     *
     * @param opcode 单字节操作码
     * @return String类型返回值
     */
    public static String mnemonic(int opcode) {
        return MNEMONICS[opcode & 0xFF];
    }

    /**
     * 条件跳转的逆操作数（if_icmpeq ↔ if_icmpne 等），改写比较方向时使用
     *
     * @param opcode 单字节操作码
     * @return int类型返回值；非条件跳转返回 -1
     */
    public static int negateCondition(int opcode) {
        switch (opcode & 0xFF) {
            case IFEQ:
                return 0x9a;
            case 0x9a:
                return IFEQ;
            case 0x9b:
                return 0x9c;
            case 0x9c:
                return 0x9b;
            case 0x9d:
                return 0x9e;
            case 0x9e:
                return 0x9d;
            case 0x9f:
                return 0xa0;
            case 0xa0:
                return 0x9f;
            case 0xa1:
                return 0xa2;
            case 0xa2:
                return 0xa1;
            case 0xa3:
                return 0xa4;
            case 0xa4:
                return 0xa3;
            case 0xa5:
                return 0xa6;
            case 0xa6:
                return 0xa5;
            case IFNULL:
                return IFNONNULL;
            case IFNONNULL:
                return IFNULL;
            default:
                return -1;
        }
    }
}
