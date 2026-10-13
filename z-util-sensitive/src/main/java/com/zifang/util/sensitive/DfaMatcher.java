package com.zifang.util.sensitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 敏感词 DFA 匹配器。
 * <p>
 * N 个词条建一次自动机，之后扫全文是 O(len)，与词库规模无关。
 * 子节点用 {@code Map<Character, Node>} 而非数组：模块要同时匹配中文与 ASCII，
 * 开 Unicode 全空间的数组纯属浪费。
 * <p>
 * 构建后完全不可变，可多线程共享。词库热更新 = 用新词库建一个新实例 + 换引用。
 */
public class DfaMatcher {

    private final Node root;
    private final boolean hasWords;

    private DfaMatcher(Node root, boolean hasWords) {
        this.root = root;
        this.hasWords = hasWords;
    }

    public boolean hasWords() {
        return hasWords;
    }

    public static DfaMatcher compile(Set<WordEntry> entries) {
        Node root = new Node();
        if (entries != null) {
            for (WordEntry entry : entries) {
                Node current = root;
                for (int i = 0; i < entry.word().length(); i++) {
                    current = current.child(entry.word().charAt(i), true);
                }
                // 同一词条挂多个类型/标签时全部保留，不能互相覆盖
                current.entries.add(entry);
                current.terminal = true;
            }
        }
        return new DfaMatcher(root, entries != null && !entries.isEmpty());
    }

    public static DfaMatcher empty() {
        return new DfaMatcher(new Node(), false);
    }

    /**
     * 扫全文找命中。同一起始位置取<b>最长</b>匹配并整段跳过，
     * 否则 "涉政涉黄" 这类互相包含的词会层层重叠地重复报。
     */
    public List<SensitiveMatch> match(String text) {
        List<SensitiveMatch> matches = new ArrayList<>();
        if (!hasWords || text == null || text.isEmpty()) {
            return matches;
        }
        int i = 0;
        while (i < text.length()) {
            Node node = root;
            int j = i;
            Node best = null;
            while (j < text.length()) {
                node = node.children.get(text.charAt(j));
                if (node == null) {
                    break;
                }
                j++;
                if (node.terminal) {
                    best = node;
                }
            }
            if (best != null) {
                // 一个词挂多个词条时逐条报出：词条自带的类型与标签才是调用方要的信息，
                // 统一按 WORD 报出去等于把 add(word, type) 这个入口作废
                for (WordEntry entry : best.entries) {
                    matches.add(new SensitiveMatch(entry.type(), text.substring(i, j), i, j, entry.tag()));
                }
                i = j;
            } else {
                i++;
            }
        }
        return matches;
    }

    public boolean matches(String text) {
        if (!hasWords || text == null || text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            Node node = root;
            int j = i;
            while (j < text.length()) {
                node = node.children.get(text.charAt(j));
                if (node == null) {
                    break;
                }
                j++;
                if (node.terminal) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final class Node {

        private final Map<Character, Node> children = new HashMap<>();
        private final List<WordEntry> entries = new ArrayList<>(1);
        private boolean terminal;

        private Node child(char c, boolean create) {
            Node node = children.get(c);
            if (node == null && create) {
                node = new Node();
                children.put(c, node);
            }
            return node;
        }
    }
}
