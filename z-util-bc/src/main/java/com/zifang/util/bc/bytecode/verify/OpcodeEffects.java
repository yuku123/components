package com.zifang.util.bc.bytecode.verify;

import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;

import java.util.List;

/**
 * 单条指令对栈与局部变量的作用
 * <p>
 * 从帧计算的数据流里拆出来，是因为这份「什么指令弹几个、压几个」的表本身就是
 * JVMS §6.5 的逐条翻译，和不动点迭代是两件事。放在这里也让帧计算器只管传播。
 * <p>
 * cat1/cat2 的区分贯穿全表：LONG/DOUBLE 是 cat2，其余是 cat1，
 * 凡是会绕 cat1 的地方（JVM 规定的 dup 家族、pop2）都必须显式区分栈顶宽度。
 */
final class OpcodeEffects {

    private OpcodeEffects() {
    }

    /**
     * 栈与局部变量的读写端口，由 {@link FrameComputer} 实现
     */
    interface StackOps {

        void push(VerificationType type);

        VerificationType pop(int pc);

        VerificationType popCat1(int pc);

        void popSlots(int slots, int pc);

        List<VerificationType> popValues(int slots, int pc);

        boolean topIsWide();

        /** 次栈顶是否为 cat2（dup2_x2 区分栈型用） */
        boolean secondIsWide();

        /** 当前栈深 */
        int stackDepth();

        VerificationType loadLocal(int slot, int pc);

        void storeLocal(int slot, VerificationType type);

        /** 描述符类型 → 校验类型 */
        VerificationType typeOf(String descriptor);

        /** ldc 的常量池项 → 栈上类型 */
        VerificationType constantType(int cpIndex, int pc);

        /** newarray 压入的数组引用类型 */
        VerificationType arrayType(int atype);

        /** getstatic/getfield */
        void pushFieldType(int cpIndex, int pc);

        /** putstatic/putfield 弹掉值 */
        void popFieldType(int cpIndex, int pc);

        /** 五条 invoke：弹参、弹接收者、压返回值 */
        void invoke(int opcode, int cpIndex, int pc);
    }

