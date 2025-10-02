package com.zifang.util.core.meta;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.io.IOException;

@JacksonStdImpl
public class KeepLongSerializer extends NumberSerializers.Base<Object> {

    private static final long serialVersionUID = -1194198701939237302L;

    public KeepLongSerializer() {
        super(Long.class, NumberType.LONG, "number");
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber((Long) value);
    }

}
