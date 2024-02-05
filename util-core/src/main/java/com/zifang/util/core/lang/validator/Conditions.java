package com.zifang.util.core.lang.validator;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 各种验证条件的集中管理
 */
public class Conditions {
    public static Predicate<Object> IS_NOT_NULL = Objects::nonNull;
    public static Predicate<Object> IS_NULL = Objects::isNull;
    public static Predicate<Object> IS_ARRAY = (e) -> true;
}
