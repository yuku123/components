package com.zifang.util.sensitive;

/**
 * 敏感词词条。不可变。
 */
public class WordEntry {

    private final String word;
    private final SensitiveType type;
    private final String tag;

    public WordEntry(String word, SensitiveType type, String tag) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("word must not be blank");
        }
        this.word = word.trim();
        this.type = type == null ? SensitiveType.WORD : type;
        this.tag = tag;
    }

    public static WordEntry of(String word) {
        return new WordEntry(word, SensitiveType.WORD, null);
    }

    public static WordEntry of(String word, SensitiveType type) {
        return new WordEntry(word, type, null);
    }

    public static WordEntry of(String word, SensitiveType type, String tag) {
        return new WordEntry(word, type, tag);
    }

    public String word() {
        return word;
    }

    public SensitiveType type() {
        return type;
    }

    public String tag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WordEntry)) {
            return false;
        }
        WordEntry other = (WordEntry) o;
        return word.equals(other.word) && type == other.type
                && (tag == null ? other.tag == null : tag.equals(other.tag));
    }

    @Override
    public int hashCode() {
        return 31 * (31 * word.hashCode() + type.hashCode()) + (tag == null ? 0 : tag.hashCode());
    }

    @Override
    public String toString() {
        return "WordEntry{" + word + "," + type + (tag != null ? "," + tag : "") + '}';
    }
}
