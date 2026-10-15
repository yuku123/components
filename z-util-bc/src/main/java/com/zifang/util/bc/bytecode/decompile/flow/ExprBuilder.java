package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.decompile.core.ParamsConvertor;
import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.constantpool.AbstractConstantPool;
import com.zifang.util.bc.bytecode.model.constantpool.ClassInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantDoubleInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantFloatInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantIntegerInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantLongInfo;
import com.zifang.util.bc.bytecode.model.constantpool.ConstantStringInfo;
import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 栈机模拟器
 * <p>
 * 把一段直线指令按操作数栈语义推完，产出 {@link Stmt} 列表：调用、赋值、返回
 * 都在这里从「谁压在谁上面」还原成树。控制转移指令本身不消费，条件值留给
 * {@link Structurer} 取走去拼 if/while。
 * <p>
 * 局部变量表只在能按作用域定位名字时用；取不到就退化成 {@code varN}，
 * 宁可名字难看也不猜错。
 */
public class ExprBuilder {

    private final ConstantPoolHelper pool;
    private final LocalVarNames locals;
    private final Deque<Expr> stack = new ArrayDeque<>();
    private final Set<Integer> declaredSlots = new HashSet<>();

    public ExprBuilder(ControlFlowGraph cfg, LocalVarNames locals) {
        this.pool = new ConstantPoolHelper(cfg.getCode().getPoolList());
        this.locals = locals;
    }

    /**
     * 模拟一段直线指令
     *
     * @param insns 指令序列
     * @return Stmt列表类型返回值
     */
    public List<Stmt> run(List<Instruction> insns) {
        List<Stmt> out = new ArrayList<>();
        for (Instruction insn : insns) {
            step(insn, out);
        }
        return out;
    }

    private void step(Instruction insn, List<Stmt> out) {
        int op = insn.getOpcode();
        switch (op) {
            case Opcodes.NOP:
                return;
            case Opcodes.ACONST_NULL:
                push(Expr.Ref.nullRef());
                return;
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
                push(Expr.Literal.ofNumber(op - Opcodes.ICONST_0));
                return;
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
                push(Expr.Literal.ofNumber(op - Opcodes.LCONST_0));
                return;
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
                push(Expr.Literal.ofNumber(op - Opcodes.FCONST_0));
                return;
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
                push(Expr.Literal.ofNumber(op - Opcodes.DCONST_0));
                return;
            case Opcodes.BIPUSH:
            case Opcodes.SIPUSH:
                push(Expr.Literal.ofNumber(insn.shortOperand()));
                return;
            case Opcodes.LDC:
                push(constant(insn.u1Operand()));
                return;
            case Opcodes.LDC_W:
            case Opcodes.LDC2_W:
                push(constant(insn.u2Operand()));
                return;
            case Opcodes.ALOAD_0:
            case Opcodes.ALOAD_1:
            case Opcodes.ALOAD_2:
            case Opcodes.ALOAD_3: {
                int slot = op - Opcodes.ALOAD_0;
                push(locals.isThis(slot)
                        ? Expr.Ref.thisRef()
                        : new Expr.Local(locals.name(slot, insn.getPc()), null));
                return;
            }
            case Opcodes.ILOAD:
            case Opcodes.LLOAD:
            case Opcodes.FLOAD:
            case Opcodes.DLOAD:
            case Opcodes.ALOAD: {
                int slot = insn.u1Operand();
                push(locals.isThis(slot)
                        ? Expr.Ref.thisRef()
                        : new Expr.Local(locals.name(slot, insn.getPc()), null));
                return;
            }
            case Opcodes.ILOAD_0:
            case Opcodes.ILOAD_1:
            case Opcodes.ILOAD_2:
            case Opcodes.ILOAD_3:
            case Opcodes.LLOAD_0:
            case Opcodes.LLOAD_1:
            case Opcodes.LLOAD_2:
            case Opcodes.LLOAD_3:
            case Opcodes.FLOAD_0:
            case Opcodes.FLOAD_1:
            case Opcodes.FLOAD_2:
            case Opcodes.FLOAD_3:
            case Opcodes.DLOAD_0:
            case Opcodes.DLOAD_1:
            case Opcodes.DLOAD_2:
            case Opcodes.DLOAD_3:
                push(new Expr.Local(locals.name(op - Opcodes.ILOAD_0, insn.getPc()), null));
                return;
            default:
                break;
        }
        if (stepArithmetic(insn, out)) {
            return;
        }
        if (stepMember(insn, out)) {
            return;
        }
        out.add(new Stmt.Comment(Opcodes.mnemonic(op)));
    }

