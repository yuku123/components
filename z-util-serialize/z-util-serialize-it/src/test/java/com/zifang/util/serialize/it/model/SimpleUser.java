package com.zifang.util.serialize.it.model;

import com.zifang.util.serialize.annotation.FieldType;
import com.zifang.util.serialize.annotation.ZField;
import com.zifang.util.serialize.annotation.ZMessage;

/**
 * Simple test message for codegen verification.
 */
@ZMessage(id = 500, name = "it.SimpleUser")
public class SimpleUser {

    @ZField(id = 1, type = FieldType.VARINT)
    public long id;

    @ZField(id = 2, type = FieldType.LENGTH_DELIMITED)
    public String name;

    @ZField(id = 3, type = FieldType.VARINT)
    public int age;

    @ZField(id = 4, type = FieldType.VARINT)
    public boolean active;

    public SimpleUser() {}
}
