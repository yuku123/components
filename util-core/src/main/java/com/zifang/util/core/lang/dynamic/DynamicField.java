package com.zifang.util.core.lang.dynamic;

/**
 * @author zifang
 *
 * /**
 *  * comments
 *  \*\/
 * // desc
 * private String name = "";
 *
 */
public class DynamicField {

    private String name;

    private String type;

    private Object value;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static DynamicField of(String name, String type){
        return DynamicField.of(name, type, null, null);
    }
    public static DynamicField of(String name, String type, Object value, String desc){
        DynamicField dynamicField = new DynamicField();
        dynamicField.setName(name);
        dynamicField.setType(type);
        dynamicField.setValue(value);
        dynamicField.setDesc(desc);
        return dynamicField;
    }
}