    // ------------------------------------------------------------ 运算与栈

    private boolean stepArithmetic(Instruction insn, List<Stmt> out) {
        int op = insn.getOpcode();
        String binary = binaryOperator(op);
        if (binary != null) {
            Expr right = pop();
            Expr left = pop();
            push(new Expr.Binary(binary, left, right));
            return true;
        }
        String unary = unaryOperator(op);
        if (unary != null) {
            push(new Expr.Unary(unary, pop()));
            return true;
        }
        switch (op) {
            case Opcodes.LCMP:
            case Opcodes.FCMPL:
            case Opcodes.FCMPG:
            case Opcodes.DCMPL:
            case Opcodes.DCMPG: {
                Expr right = pop();
                Expr left = pop();
                push(new Expr.Unknown("compare " + Opcodes.mnemonic(op)));
                return true;
            }
            case Opcodes.I2L:
            case Opcodes.I2F:
            case Opcodes.I2D:
            case Opcodes.L2I:
            case Opcodes.L2F:
            case Opcodes.L2D:
            case Opcodes.F2I:
            case Opcodes.F2L:
            case Opcodes.F2D:
            case Opcodes.D2I:
            case Opcodes.D2L:
            case Opcodes.D2F:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                // 隐式加宽在 Java 源码里由类型系统隐含，显式 cast 会盖住真实语义
                push(pop());
                return true;
            case Opcodes.POP:
            case Opcodes.POP2:
                out.add(new Stmt.ExprStmt(pop()));
                return true;
            case Opcodes.DUP:
            case Opcodes.DUP_X1:
            case Opcodes.DUP_X2:
            case Opcodes.DUP2:
            case Opcodes.DUP2_X1:
            case Opcodes.DUP2_X2:
            case Opcodes.SWAP: {
                Expr top = stack.peek();
                if (top != null) {
                    push(top);
                }
                return true;
            }
            case Opcodes.ARRAYLENGTH:
                push(new Expr.Unknown("length"));
                return true;
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.FALOAD:
            case Opcodes.DALOAD:
            case Opcodes.AALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD: {
                Expr index = pop();
                push(new Expr.ArrayAccess(pop(), index));
                return true;
            }
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE: {
                Expr value = pop();
                Expr index = pop();
                out.add(new Stmt.Assign(new Expr.ArrayAccess(pop(), index), value));
                return true;
            }
            case Opcodes.CHECKCAST:
                push(new Expr.Cast(typeOfClass(insn.u2Operand()), pop()));
                return true;
            case Opcodes.INSTANCEOF:
                push(new Expr.InstanceOf(pop(), typeOfClass(insn.u2Operand())));
                return true;
            case Opcodes.NEW:
                push(new Expr.New(typeOfClass(insn.u2Operand()), null));
                return true;
            case Opcodes.NEWARRAY:
                push(new Expr.NewArray(primitiveArrayType(insn.u1Operand()), oneDim(pop())));
                return true;
            case Opcodes.ANEWARRAY:
                push(new Expr.NewArray(typeOfClass(insn.u2Operand()), oneDim(pop())));
                return true;
            case Opcodes.MULTIANEWARRAY: {
                List<Expr> dims = new ArrayList<>();
                for (int i = 0; i < insn.u1Operand(); i++) {
                    dims.add(pop());
                }
                Collections.reverse(dims);
                push(new Expr.NewArray(typeOfClass(insn.u2Operand()), dims));
                return true;
            }
            case Opcodes.IINC:
                // iinc 的操作数是 [槽位 u1][增量 s1]，增量不是第一个字节
                out.add(new Stmt.Assign(plainLocal(insn.u1Operand()), "+=",
                        Expr.Literal.ofNumber((byte) insn.operandByte(1))));
                return true;
            case Opcodes.RETURN:
                out.add(new Stmt.Return(null));
                return true;
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
                out.add(new Stmt.Return(pop()));
                return true;
            case Opcodes.ATHROW:
                out.add(new Stmt.Throw(pop()));
                return true;
            default:
                return false;
        }
    }

    // ------------------------------------------------------------ 字段、调用与局部变量

