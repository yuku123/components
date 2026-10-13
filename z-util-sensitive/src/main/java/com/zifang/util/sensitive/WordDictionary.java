package com.zifang.util.sensitive;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 敏感词库。词库是业务资产：按监管口径增删词条，不该跟在上游通用词库后面。
 */
public class WordDictionary {

    private final Set<WordEntry> entries = new LinkedHashSet<>();

    public static WordDictionary create() {
        return new WordDictionary();
    }

    public WordDictionary add(String word) {
        return add(WordEntry.of(word));
    }

    public WordDictionary add(String word, SensitiveType type) {
        return add(WordEntry.of(word, type));
    }

    public WordDictionary add(String word, SensitiveType type, String tag) {
        return add(WordEntry.of(word, type, tag));
    }

    public WordDictionary add(WordEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
        return this;
    }

    public WordDictionary addAll(WordDictionary other) {
        if (other != null) {
            entries.addAll(other.entries);
        }
        return this;
    }

    public int size() {
        return entries.size();
    }

    public Set<WordEntry> entries() {
        return Collections.unmodifiableSet(entries);
    }

    /** 冻结成不可变匹配器 */
    public DfaMatcher build() {
        return DfaMatcher.compile(entries);
    }
}
