package com.zifang.util.bc.bytecode.decompile.flow;

import com.zifang.util.bc.bytecode.decompile.core.ParamsConvertor;
import com.zifang.util.bc.bytecode.model.ConstantPoolHelper;
import com.zifang.util.bc.bytecode.model.attribute.AbstractAttribute;
import com.zifang.util.bc.bytecode.model.attribute.Code;
import com.zifang.util.bc.bytecode.model.attribute.LocalVariableTable;
import com.zifang.util.bc.bytecode.model.instruction.Instruction;
import com.zifang.util.bc.bytecode.model.instruction.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 局部变量名与类型的按 pc 作用域索引
 * <p>
 * 同一个槽位在不同 pc 上可能是不同变量（for 循环里就是），所以名字必须按
 * {@code [startPc, startPc+length)} 区间查，不能只按槽位存一份。
 * 查不到作用域时退化成 {@code varN}，比猜错强。
 */
public class LocalVarNames {

    private static final class Entry {
        private final int slot;
        private final int start;
        private final int length;
        private final String name;
        private final String type;

        Entry(int slot, int start, int length, String name, String type) {
            this.slot = slot;
            this.start = start;
            this.length = length;
            this.name = name;
            this.type = type;
        }

        boolean covers(int pc) {
            return pc >= start && pc < start + length;
        }
    }

    private final List<Entry> entries = new ArrayList<>();
    private final boolean staticMethod;
    private final Map<Integer, Integer> firstStoreOfSlot = new HashMap<>();

    private LocalVarNames(boolean staticMethod) {
        this.staticMethod = staticMethod;
    }

    /**
     * 建索引：作用域来自 LocalVariableTable，首次赋值位置来自实际 store 指令
     *
     * @param code         方法体 Code 属性
     * @param instructions 该方法体的指令序列
     * @param thisClassName 当前类名；静态方法传 null
     */
    public static LocalVarNames of(Code code, List<Instruction> instructions, String thisClassName) {
        LocalVarNames result = new LocalVarNames(thisClassName == null);
        ConstantPoolHelper pool = new ConstantPoolHelper(code.getPoolList());
        for (AbstractAttribute attribute : code.getAttributes()) {
            if (!(attribute instanceof LocalVariableTable)) {
                continue;
            }
            for (LocalVariableTable.LocalVariableInfo info
                    : ((LocalVariableTable) attribute).getLocalVariableTable()) {
                String name = pool.utf8(info.getNameIndex().value);
                String descriptor = pool.utf8(info.getDescriptorIndex().value);
                if (name == null || name.isEmpty()) {
                    continue;
                }
                String type = descriptor == null ? "Object"
                        : ParamsConvertor.paramsConvertorFieldType(descriptor);
                result.entries.add(new Entry(info.getIndex().value, info.getStartPc().value,
                        info.getLength().value, name, type));
            }
        }
        result.collectFirstStores(instructions);
        return result;
    }

    /**
     * 记下每个槽位在方法体里的第一次 store：只有这一次渲染成带类型的声明。
     * <p>
     * 按槽位取最早的 store 而不是按作用域判定，是因为 javac 会给循环里的变量
     * 再开一条局部变量表记录，作用域判定会把循环体里的赋值误当成首次声明。
     */
    private void collectFirstStores(List<Instruction> instructions) {
        for (Instruction insn : instructions) {
            int slot = storeSlot(insn);
            if (slot < 0) {
                continue;
            }
            Integer prev = firstStoreOfSlot.get(slot);
            if (prev == null || insn.getPc() < prev) {
                firstStoreOfSlot.put(slot, insn.getPc());
            }
        }
    }

    private static int storeSlot(Instruction insn) {
        int op = insn.getOpcode();
        if (op == Opcodes.ISTORE || op == Opcodes.LSTORE || op == Opcodes.FSTORE
                || op == Opcodes.DSTORE || op == Opcodes.ASTORE) {
            return insn.u1Operand();
        }
        if (op >= Opcodes.ISTORE_0 && op <= Opcodes.ASTORE_3) {
            return op - Opcodes.ISTORE_0;
        }
        return -1;
    }

    /**
     * 槽 0 在实例方法里是 this，不是一个具名局部变量
     */
    public boolean isThis(int slot) {
        return !staticMethod && slot == 0;
    }

    /**
     * 变量名；pc 传 -1 表示不关心作用域（栈上还没有位置信息的值）
     */
    public String name(int slot, int pc) {
        Entry entry = lookup(slot, pc);
        if (entry == null && declaredAt(slot, pc)) {
            entry = earliestEntry(slot);
        }
        return entry != null ? entry.name : "var" + slot;
    }

    public String type(int slot, int pc) {
        Entry entry = lookup(slot, pc);
        if (entry == null && declaredAt(slot, pc)) {
            entry = earliestEntry(slot);
        }
        return entry != null ? entry.type : "Object";
    }

    /**
     * 首次声明的位置往往还没落进任何作用域区间（变量从循环体才开始记作用域），
     * 这时退回到该槽位最早的那条记录，名字与类型都取自它
     */
    private Entry earliestEntry(int slot) {
        Entry best = null;
        for (Entry entry : entries) {
            if (entry.slot == slot && (best == null || entry.start < best.start)) {
                best = entry;
            }
        }
        return best;
    }

    /**
     * 该 pc 上的 store 是否是该槽位在整个方法体里的首次赋值
     */
    public boolean declaredAt(int slot, int pc) {
        if (pc < 0) {
            return false;
        }
        Integer first = firstStoreOfSlot.get(slot);
        return first != null && first == pc;
    }

    private Entry lookup(int slot, int pc) {
        Entry best = null;
        if (pc < 0) {
            for (Entry entry : entries) {
                if (entry.slot == slot && (best == null || entry.start > best.start)) {
                    best = entry;
                }
            }
            return best;
        }
        for (Entry entry : entries) {
            if (entry.slot == slot && entry.covers(pc)
                    && (best == null || entry.start > best.start)) {
                best = entry;
            }
        }
        return best;
    }

    public int size() {
        return entries.size();
    }
}
