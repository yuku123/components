package com.zifang.util.sensitive;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 掩码实现与结果对象的边界形态。
 */
class MaskAndResultTest {

    // ---------------- 掩码 ----------------

    @Test
    void 全遮保持长度不变() {
        assertEquals("***********", MaskUtil.mask("13800138000", SensitiveType.MOBILE, MaskStyle.FULL));
        assertEquals("**", MaskUtil.mask("赌博", SensitiveType.WORD, MaskStyle.FULL));
        assertEquals("", MaskUtil.mask("", SensitiveType.WORD, MaskStyle.FULL));
        assertNull(MaskUtil.mask(null, SensitiveType.WORD, MaskStyle.FULL));
    }

    @Test
    void 保留头尾按类型取默认位数() {
        assertEquals("138****8000", MaskUtil.mask("13800138000", SensitiveType.MOBILE, MaskStyle.HEAD_TAIL));
        assertEquals("110105********002X", MaskUtil.mask("11010519491231002X", SensitiveType.ID_CARD, MaskStyle.HEAD_TAIL));
        assertEquals("6212********7890", MaskUtil.mask("6212123456787890", SensitiveType.BANK_CARD, MaskStyle.HEAD_TAIL));
    }

    @Test
    void 保留位数不小于长度时直接全遮() {
        // 短值不能露出全部：头尾加起来已经覆盖整串时退化成全遮
        assertEquals("***", MaskUtil.mask("abc", SensitiveType.ID_CARD, MaskStyle.HEAD_TAIL));
        assertEquals("****", MaskUtil.mask("abcd", SensitiveType.WORD, MaskStyle.HEAD_TAIL));
        assertEquals("*", MaskUtil.mask("赌", SensitiveType.WORD, MaskStyle.HEAD_TAIL));
    }

    @Test
    void 类型为null时按默认两位头两位尾() {
        assertEquals("ab**ef", MaskUtil.mask("abcdef", null, MaskStyle.HEAD_TAIL));
    }

    @Test
    void 邮箱保域名其余打码() {
        assertEquals("z****@example.com", MaskUtil.mask("zhang@example.com", SensitiveType.EMAIL, MaskStyle.HEAD_TAIL));
        // 单字符本地部分也要留一位
        assertEquals("z*@a.cn", MaskUtil.mask("z@a.cn", SensitiveType.EMAIL, MaskStyle.HEAD_TAIL));
        // 没有 @ 的畸形值退化成全遮
        assertEquals("*****", MaskUtil.mask("abcde", SensitiveType.EMAIL, MaskStyle.HEAD_TAIL));
    }

    @Test
    void 哈希脱敏同值稳定且长度固定() {
        String a = MaskUtil.mask("13800138000", SensitiveType.MOBILE, MaskStyle.HASH);
        String b = MaskUtil.mask("13800138000", SensitiveType.MOBILE, MaskStyle.HASH);
        assertEquals(a, b);
        assertEquals(8, a.length());
        assertFalse(a.equals(MaskUtil.mask("13800138001", SensitiveType.MOBILE, MaskStyle.HASH)));
        // 同一手机号在不同类型下也是同一个哈希值，可用来做关联
        assertEquals(a, MaskUtil.mask("13800138000", SensitiveType.WORD, MaskStyle.HASH));
    }

    @Test
    void ip保留头两位而url全遮() {
        assertEquals("19**********", MaskUtil.mask("192.168.1.10", SensitiveType.IP, MaskStyle.HEAD_TAIL));
        // URL 的默认保留位数是 0/0，前后都不留
        assertEquals("********************",
                MaskUtil.mask("http://a.example.com", SensitiveType.URL, MaskStyle.HEAD_TAIL));
    }

    @Test
    void 脱敏后仍保持原文总长度() {
        String text = "手机 13800138000 邮箱 zhang@example.com";
        SensitiveResult r = SensitiveDetector.create().detect(text);
        // HASH 是定长 8 位摘要，天然不等长，只有逐位打码的两种风格保持长度
        for (MaskStyle style : new MaskStyle[]{MaskStyle.FULL, MaskStyle.HEAD_TAIL}) {
            assertEquals(text.length(), r.mask(text, style).length(), style.name());
        }
        assertFalse(r.mask(text, MaskStyle.HASH).contains("13800138000"));
    }

    // ---------------- 结果对象 ----------------

    @Test
    void 空结果的一切读法都安全() {
        SensitiveResult empty = SensitiveResult.empty();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertTrue(empty.matches().isEmpty());
        assertFalse(empty.hasType(SensitiveType.WORD));
        assertTrue(empty.matchesOf(SensitiveType.WORD).isEmpty());
        assertTrue(empty.types().isEmpty());
        assertNull(empty.mask(null, MaskStyle.FULL));
        assertEquals("abc", empty.mask("abc", MaskStyle.FULL));
    }