    /**
     * 应用一条指令
     *
     * @param opcode 操作码
     * @param insn   指令视图
     * @param pc     指令偏移
     * @param ops    栈端口
     */
    static void apply(int opcode, Instruction insn, int pc, StackOps ops) {
        // 定长 load/store 家族各自按 0..3 连续排布，槽号是组内偏移而不是减去组首
        if (opcode >= Opcodes.ILOAD_0 && opcode <= Opcodes.ALOAD_3) {
            int slot = (opcode - Opcodes.ILOAD_0) % 4;
            switch (opcode) {
                case Opcodes.ILOAD_0:
                case Opcodes.ILOAD_1:
                case Opcodes.ILOAD_2:
                case Opcodes.ILOAD_3:
                    ops.push(VerificationType.integer());
                    return;
                case Opcodes.LLOAD_0:
                case Opcodes.LLOAD_1:
                case Opcodes.LLOAD_2:
                case Opcodes.LLOAD_3:
                    ops.push(VerificationType.longType());
                    return;
                case Opcodes.FLOAD_0:
                case Opcodes.FLOAD_1:
                case Opcodes.FLOAD_2:
                case Opcodes.FLOAD_3:
                    ops.push(VerificationType.floatType());
                    return;
                case Opcodes.DLOAD_0:
                case Opcodes.DLOAD_1:
                case Opcodes.DLOAD_2:
                case Opcodes.DLOAD_3:
                    ops.push(VerificationType.doubleType());
                    return;
                default:
                    // aload 压的是局部变量里的实际类型，可能带未初始化信息
                    ops.push(ops.loadLocal(slot, pc));
                    return;
            }
        }
        if (opcode >= Opcodes.ISTORE_0 && opcode <= Opcodes.ASTORE_3) {
            int slot = (opcode - Opcodes.ISTORE_0) % 4;
            switch (opcode) {
                case Opcodes.LSTORE_0:
                case Opcodes.LSTORE_1:
                case Opcodes.LSTORE_2:
                case Opcodes.LSTORE_3:
                case Opcodes.DSTORE_0:
                case Opcodes.DSTORE_1:
                case Opcodes.DSTORE_2:
                case Opcodes.DSTORE_3:
                    ops.storeLocal(slot, ops.pop(pc));
                    return;
                default:
                    ops.storeLocal(slot, ops.popCat1(pc));
                    return;
            }
        }

        switch (opcode) {
            // ---------------- 与栈无关
            case Opcodes.NOP:
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
            case Opcodes.IINC:
            case Opcodes.GOTO:
            case Opcodes.GOTO_W:
            case Opcodes.JSR:
            case Opcodes.JSR_W:
            case Opcodes.RET:
            case Opcodes.RETURN:
                return;
            // ---------------- 常量
            case Opcodes.ACONST_NULL:
                ops.push(VerificationType.nullType());
                return;
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                ops.push(VerificationType.integer());
                return;
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
                ops.push(VerificationType.longType());
                return;
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
                ops.push(VerificationType.doubleType());
                return;
            case Opcodes.LDC:
                // ldc 的下标只有一个字节，用 u2 读会把下一条指令的操作码也读进来
                ops.push(ops.constantType(insn.u1Operand(), pc));
                return;
            case Opcodes.LDC_W:
            case Opcodes.LDC2_W:
                ops.push(ops.constantType(insn.u2Operand(), pc));
                return;
            // ---------------- 带下标的 load/store
            case Opcodes.ILOAD:
                ops.push(VerificationType.integer());
                return;
            case Opcodes.LLOAD:
                ops.push(VerificationType.longType());
                return;
            case Opcodes.FLOAD:
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.DLOAD:
                ops.push(VerificationType.doubleType());
                return;
            case Opcodes.ALOAD:
                ops.push(ops.loadLocal(insn.byteOperand(), pc));
                return;
            case Opcodes.ISTORE:
            case Opcodes.FSTORE:
            case Opcodes.ASTORE:
                ops.storeLocal(insn.byteOperand(), ops.popCat1(pc));
                return;
            case Opcodes.LSTORE:
            case Opcodes.DSTORE:
                ops.storeLocal(insn.byteOperand(), ops.pop(pc));
                return;
            // ---------------- 数组
            case Opcodes.IALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
                ops.popValues(2, pc);
                ops.push(VerificationType.integer());
                return;
            case Opcodes.LALOAD:
                ops.popValues(2, pc);
                ops.push(VerificationType.longType());
                return;
            case Opcodes.FALOAD:
                ops.popValues(2, pc);
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.DALOAD:
                ops.popValues(2, pc);
                ops.push(VerificationType.doubleType());
                return;
            case Opcodes.AALOAD:
                // 弹下标与数组引用；元素类型沿用数组的引用类型（精度由校验器按规则放宽）
                ops.popCat1(pc);
                ops.popCat1(pc);
                ops.push(VerificationType.top());
                return;
            case Opcodes.LASTORE:
            case Opcodes.DASTORE:
                // 被存的值是 cat2，加上数组引用与下标共四个槽
                ops.popValues(4, pc);
                return;
            case Opcodes.IASTORE:
            case Opcodes.FASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                ops.popValues(3, pc);
                return;
            // ---------------- 栈整形
            case Opcodes.POP:
                ops.popCat1(pc);
                return;
            case Opcodes.POP2:
                if (ops.topIsWide()) {
                    ops.pop(pc);
                } else {
                    ops.popCat1(pc);
                    ops.popCat1(pc);
                }
                return;
            case Opcodes.DUP: {
                VerificationType v1 = ops.popCat1(pc);
                ops.push(v1);
                ops.push(v1);
                return;
            }
            case Opcodes.DUP_X1: {
                List<VerificationType> v = ops.popValues(2, pc);
                ops.push(v.get(1));
                ops.push(v.get(0));
                ops.push(v.get(1));
                return;
            }
            case Opcodes.DUP_X2: {
                // cat1+cat1 与 cat2+cat1 两种栈型产出相同
                List<VerificationType> v = ops.popValues(3, pc);
                ops.push(v.get(1));
                ops.push(v.get(0));
                ops.push(v.get(1));
                return;
            }
            case Opcodes.DUP2: {
                if (ops.topIsWide()) {
                    VerificationType v1 = ops.pop(pc);
                    ops.push(v1);
                    ops.push(v1);
                } else {
                    List<VerificationType> v = ops.popValues(2, pc);
                    ops.push(v.get(0));
                    ops.push(v.get(1));
                    ops.push(v.get(0));
                    ops.push(v.get(1));
                }
                return;
            }
            case Opcodes.DUP2_X1: {
                if (ops.topIsWide()) {
                    VerificationType v1 = ops.pop(pc);
                    VerificationType v2 = ops.popCat1(pc);
                    ops.push(v1);
                    ops.push(v2);
                    ops.push(v1);
                } else {
                    List<VerificationType> v = ops.popValues(3, pc);
                    ops.push(v.get(1));
                    ops.push(v.get(0));
                    ops.push(v.get(2));
                    ops.push(v.get(1));
                    ops.push(v.get(0));
                }
                return;
            }
            case Opcodes.DUP2_X2:
                dup2x2(pc, ops);
                return;
            case Opcodes.SWAP: {
                VerificationType v1 = ops.popCat1(pc);
                VerificationType v2 = ops.popCat1(pc);
                ops.push(v1);
                ops.push(v2);
                return;
            }
            // ---------------- 整数运算
            case Opcodes.IADD:
            case Opcodes.ISUB:
            case Opcodes.IMUL:
            case Opcodes.IDIV:
            case Opcodes.IREM:
            case Opcodes.ISHL:
            case Opcodes.ISHR:
            case Opcodes.IUSHR:
            case Opcodes.IAND:
            case Opcodes.IOR:
            case Opcodes.IXOR:
                ops.popValues(2, pc);
                ops.push(VerificationType.integer());
                return;
            // ---------------- 长整数运算
            case Opcodes.LADD:
            case Opcodes.LSUB:
            case Opcodes.LMUL:
            case Opcodes.LDIV:
            case Opcodes.LREM:
            case Opcodes.LAND:
            case Opcodes.LOR:
            case Opcodes.LXOR:
                ops.popValues(4, pc);
                ops.push(VerificationType.longType());
                return;
            case Opcodes.LSHL:
            case Opcodes.LSHR:
            case Opcodes.LUSHR:
                // 移位量是 int，与被移位的 long 合计 3 槽
                ops.popCat1(pc);
                ops.popSlots(2, pc);
                ops.push(VerificationType.longType());
                return;
            // ---------------- 浮点与双精度
            case Opcodes.FADD:
            case Opcodes.FSUB:
            case Opcodes.FMUL:
            case Opcodes.FDIV:
            case Opcodes.FREM:
                ops.popValues(2, pc);
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.DADD:
            case Opcodes.DSUB:
            case Opcodes.DMUL:
            case Opcodes.DDIV:
            case Opcodes.DREM:
                ops.popValues(4, pc);
                ops.push(VerificationType.doubleType());
                return;
            // ---------------- 转换
            case Opcodes.I2L:
            case Opcodes.F2L:
                ops.popCat1(pc);
                ops.push(VerificationType.longType());
                return;
            case Opcodes.I2D:
            case Opcodes.F2D:
                ops.popCat1(pc);
                ops.push(VerificationType.doubleType());
                return;
            case Opcodes.I2F:
                ops.popCat1(pc);
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.L2I:
            case Opcodes.D2I:
            case Opcodes.F2I:
                ops.popSlots(slotsOfSource(opcode), pc);
                ops.push(VerificationType.integer());
                return;
            case Opcodes.L2F:
            case Opcodes.D2F:
                ops.popSlots(2, pc);
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.L2D:
                ops.popSlots(2, pc);
                ops.push(VerificationType.doubleType());
                return;
            case Opcodes.D2L:
                ops.popSlots(2, pc);
                ops.push(VerificationType.longType());
                return;
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                ops.popCat1(pc);
                ops.push(VerificationType.integer());
                return;
            // ---------------- 比较
            case Opcodes.LCMP:
                ops.popValues(4, pc);
                ops.push(VerificationType.integer());
                return;
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
                ops.popValues(2, pc);
                ops.push(VerificationType.integer());
                return;
            case Opcodes.DCMPL:
            case Opcodes.DCMPG:
                ops.popValues(4, pc);
                ops.push(VerificationType.integer());
                return;
            // ---------------- 跳转与返回
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                ops.popCat1(pc);
                return;
            case Opcodes.IF_ICMPEQ:
            case Opcodes.IF_ICMPNE:
            case Opcodes.IF_ICMPLT:
            case Opcodes.IF_ICMPGE:
            case Opcodes.IF_ICMPGT:
            case Opcodes.IF_ICMPLE:
            case Opcodes.IF_ACMPEQ:
            case Opcodes.IF_ACMPNE:
                ops.popValues(2, pc);
                return;
            case Opcodes.TABLESWITCH:
            case Opcodes.LOOKUPSWITCH:
                ops.popCat1(pc);
                return;
            case Opcodes.IRETURN:
            case Opcodes.FRETURN:
            case Opcodes.ARETURN:
            case Opcodes.ATHROW:
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
                ops.popCat1(pc);
                return;
            case Opcodes.LRETURN:
            case Opcodes.DRETURN:
                ops.popSlots(2, pc);
                return;
            case Opcodes.ARRAYLENGTH:
            case Opcodes.INSTANCEOF:
                ops.popCat1(pc);
                ops.push(VerificationType.integer());
                return;
            // ---------------- 字段与调用
            case Opcodes.GETSTATIC:
                ops.pushFieldType(insn.u2Operand(), pc);
                return;
            case Opcodes.PUTSTATIC:
                ops.popFieldType(insn.u2Operand(), pc);
                return;
            case Opcodes.GETFIELD:
                ops.popCat1(pc);
                ops.pushFieldType(insn.u2Operand(), pc);
                return;
            case Opcodes.PUTFIELD:
                ops.popFieldType(insn.u2Operand(), pc);
                ops.popCat1(pc);
                return;
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEINTERFACE:
            case Opcodes.INVOKEDYNAMIC:
                ops.invoke(opcode, insn.u2Operand(), pc);
                return;
            // ---------------- 对象与数组创建
            case Opcodes.NEW:
                // new 不消费任何值，直接压一个指向本条指令的未初始化类型
                ops.push(VerificationType.uninitialized(pc));
                return;
            case Opcodes.NEWARRAY:
                // 压的是数组本身的引用类型，元素类型不体现在栈上
                ops.popCat1(pc);
                ops.push(ops.arrayType(insn.byteOperand()));
                return;
            case Opcodes.ANEWARRAY:
                ops.popCat1(pc);
                ops.push(VerificationType.object(insn.u2Operand()));
                return;
            case Opcodes.MULTIANEWARRAY:
                // 三字节指令：u2 是类下标，第三个字节才是维度数
                ops.popValues(insn.operandByte(2), pc);
                ops.push(VerificationType.object(insn.u2Operand()));
                return;
            case Opcodes.CHECKCAST:
                ops.popCat1(pc);
                ops.push(VerificationType.object(insn.u2Operand()));
                return;
            case Opcodes.WIDE:
                applyWide(insn, pc, ops);
                return;
            default:
                throw new ClassVerifyException("帧计算暂不支持的指令: " + Opcodes.mnemonic(opcode)
                        + " (0x" + Integer.toHexString(opcode) + ")", pc);
        }
    }

