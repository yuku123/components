package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertContext;
import com.zifang.util.core.lang.converter.ConvertException;
import com.zifang.util.core.lang.converter.ConvertPolicy;
import com.zifang.util.core.lang.converter.ConverterScope;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 时间族：String↔java.time 与 java.util.Date 的互转。
 * <p>
 * 候选格式先取 {@link ConvertPolicy#getTemporalFormats()}，未声明则走内置默认表，逐个尝试直到命中。
 *
 * @author zifang
 */
public final class TemporalFamily {

    private static final List<DateTimeFormatter> DEFAULT_DATE = list(
            DateTimeFormatter.ISO_LOCAL_DATE, of("yyyy/MM/dd"), of("yyyyMMdd"));
    private static final List<DateTimeFormatter> DEFAULT_DATE_TIME = list(
            of("yyyy-MM-dd HH:mm:ss"), DateTimeFormatter.ISO_LOCAL_DATE_TIME, of("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            of("yyyy/MM/dd HH:mm:ss"), of("yyyy-MM-dd HH:mm"), of("yyyy-MM-dd"));
    private static final List<DateTimeFormatter> DEFAULT_TIME = list(
            DateTimeFormatter.ISO_LOCAL_TIME, of("HH:mm:ss"), of("HH:mm"));
    private static final List<DateTimeFormatter> DEFAULT_OFFSET_DATE_TIME = list(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME, DateTimeFormatter.ISO_ZONED_DATE_TIME, of("yyyy-MM-dd HH:mm:ssXXX"));
    private static final List<DateTimeFormatter> DEFAULT_ZONED_DATE_TIME = list(
            DateTimeFormatter.ISO_ZONED_DATE_TIME, DateTimeFormatter.ISO_OFFSET_DATE_TIME, of("yyyy-MM-dd HH:mm:ssXXX"));
    private static final List<DateTimeFormatter> DEFAULT_INSTANT = list(
            DateTimeFormatter.ISO_INSTANT, of("yyyy-MM-dd HH:mm:ss"), of("yyyy-MM-dd HH:mm:ss.SSS"));
    private static final List<DateTimeFormatter> DEFAULT_DATE_STAMP = list(
            of("yyyy-MM-dd HH:mm:ss"), of("yyyy-MM-dd"), of("yyyy/MM/dd HH:mm:ss"));

    private TemporalFamily() {
    }

    public static void register(ConverterScope scope) {
        BuiltInConverters.register(scope, String.class, LocalDate.class,
                (value, context) -> parse(value, context, LocalDate.class, DEFAULT_DATE, LocalDate::parse));
        BuiltInConverters.register(scope, String.class, LocalDateTime.class,
                (value, context) -> parse(value, context, LocalDateTime.class, DEFAULT_DATE_TIME, LocalDateTime::parse));
        BuiltInConverters.register(scope, String.class, LocalTime.class,
                (value, context) -> parse(value, context, LocalTime.class, DEFAULT_TIME, LocalTime::parse));
        BuiltInConverters.register(scope, String.class, OffsetDateTime.class,
                (value, context) -> parse(value, context, OffsetDateTime.class, DEFAULT_OFFSET_DATE_TIME, OffsetDateTime::parse));
        BuiltInConverters.register(scope, String.class, ZonedDateTime.class,
                (value, context) -> parse(value, context, ZonedDateTime.class, DEFAULT_ZONED_DATE_TIME, ZonedDateTime::parse));
        BuiltInConverters.register(scope, String.class, Duration.class,
                (value, context) -> parseIso(value, context, Duration.class, Duration::parse));
        BuiltInConverters.register(scope, String.class, Period.class,
                (value, context) -> parseIso(value, context, Period.class, Period::parse));

        BuiltInConverters.register(scope, String.class, Instant.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            for (DateTimeFormatter formatter : candidates(context.getPolicy(), Instant.class, DEFAULT_INSTANT)) {
                try {
                    return Instant.from(formatter.parse(text));
                } catch (RuntimeException ignore) {
                    // 换下一个候选格式
                }
            }
            try {
                return Instant.ofEpochMilli(Long.parseLong(text));
            } catch (NumberFormatException e) {
                throw new ConvertException(value, String.class, Instant.class,
                        "既不是候选格式，也不是 epoch 毫秒");
            }
        });

        registerDateBridge(scope);
    }

    /**
     * java.util.Date 与 java.time 的桥：Date 按系统默认时区落到 java.time。
     */
    private static void registerDateBridge(ConverterScope scope) {
        BuiltInConverters.register(scope, Date.class, Instant.class,
                (value, context) -> Instant.ofEpochMilli(value.getTime()));
        BuiltInConverters.register(scope, Date.class, LocalDate.class,
                (value, context) -> zoned(value, context).toLocalDate());
        BuiltInConverters.register(scope, Date.class, LocalDateTime.class,
                (value, context) -> zoned(value, context).toLocalDateTime());

        BuiltInConverters.register(scope, Instant.class, Date.class, (value, context) -> Date.from(value));
        BuiltInConverters.register(scope, LocalDate.class, Date.class, (value, context) ->
                Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        BuiltInConverters.register(scope, LocalDateTime.class, Date.class, (value, context) ->
                Date.from(value.atZone(ZoneId.systemDefault()).toInstant()));

        BuiltInConverters.register(scope, Date.class, String.class,
                (value, context) -> format(zoned(value, context), context, Date.class, DEFAULT_DATE_STAMP));
        BuiltInConverters.register(scope, LocalDate.class, String.class,
                (value, context) -> format(value, context, LocalDate.class, DEFAULT_DATE));
        BuiltInConverters.register(scope, LocalDateTime.class, String.class,
                (value, context) -> format(value, context, LocalDateTime.class, DEFAULT_DATE_TIME));
        BuiltInConverters.register(scope, LocalTime.class, String.class,
                (value, context) -> format(value, context, LocalTime.class, DEFAULT_TIME));
        BuiltInConverters.register(scope, Instant.class, String.class, (value, context) -> value.toString());
    }

    private static ZonedDateTime zoned(Date value, ConvertContext context) {
        return value.toInstant().atZone(ZoneId.systemDefault());
    }

    private static String format(TemporalAccessor value, ConvertContext context, Class<?> type,
                                 List<DateTimeFormatter> fallback) {
        return candidates(context.getPolicy(), type, fallback).get(0).format(value);
    }

    private interface Parser<T> {
        T parse(String text, DateTimeFormatter formatter);
    }

    private interface PlainParser<T> {
        T parse(String text);
    }

    private static <T> T parse(String raw, ConvertContext context, Class<?> type,
                               List<DateTimeFormatter> fallback, Parser<T> parser) {
        String text = Inputs.trim(raw, context.getPolicy());
        List<DateTimeFormatter> formatters = candidates(context.getPolicy(), type, fallback);
        for (DateTimeFormatter formatter : formatters) {
            try {
                return parser.parse(text, formatter);
            } catch (RuntimeException ignore) {
                // 换下一个候选格式
            }
        }
        throw new ConvertException(raw, String.class, type, "无法按候选格式解析: " + describe(formatters));
    }

    private static <T> T parseIso(String raw, ConvertContext context, Class<?> type, PlainParser<T> parser) {
        String text = Inputs.trim(raw, context.getPolicy());
        try {
            return parser.parse(text);
        } catch (RuntimeException e) {
            throw new ConvertException(raw, String.class, type, "无法按 ISO-8601 解析: " + e.getMessage());
        }
    }

    private static List<DateTimeFormatter> candidates(ConvertPolicy policy, Class<?> type,
                                                       List<DateTimeFormatter> fallback) {
        List<DateTimeFormatter> configured = policy.getTemporalFormats().get(type);
        return configured == null || configured.isEmpty() ? fallback : configured;
    }

    private static String describe(List<DateTimeFormatter> formatters) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < formatters.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(formatters.get(i));
        }
        return sb.append(']').toString();
    }

    private static List<DateTimeFormatter> list(DateTimeFormatter... formatters) {
        return Collections.unmodifiableList(Arrays.asList(formatters));
    }

    private static DateTimeFormatter of(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }
}