    private boolean stepMember(Instruction insn, List<Stmt> out) {
        int op = insn.getOpcode();
        switch (op) {
            case Opcodes.GETSTATIC:
                push(new Expr.Field(null, fieldName(insn.u2Operand())));
                return true;
            case Opcodes.GETFIELD:
                push(new Expr.Field(pop(), fieldName(insn.u2Operand())));
                return true;
            case Opcodes.PUTSTATIC:
                out.add(new Stmt.Assign(new Expr.Field(null, fieldName(insn.u2Operand())), pop()));
                return true;
            case Opcodes.PUTFIELD: {
                Expr value = pop();
                out.add(new Stmt.Assign(new Expr.Field(pop(), fieldName(insn.u2Operand())), value));
                return true;
            }
            case Opcodes.ISTORE:
            case Opcodes.LSTORE:
            case Opcodes.FSTORE:
            case Opcodes.DSTORE:
            case Opcodes.ASTORE:
                store(insn.u1Operand(), insn.getPc(), pop(), out);
                return true;
            case Opcodes.ISTORE_0:
            case Opcodes.ISTORE_1:
            case Opcodes.ISTORE_2:
            case Opcodes.ISTORE_3:
            case Opcodes.LSTORE_0:
            case Opcodes.LSTORE_1:
            case Opcodes.LSTORE_2:
            case Opcodes.LSTORE_3:
            case Opcodes.FSTORE_0:
            case Opcodes.FSTORE_1:
            case Opcodes.FSTORE_2:
            case Opcodes.FSTORE_3:
            case Opcodes.DSTORE_0:
            case Opcodes.DSTORE_1:
            case Opcodes.DSTORE_2:
            case Opcodes.DSTORE_3:
            case Opcodes.ASTORE_0:
            case Opcodes.ASTORE_1:
            case Opcodes.ASTORE_2:
            case Opcodes.ASTORE_3:
                store(op - Opcodes.ISTORE_0, insn.getPc(), pop(), out);
                return true;
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
            case Opcodes.INVOKESTATIC:
            case Opcodes.INVOKEINTERFACE:
            case Opcodes.INVOKEDYNAMIC:
                return invoke(insn, out);
            case Opcodes.MONITORENTER:
            case Opcodes.MONITOREXIT:
                pop();
                return true;
            default:
                return false;
        }
    }

    private boolean invoke(Instruction insn, List<Stmt> out) {
        int op = insn.getOpcode();
        String descriptor = methodDescriptor(insn.u2Operand());
        boolean staticCall = op == Opcodes.INVOKESTATIC || op == Opcodes.INVOKEDYNAMIC;
        Expr owner = staticCall ? null : pop();
        List<Expr> args = new ArrayList<>();
        int slots = argumentSlots(descriptor);
        for (int i = 0; i < slots; i++) {
            args.add(0, pop());
        }
        String methodName = methodName(insn.u2Operand());

        if ("<init>".equals(methodName)) {
            if (owner instanceof Expr.New) {
                Expr.New created = (Expr.New) owner;
                // 栈对不齐时 pop 可能把 new 自己弹回来，直接塞进参数表会形成自引用环
                for (Expr arg : args) {
                    if (arg != created) {
                        created.getArgs().add(arg);
                    }
                }
            }
            if (owner == null) {
                push(new Expr.Unknown("super(" + Stmt.joinExprs(args) + ")"));
            } else {
                push(owner);
            }
            return true;
        }
        Expr call = new Expr.Invoke(staticCall ? staticOwner(insn.u2Operand()) : owner,
                methodName, args);
        if (isVoid(descriptor)) {
            out.add(new Stmt.ExprStmt(call));
        } else {
            push(call);
        }
        return true;
    }

    /**
     * 局部变量存储：槽位在该 pc 上是首次赋值且带名字时渲染成声明，否则是纯赋值
     */
    private void store(int slot, int pc, Expr value, List<Stmt> out) {
        if (locals.isThis(slot)) {
            out.add(new Stmt.Comment("this store"));
            return;
        }
        String name = locals.name(slot, pc);
        if (locals.declaredAt(slot, pc) && declaredSlots.add(slot)) {
            out.add(new Stmt.Decl(locals.type(slot, pc), name, value));
        } else {
            out.add(new Stmt.Assign(plainLocal(slot), value));
        }
    }

    private Expr plainLocal(int slot) {
        return new Expr.Local(locals.name(slot, -1), null);
    }

