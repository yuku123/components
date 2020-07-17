package com.zifang.util.core.lang.object.component;


import com.github.javaparser.ast.Modifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldInfo {

    private String type;
    private String value;
    private Modifier.Keyword[] modifiers = new Modifier.Keyword[]{Modifier.Keyword.PRIVATE};
    private String initializer = "null";

    public FieldInfo(String type,String value){
        this.type = type;
        this.value = value;
        this.modifiers = new Modifier.Keyword[]{Modifier.Keyword.PRIVATE};
        this.initializer = "null";
    }

    public void setModifier(Modifier.Keyword ... modifier) {
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