    private static int slotsOfSource(int opcode) {
        return opcode == Opcodes.F2I ? 1 : 2;
    }

    /**
     * dup2_x2 的四种栈型（JVMS §6.5.dup2_x2）
     */
    private static void dup2x2(int pc, StackOps ops) {
        if (ops.stackDepth() < 2) {
            throw new ClassVerifyException("dup2_x2 操作数栈不足", pc);
        }
        if (ops.topIsWide() && ops.secondIsWide()) {
            VerificationType v1 = ops.pop(pc);
            VerificationType v2 = ops.pop(pc);
            ops.push(v1);
            ops.push(v2);
            ops.push(v1);
            return;
        }
        if (ops.topIsWide()) {
            // ..., value2(cat1), value1(cat2)
            VerificationType v1 = ops.pop(pc);
            VerificationType v2 = ops.popCat1(pc);
            ops.push(v1);
            ops.push(v2);
            ops.push(v1);
            return;
        }
        if (ops.secondIsWide()) {
            // ..., value3(cat2), value2(cat1), value1(cat1)
            List<VerificationType> v = ops.popValues(3, pc);
            ops.push(v.get(1));
            ops.push(v.get(0));
            ops.push(v.get(1));
            ops.push(v.get(0));
            ops.push(v.get(1));
            return;
        }
        if (ops.stackDepth() < 4) {
            throw new ClassVerifyException("dup2_x2 操作数栈不足（需要四个 cat1）", pc);
        }
        // ..., value3, value2, value1, value4
        List<VerificationType> v = ops.popValues(4, pc);
        ops.push(v.get(2));
        ops.push(v.get(1));
        ops.push(v.get(3));
        ops.push(v.get(2));
        ops.push(v.get(1));
        ops.push(v.get(0));
    }

    /**
     * wide 前缀：下标变成 u2
     */
    private static void applyWide(Instruction insn, int pc, StackOps ops) {
        int widened = insn.byteOperand() & 0xFF;
        int index = insn.shortOperand();
        switch (widened) {
            case Opcodes.ILOAD:
                ops.push(VerificationType.integer());
                return;
            case Opcodes.LLOAD:
                ops.push(VerificationType.longType());
                return;
            case Opcodes.FLOAD:
                ops.push(VerificationType.floatType());
                return;
            case Opcodes.DLOAD:
                ops.push(VerificationType.doubleType());
                return;
            case Opcodes.ALOAD:
                ops.push(ops.loadLocal(index, pc));
                return;
            case Opcodes.ISTORE:
            case Opcodes.FSTORE:
            case Opcodes.ASTORE:
                ops.storeLocal(index, ops.popCat1(pc));
                return;
            case Opcodes.LSTORE:
            case Opcodes.DSTORE:
                ops.storeLocal(index, ops.pop(pc));
                return;
            case Opcodes.RET:
            case Opcodes.IINC:
                return;
            default:
                throw new ClassVerifyException("wide 修饰了非法操作码: 0x" + Integer.toHexString(widened), pc);
        }
    }
}