    // ------------------------------------------------------------ 常量池解析

    private Expr constant(int index) {
        AbstractConstantPool entry = pool.get(index);
        if (entry instanceof ConstantIntegerInfo) {
            return Expr.Literal.ofNumber(((ConstantIntegerInfo) entry).getBytes().getValue());
        }
        if (entry instanceof ConstantLongInfo) {
            return new Expr.Literal(((ConstantLongInfo) entry).getBytes().getValue() + "L");
        }
        if (entry instanceof ConstantFloatInfo) {
            return new Expr.Literal(((ConstantFloatInfo) entry).getBytes().getValue() + "f");
        }
        if (entry instanceof ConstantDoubleInfo) {
            return new Expr.Literal(String.valueOf(((ConstantDoubleInfo) entry).getBytes().getValue()));
        }
        if (entry instanceof ConstantStringInfo) {
            String literal = pool.stringLiteral(index);
            return new Expr.Literal(literal == null ? "\"\"" : quote(literal));
        }
        if (entry instanceof ClassInfo) {
            String internal = pool.utf8(((ClassInfo) entry).getNameIndex().intValue());
            if (internal != null && internal.startsWith("[")) {
                return new Expr.Literal(ParamsConvertor.paramsConvertorFieldType(internal) + ".class");
            }
            return new Expr.Literal(simple(internal) + ".class");
        }
        return new Expr.Unknown("constant #" + index);
    }

    /**
     * 字符串字面量转义：只挡会破坏字面量的字符，不做 Unicode 规范化
     */
    static String quote(String raw) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    private String typeOfClass(int classIndex) {
        String internal = pool.classInternalName(classIndex);
        if (internal == null) {
            return "Object";
        }
        if (internal.startsWith("[")) {
            return ParamsConvertor.paramsConvertorFieldType(internal);
        }
        return simple(internal);
    }

    private String fieldName(int ref) {
        String name = pool.fieldName(ref);
        return name == null ? "/* field */" : name;
    }

    /**
     * 静态调用带上宿主类型名：只写方法名会与同名实例方法混淆
     */
    private Expr staticOwner(int ref) {
        String owner = pool.methodOwner(ref);
        return owner == null ? null : new Expr.Ref(simple(owner));
    }

    private String methodName(int ref) {
        String name = pool.methodName(ref);
        return name == null ? "/* method */" : name;
    }

    private String methodDescriptor(int ref) {
        String descriptor = pool.methodDescriptor(ref);
        return descriptor == null ? "()V" : descriptor;
    }

    static String simple(String internalName) {
        return internalName == null ? "Object"
                : internalName.substring(internalName.lastIndexOf('/') + 1);
    }

    private static List<Expr> oneDim(Expr dim) {
        List<Expr> dims = new ArrayList<>();
        dims.add(dim);
        return dims;
    }

    static String binaryOperator(int opcode) {
        switch (opcode) {
            case Opcodes.IADD:
            case Opcodes.LADD:
            case Opcodes.FADD:
            case Opcodes.DADD:
                return "+";
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
                return "-";
            case Opcodes.IMUL:
            case Opcodes.LMUL:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
                return "*";
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
                return "/";
            case Opcodes.IREM:
            case Opcodes.LREM:
            case Opcodes.FREM:
            case Opcodes.DREM:
                return "%";
            case Opcodes.ISHL:
            case Opcodes.LSHL:
                return "<<";
            case Opcodes.ISHR:
            case Opcodes.LSHR:
                return ">>";
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
                return ">>>";
            case Opcodes.IAND:
            case Opcodes.LAND:
                return "&";
            case Opcodes.IOR:
            case Opcodes.LOR:
                return "|";
            case Opcodes.IXOR:
            case Opcodes.LXOR:
                return "^";
            default:
                return null;
        }
    }

    static String unaryOperator(int opcode) {
        switch (opcode) {
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
                return "-";
            default:
                return null;
        }
    }

    private static String primitiveArrayType(int operand) {
        switch (operand) {
            case 4:
                return "boolean";
            case 5:
                return "char";
            case 6:
                return "float";
            case 7:
                return "double";
            case 8:
                return "byte";
            case 9:
                return "short";
            case 10:
                return "int";
            case 11:
                return "long";
            default:
                return "int";
        }
    }

    static boolean isVoid(String descriptor) {
        int close = descriptor.lastIndexOf(')');
        return close >= 0 && close + 1 < descriptor.length() && descriptor.charAt(close + 1) == 'V';
    }

