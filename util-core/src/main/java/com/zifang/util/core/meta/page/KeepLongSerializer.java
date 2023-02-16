package com.zifang.util.core.meta.page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.io.IOException;

/**
 * @author: Ares
 * @time: 2021-06-15 16:09:00
 * @description: keep long still origin serializer not string serializer
 * @version: JDK 1.8
 * @see com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer
 */
@JacksonStdImpl
public class KeepLongSerializer extends NumberSerializers.Base<Object> {

  private static final long serialVersionUID = -1194198701939237302L;

  public KeepLongSerializer() {
    super(Long.class, NumberType.LONG, "number");
  }

  @Override
  public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeNumber((Long) value);
  }

}
