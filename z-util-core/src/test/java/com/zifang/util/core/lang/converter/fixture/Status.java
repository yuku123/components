package com.zifang.util.core.lang.converter.fixture;

import com.zifang.util.core.lang.converter.EnumValue;

/**
 * 带 {@link EnumValue} 业务值字段的枚举，字面量按 code 匹配而非常量名。
 */
public enum Status {

    ENABLED("1", "启用"),
    DISABLED("0", "停用");

    @EnumValue
    private final String code;

    private final String label;

    Status(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}