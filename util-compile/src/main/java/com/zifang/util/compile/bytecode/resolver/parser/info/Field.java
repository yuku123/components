package com.zifang.util.compile.bytecode.a.parser.info;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Field {

    int accessFlag;

    int nameIndex;

    int descriptorIndex;

    int attributesCount;



    private String getString(int nameIndex) {
        return null;
    }

    public Field(int accessFlag, int nameIndex, int descriptorIndex, int attributesCount) {
        super();
        this.accessFlag = accessFlag;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
    }

}
