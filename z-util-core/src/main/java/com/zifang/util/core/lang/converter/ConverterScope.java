package com.zifang.util.core.lang.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 转换器分组。
 * <p>
 * scope 是一棵有父子的树：子 scope 继承父 scope 的全部转换器，可整组启停，
 * 并在类型对相同的情况下以更高优先级参与候选链。
 *
 * @author zifang
 */
public class ConverterScope {

    private final String name;
    private final ConverterScope parent;
    private final List<ConverterScope> children = new ArrayList<>();

    private int priority;
    private volatile boolean enabled = true;

    public ConverterScope(String name) {
        this(name, null, 0);
    }

    public ConverterScope(String name, ConverterScope parent, int priority) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("scope name 不能为空");
        }
        if (parent != null && parent.contains(name)) {
            throw new IllegalArgumentException("scope 名重复: " + name + " 已存在于 " + parent.name + " 的子树");
        }
        this.name = name;
        this.parent = parent;
        this.priority = priority;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public String getName() {
        return name;
    }

    public ConverterScope getParent() {
        return parent;
    }

    public List<ConverterScope> getChildren() {
        return Collections.unmodifiableList(new ArrayList<>(children));
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 本 scope 及其所有祖先是否都处于启用态
     */
    public boolean isActive() {
        for (ConverterScope scope = this; scope != null; scope = scope.parent) {
            if (!scope.enabled) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从本 scope 向上到根的链路（含自身）
     */
    public List<ConverterScope> lineage() {
        List<ConverterScope> chain = new ArrayList<>();
        for (ConverterScope scope = this; scope != null; scope = scope.parent) {
            chain.add(0, scope);
        }
        return chain;
    }

    public ConverterScope find(String scopeName) {
        if (name.equals(scopeName)) {
            return this;
        }
        for (ConverterScope child : children) {
            ConverterScope found = child.find(scopeName);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private boolean contains(String scopeName) {
        return find(scopeName) != null;
    }

    @Override
    public String toString() {
        return (parent == null ? "" : parent.name + "/") + name + "(priority=" + priority + ", enabled=" + enabled + ")";
    }
}