    @Test
    void 结果列表不可改() {
        SensitiveResult r = SensitiveDetector.create().detect("手机 13800138000");
        assertThrows(UnsupportedOperationException.class, () -> r.matches().clear());
    }

    @Test
    void 按类型筛选与汇总类型() {
        SensitiveResult r = SensitiveDetector.create()
                .detect("联系 zhang.san@example.com，服务器 192.168.1.10，手机 13800138000");
        assertEquals(1, r.matchesOf(SensitiveType.EMAIL).size());
        assertEquals(1, r.matchesOf(SensitiveType.IP).size());
        assertEquals(1, r.matchesOf(SensitiveType.MOBILE).size());
        assertEquals(0, r.matchesOf(SensitiveType.BANK_CARD).size());
        assertEquals(3, r.types().size());
        assertTrue(r.types().contains(SensitiveType.URL) == false);
    }

    @Test
    void 命中按起点升序且区间不重叠() {
        String text = "13800138000 与 13900139000";
        SensitiveResult r = SensitiveDetector.create().detect(text);
        assertEquals(2, r.size());
        int cursor = 0;
        for (SensitiveMatch m : r.matches()) {
            assertTrue(m.start() >= cursor, "命中未按起点排序");
            assertEquals(text.substring(m.start(), m.end()), m.value());
            cursor = m.end();
        }
    }

    @Test
    void 自定义类型的掩码用默认位数() {
        SensitiveDetector d = SensitiveDetector.withPiiRules(
                WordDictionary.create()
                        .add(WordEntry.of("内部代号", SensitiveType.CUSTOM, "code"))
                        .add(WordEntry.of("机密专用代号", SensitiveType.CUSTOM, "code")),
                Collections.<com.zifang.util.sensitive.pii.PiiRule>emptyList());
        // 4 个字，保留位已覆盖全串，直接退化成全遮
        assertEquals("****", d.mask("内部代号", MaskStyle.HEAD_TAIL));
        assertEquals("机密**代号", d.mask("机密专用代号", MaskStyle.HEAD_TAIL));
        assertEquals(SensitiveType.CUSTOM, d.detect("内部代号").matches().get(0).type());
        // 词库声明的类型要跟着一路报出来，不能一律当成 WORD
        assertEquals(SensitiveType.CUSTOM, d.detect("机密专用代号").matches().get(0).type());
    }

    @Test
    void 空PII规则集时只跑敏感词() {
        SensitiveDetector d = SensitiveDetector.withPiiRules(
                WordDictionary.create().add("赌博"),
                Collections.<com.zifang.util.sensitive.pii.PiiRule>emptyList());
        SensitiveResult r = d.detect("赌博 13800138000");
        assertEquals(1, r.size());
        assertEquals(SensitiveType.WORD, r.matches().get(0).type());
    }

    @Test
    void 默认PII规则集覆盖六类() {
        assertEquals(6, SensitiveDetector.defaultPiiRules().size());
        assertThrows(UnsupportedOperationException.class, () -> SensitiveDetector.defaultPiiRules().clear());
        SensitiveResult r = SensitiveDetector.create()
                .detect("11010519491231002X 4111111111111111 13800138000 a@b.cn 192.168.1.10 http://a.cn");
        assertTrue(r.types().containsAll(Arrays.asList(
                SensitiveType.ID_CARD, SensitiveType.BANK_CARD, SensitiveType.MOBILE,
                SensitiveType.EMAIL, SensitiveType.IP, SensitiveType.URL)), r.types().toString());
    }

    @Test
    void 空白与null文本返回空结果() {
        SensitiveDetector d = SensitiveDetector.create(WordDictionary.create().add("赌博"));
        assertTrue(d.detect(null).isEmpty());
        assertTrue(d.detect("").isEmpty());
        assertFalse(d.detects(null));
        assertNull(d.mask(null, MaskStyle.FULL));
        assertEquals("无敏感内容", d.mask("无敏感内容", MaskStyle.FULL));
    }

    @Test
    void 类型的默认保留位数可读() {
        assertEquals(2, SensitiveType.WORD.defaultHead());
        assertEquals(2, SensitiveType.WORD.defaultTail());
        assertEquals(0, SensitiveType.URL.defaultHead());
        assertEquals(0, SensitiveType.URL.defaultTail());
        assertEquals(6, SensitiveType.ID_CARD.defaultHead());
        assertEquals(8, SensitiveType.values().length);
    }
}