package com.zifang.util.core.meta.page;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zifang
 */
public class SortField implements Serializable {

    private static final long serialVersionUID = -7342671270425244017L;

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    public SortField(String column, boolean asc) {
        this.column = column;
        this.asc = asc;
    }

    public SortField() {
    }

    public static SortField asc(String column) {
        return build(column, true);
    }

    public static SortField desc(String column) {
        return build(column, false);
    }

    public static List<SortField> ascs(String... columns) {
        return Arrays.stream(columns).map(SortField::asc).collect(Collectors.toList());
    }

    public static List<SortField> descs(String... columns) {
        return Arrays.stream(columns).map(SortField::desc).collect(Collectors.toList());
    }

    private static SortField build(String column, boolean asc) {
        return new SortField(column, asc);
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}