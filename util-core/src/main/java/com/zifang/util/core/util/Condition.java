package com.zifang.util.core.util;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 各种验证条件的集中管理
 * */
public class Condition {
    public static Predicate<Object> IS_NULL = Objects::isNull;
    public static Predicate<Object> IS_ARRAY = (e) -> true;
}