    /**
     * 方法参数占几个栈槽（long/double 各占两个），决定弹栈次数
     */
    static int argumentSlots(String descriptor) {
        String params = parameterDescriptors(descriptor);
        int slots = 0;
        int i = 0;
        while (i < params.length()) {
            char c = params.charAt(i);
            if (c == 'J' || c == 'D') {
                slots += 2;
                i++;
            } else if (c == 'L') {
                i = params.indexOf(';', i) + 1;
                slots++;
            } else {
                i++;
                slots++;
            }
        }
        return slots;
    }

    private static String parameterDescriptors(String descriptor) {
        int open = descriptor.indexOf('(');
        int close = descriptor.indexOf(')');
        if (open < 0 || close < open) {
            return "";
        }
        return descriptor.substring(open + 1, close);
    }

    // ------------------------------------------------------------ 栈操作

    public void push(Expr expr) {
        stack.push(expr);
    }

    public Expr pop() {
        return stack.isEmpty() ? new Expr.Unknown("stack") : stack.pop();
    }

    public int stackDepth() {
        return stack.size();
    }

    /**
     * 条件跳转要弹 1 个（ifeq 这类单值比较）或 2 个（if_icmp、if_acmp 这类双值比较）。
     * 返回值按源码书写顺序排列：[0] 是左操作数。
     */
    public List<Expr> popConditionOperands(int opcode) {
        List<Expr> operands = new ArrayList<>();
        if (takesTwoOperands(opcode)) {
            Expr right = pop();
            Expr left = pop();
            operands.add(left);
            operands.add(right);
        } else {
            operands.add(pop());
        }
        return operands;
    }

    /**
     * switch 的选择值已压在栈顶，结构化阶段取走
     */
    public Expr popSelector() {
        return pop();
    }

    static boolean takesTwoOperands(int opcode) {
        String mnemonic = Opcodes.mnemonic(opcode);
        return mnemonic.startsWith("if_icmp") || mnemonic.startsWith("if_acmp");
    }

    /**
     * 把条件跳转的被比较值还原成源码表达式。taken 为 true 表示「跳转成立」，
     * false 表示 fall-through 成立（比较取反）。
     */
    public static Expr condition(int opcode, List<Expr> operands, boolean taken) {
        String mnemonic = Opcodes.mnemonic(opcode);
        if (takesTwoOperands(opcode)) {
            String operator = compareOperator(opcode);
            Expr left = operands.get(0);
            Expr right = operands.get(1);
            return new Expr.Binary(taken ? operator : negate(operator), left, right);
        }
        if (mnemonic.startsWith("ifnull") || mnemonic.startsWith("ifnonnull")) {
            boolean expectNull = mnemonic.startsWith("ifnull");
            boolean isNull = rightNull(operands.get(0));
            boolean holds = taken ? isNull == expectNull : isNull != expectNull;
            return Expr.Literal.of(holds);
        }
        Expr value = operands.get(0);
        String operator = compareOperator(opcode);
        return new Expr.Binary(taken ? operator : negate(operator), value, Expr.Literal.ofNumber(0));
    }

    /**
     * 编译期无法确定是否为 null 时不猜，把判断退化成 true/false 加注释
     */
    private static boolean rightNull(Expr expr) {
        if (expr instanceof Expr.Ref) {
            return "null".equals(expr.toJava());
        }
        return false;
    }

    static String compareOperator(int opcode) {
        switch (opcode & 0xFF) {
            case 0x99:
            case 0x9f:
                return "==";
            case 0x9a:
            case 0xa0:
                return "!=";
            case 0x9b:
            case 0xa1:
                return "<";
            case 0x9c:
            case 0xa2:
                return ">=";
            case 0x9d:
            case 0xa3:
                return ">";
            case 0x9e:
            case 0xa4:
                return "<=";
            case Opcodes.IFNULL:
                return "==";
            case Opcodes.IFNONNULL:
                return "!=";
            default:
                return "==";
        }
    }

    private static String negate(String operator) {
        if ("==".equals(operator)) {
            return "!=";
        }
        if ("!=".equals(operator)) {
            return "==";
        }
        if ("<".equals(operator)) {
            return ">=";
        }
        if (">=".equals(operator)) {
            return "<";
        }
        if (">".equals(operator)) {
            return "<=";
        }
        return ">";
    }
}
