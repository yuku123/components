package com.zifang.util.core.lang.converter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultConverter2 {

    class IntegerLongConverter implements IConverter<Integer,Long>{
        @Override
        public Long to(Integer value, Long defaultValue) {
            log.info("call Long to(Integer value, Long defaultValue");
            if (value == null) {
                return defaultValue;
            }
            return null;
        }
    }
}
