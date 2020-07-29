package com.zifang.util.core.lang.object.component;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldInfo {

    private String type;
    private String value;
    private int [] modifiers = new int[]{};
    private String initializer = "null";

    public FieldInfo(String type,String value){
        this.type = type;
        this.value = value;
        this.modifiers = new int[]{};
        this.initializer = "null";
    }

    public void setModifier(int ... modifier) {
        modifiers = modifier;
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(((FieldInfo)obj).value);
    }

    @Override
    public String toString(){
        return type+" "+value+" = "+initializer+";";
    }
}
