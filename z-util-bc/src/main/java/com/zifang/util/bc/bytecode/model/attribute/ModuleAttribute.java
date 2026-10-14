package com.zifang.util.bc.bytecode.model.attribute;

import com.zifang.util.bc.bytecode.model.readtype.ByteReader;
import com.zifang.util.bc.bytecode.model.readtype.U2;
import com.zifang.util.bc.bytecode.model.readtype.U4;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Module 属性（4.7.23，Java 9+）：模块声明本身。
 * <p>
 * 结构：module_name_index u2、module_flags u2、module_version_index u2，
 * 之后是五张表（requires / exports / opens / uses / provides），每张表都是「u2 计数 + 定长条目」。
 */
public class ModuleAttribute extends AbstractAttribute {

    /** requires 子表条目 */
    public static class Requires {
        private final U2 requiresIndex;
        private final U2 requiresFlags;
        private final U2 requiresVersionIndex;

        public Requires(U2 requiresIndex, U2 requiresFlags, U2 requiresVersionIndex) {
            this.requiresIndex = requiresIndex;
            this.requiresFlags = requiresFlags;
            this.requiresVersionIndex = requiresVersionIndex;
        }

        public U2 getRequiresIndex() {
            return requiresIndex;
        }

        public U2 getRequiresFlags() {
            return requiresFlags;
        }

        public U2 getRequiresVersionIndex() {
            return requiresVersionIndex;
        }

        void writeBody(DataOutput out) throws IOException {
            requiresIndex.write(out);
            requiresFlags.write(out);
            requiresVersionIndex.write(out);
        }
    }

    /** exports / opens 子表条目（两个表布局相同，只是语义不同） */
    public static class ExportsOpens {
        private final U2 index;
        private final U2 flags;
        private final List<U2> toIndex;

        public ExportsOpens(U2 index, U2 flags, List<U2> toIndex) {
            this.index = index;
            this.flags = flags;
            this.toIndex = toIndex;
        }

        public U2 getIndex() {
            return index;
        }

        public U2 getFlags() {
            return flags;
        }

        public List<U2> getToIndex() {
            return Collections.unmodifiableList(toIndex);
        }

        void writeBody(DataOutput out) throws IOException {
            index.write(out);
            flags.write(out);
            out.writeShort(toIndex.size());
            for (U2 t : toIndex) {
                t.write(out);
            }
        }

        int bodySize() {
            return 6 + toIndex.size() * 2;
        }
    }

    /** provides 子表条目 */
    public static class Provides {
        private final U2 providesIndex;
        private final List<U2> withIndex;

        public Provides(U2 providesIndex, List<U2> withIndex) {
            this.providesIndex = providesIndex;
            this.withIndex = withIndex;
        }

        public U2 getProvidesIndex() {
            return providesIndex;
        }

        public List<U2> getWithIndex() {
            return Collections.unmodifiableList(withIndex);
        }

        void writeBody(DataOutput out) throws IOException {
            providesIndex.write(out);
            out.writeShort(withIndex.size());
            for (U2 w : withIndex) {
                w.write(out);
            }
        }

        int bodySize() {
            return 4 + withIndex.size() * 2;
        }
    }

    private U2 moduleNameIndex;
    private U2 moduleFlags;
    private U2 moduleVersionIndex;
    private final List<Requires> requires = new ArrayList<>();
    private final List<ExportsOpens> exports = new ArrayList<>();
    private final List<ExportsOpens> opens = new ArrayList<>();
    private final List<U2> usesIndex = new ArrayList<>();
    private final List<Provides> provides = new ArrayList<>();

    public ModuleAttribute(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {
        ByteReader reader = new ByteReader(inputStream);
        moduleNameIndex = reader.readU2();
        moduleFlags = reader.readU2();
        moduleVersionIndex = reader.readU2();

        int n = reader.readU2().intValue();
        for (int i = 0; i < n; i++) {
            requires.add(new Requires(reader.readU2(), reader.readU2(), reader.readU2()));
        }
        readExportsOpens(reader, exports);
        readExportsOpens(reader, opens);

        n = reader.readU2().intValue();
        for (int i = 0; i < n; i++) {
            usesIndex.add(reader.readU2());
        }

        n = reader.readU2().intValue();
        for (int i = 0; i < n; i++) {
            U2 providesIndex = reader.readU2();
            int withCount = reader.readU2().intValue();
            List<U2> withIndex = new ArrayList<>(withCount);
            for (int j = 0; j < withCount; j++) {
                withIndex.add(reader.readU2());
            }
            provides.add(new Provides(providesIndex, withIndex));
        }
    }

    private void readExportsOpens(ByteReader reader, List<ExportsOpens> target) {
        int n = reader.readU2().intValue();
        for (int i = 0; i < n; i++) {
            U2 index = reader.readU2();
            U2 flags = reader.readU2();
            int toCount = reader.readU2().intValue();
            List<U2> toIndex = new ArrayList<>(toCount);
            for (int j = 0; j < toCount; j++) {
                toIndex.add(reader.readU2());
            }
            target.add(new ExportsOpens(index, flags, toIndex));
        }
    }

    @Override
    protected void writeBody(DataOutput out) throws IOException {
        moduleNameIndex.write(out);
        moduleFlags.write(out);
        moduleVersionIndex.write(out);

        out.writeShort(requires.size());
        for (Requires r : requires) {
            r.writeBody(out);
        }
        writeExportsOpens(out, exports);
        writeExportsOpens(out, opens);

        out.writeShort(usesIndex.size());
        for (U2 u : usesIndex) {
            u.write(out);
        }

        out.writeShort(provides.size());
        for (Provides p : provides) {
            p.writeBody(out);
        }
    }

    private void writeExportsOpens(DataOutput out, List<ExportsOpens> items) throws IOException {
        out.writeShort(items.size());
        for (ExportsOpens e : items) {
            e.writeBody(out);
        }
    }

    @Override
    public int contentSize() {
        int n = 6;
        n += 2 + requires.size() * 6;
        n += 2 + exportsSize(exports);
        n += 2 + exportsSize(opens);
        n += 2 + usesIndex.size() * 2;
        n += 2;
        for (Provides p : provides) {
            n += p.bodySize();
        }
        return n;
    }

    private int exportsSize(List<ExportsOpens> items) {
        int n = 0;
        for (ExportsOpens e : items) {
            n += e.bodySize();
        }
        return n;
    }

    public U2 getModuleNameIndex() {
        return moduleNameIndex;
    }

    public U2 getModuleFlags() {
        return moduleFlags;
    }

    public List<Requires> getRequires() {
        return Collections.unmodifiableList(requires);
    }

    public List<ExportsOpens> getExports() {
        return Collections.unmodifiableList(exports);
    }

    public List<ExportsOpens> getOpens() {
        return Collections.unmodifiableList(opens);
    }

    public List<U2> getUsesIndex() {
        return Collections.unmodifiableList(usesIndex);
    }

    public List<Provides> getProvides() {
        return Collections.unmodifiableList(provides);
    }
}
