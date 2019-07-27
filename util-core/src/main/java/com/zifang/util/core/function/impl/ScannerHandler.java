package com.zifang.util.core.function.impl;

import java.util.function.Function;

public class ScannerHandler implements Function<String,String> {
    @Override
    public String apply(String s) {
        return "sb.append(\""+s+");\\n";
    }
}