package com.zifang.util.sensitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * 识别结果：按起点排序、互不重叠的一组命中。
 */
public class SensitiveResult {

    private static final SensitiveResult EMPTY = new SensitiveResult(Collections.<SensitiveMatch>emptyList());

    private final List<SensitiveMatch> matches;

    SensitiveResult(List<SensitiveMatch> matches) {
        this.matches = Collections.unmodifiableList(matches);
    }

    public static SensitiveResult empty() {
        return EMPTY;
    }

    public List<SensitiveMatch> matches() {
        return matches;
    }

    public boolean isEmpty() {
        return matches.isEmpty();
    }

    public int size() {
        return matches.size();
    }

    public boolean hasType(SensitiveType type) {
        for (SensitiveMatch m : matches) {
            if (m.type() == type) {
                return true;
            }
        }
        return false;
    }

    public List<SensitiveMatch> matchesOf(SensitiveType type) {
        List<SensitiveMatch> out = new ArrayList<>();
        for (SensitiveMatch m : matches) {
            if (m.type() == type) {
                out.add(m);
            }
        }
        return out;
    }

    public Set<SensitiveType> types() {
        Set<SensitiveType> types = EnumSet.noneOf(SensitiveType.class);
        for (SensitiveMatch m : matches) {
            types.add(m.type());
        }
        return types;
    }

    /** 用掩码替换全部命中，其余原文不动 */
    public String mask(String text, MaskStyle style) {
        if (text == null || matches.isEmpty()) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text.length());
        int cursor = 0;
        for (SensitiveMatch m : matches) {
            if (m.start() > cursor) {
                sb.append(text, cursor, m.start());
            }
            sb.append(MaskUtil.mask(m.value(), m.type(), style));
            cursor = m.end();
        }
        if (cursor < text.length()) {
            sb.append(text, cursor, text.length());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "SensitiveResult" + matches;
    }
}
