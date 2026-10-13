package com.zifang.util.sensitive;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 词库与 DFA 匹配器：词条去重、多次出现、最长匹配、ASCII 词、并发热更新。
 */
class WordDictionaryDfaTest {

    private static List<String> valuesOf(SensitiveResult result) {
        List<String> values = new ArrayList<>();
        for (SensitiveMatch m : result.matches()) {
            values.add(m.value());
        }
        return values;
    }

    // ---------------- 词库 ----------------

    @Test
    void 同一词条重复加入只留一条() {
        WordDictionary dict = WordDictionary.create().add("赌博").add("赌博");
        assertEquals(1, dict.size());
        assertEquals(1, dict.build().match("赌博").size());
    }

    @Test
    void 同词不同类型或标签算不同词条() {
        WordDictionary dict = WordDictionary.create()
                .add(WordEntry.of("菠菜", SensitiveType.WORD, "gamble"))
                .add(WordEntry.of("菠菜", SensitiveType.CUSTOM, "crypto"));
        assertEquals(2, dict.size());
        // 类型与标签都保留，逐条报出，不互相覆盖
        List<SensitiveMatch> hits = dict.build().match("菠菜");
        assertEquals(2, hits.size());
        assertEquals(SensitiveType.WORD, hits.get(0).type());
        assertEquals("gamble", hits.get(0).tag());
        assertEquals(SensitiveType.CUSTOM, hits.get(1).type());
        assertEquals("crypto", hits.get(1).tag());
    }

    @Test
    void 词条首尾空白被裁掉() {
        WordDictionary dict = WordDictionary.create().add("  赌博  ");
        assertEquals("赌博", dict.entries().iterator().next().word());
        assertTrue(dict.build().matches("这里有赌博行为"));
    }

    @Test
    void 空词与null词条被拒或忽略() {
        assertThrows(IllegalArgumentException.class, () -> WordEntry.of(null));
        assertThrows(IllegalArgumentException.class, () -> WordEntry.of("  "));
        // 词条对象为 null 是忽略，字符串为 null 则在词条构造时就拒
        WordDictionary dict = WordDictionary.create().add((WordEntry) null);
        assertEquals(0, dict.size());
        assertThrows(IllegalArgumentException.class, () -> WordDictionary.create().add((String) null));
    }

    @Test
    void 词库可合并且视图不可改() {
        WordDictionary a = WordDictionary.create().add("赌博");
        WordDictionary b = WordDictionary.create().add("诈骗").add("赌博");
        a.addAll(b);
        assertEquals(2, a.size());
        a.addAll(null);
        assertEquals(2, a.size());
        assertThrows(UnsupportedOperationException.class, () -> a.entries().clear());
    }

    @Test
    void 同一词库建两次得到两个独立匹配器() {
        WordDictionary dict = WordDictionary.create().add("赌博");
        DfaMatcher m1 = dict.build();
        DfaMatcher m2 = dict.build();
        assertNotSame(m1, m2);
        assertEquals(m1.match("赌博").size(), m2.match("赌博").size());
    }

    // ---------------- 匹配 ----------------

    @Test
    void 同一词出现多次每一处都报() {
        DfaMatcher m = WordDictionary.create().add("赌博").build();
        List<SensitiveMatch> hits = m.match("赌博团伙，赌博的可疑");
        assertEquals(2, hits.size());
        assertEquals(0, hits.get(0).start());
        assertEquals(0, hits.get(1).start() - 5);
    }

    @Test
    void 单字符词也能命中() {
        DfaMatcher m = WordDictionary.create().add("毒").build();
        assertTrue(m.matches("毒品"));
        assertEquals(1, m.match("涉毒").size());
    }

    @Test
    void 词出现在文本末尾也能命中() {
        DfaMatcher m = WordDictionary.create().add("赌博").build();
        assertTrue(m.matches("这是一场赌博"));
        assertEquals(4, m.match("这是一场赌博").get(0).start());
    }

    @Test
    void 纯ascii词正常工作() {
        DfaMatcher m = WordDictionary.create().add("fuck").build();
        assertTrue(m.matches("what the fuck"));
        assertEquals("fuck", m.match("what the fuck").get(0).value());
    }

    @Test
    void 中英混合词也能建自动机() {
        DfaMatcher m = WordDictionary.create().add("代开发票").add("VPN").build();
        assertTrue(m.matches("提供代开发票"));
        assertTrue(m.matches("自建VPN"));
        assertFalse(m.matches("开发票"));
    }

    @Test
    void 命中区间可直接切出原文() {
        String text = "这里有赌博行为，请核实";
        DfaMatcher m = WordDictionary.create().add("赌博").build();
        SensitiveMatch hit = m.match(text).get(0);
        assertEquals("赌博", text.substring(hit.start(), hit.end()));
        assertEquals(2, hit.length());
    }

    @Test
    void 空词库的匹配器什么都不命中() {
        DfaMatcher empty = WordDictionary.create().build();
        assertFalse(empty.hasWords());
        assertFalse(empty.matches("赌博"));
        assertTrue(empty.match("赌博").isEmpty());
        assertFalse(DfaMatcher.empty().hasWords());
        assertTrue(DfaMatcher.empty().match(null).isEmpty());
        assertFalse(DfaMatcher.empty().matches(null));
    }

    @Test
    void 命中项的toString带区间与标签() {
        SensitiveMatch m = new SensitiveMatch(SensitiveType.WORD, "赌博", 3, 5, "gamble");
        assertEquals("WORD[3,5)=赌博 tag=gamble", m.toString());
        assertEquals(2, m.length());
        assertEquals(SensitiveType.WORD, m.type());
        assertEquals("赌博", m.value());
    }

    // ---------------- 并发 ----------------

    @Test
    void 词库热更新与匹配并发进行不炸() throws Exception {
        final SensitiveDetector detector = SensitiveDetector.create(WordDictionary.create().add("赌博"));
        int threads = 8;
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(threads);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < 2000; i++) {
                        detector.detect("赌博团伙在这里出现");
                    }
                } catch (Throwable e) {
                    failure.compareAndSet(null, e);
                } finally {
                    done.countDown();
                }
            });
        }
        pool.submit(() -> {
            try {
                start.await();
                for (int i = 0; i < 200; i++) {
                    detector.updateWords(WordDictionary.create().add("诈骗"));
                }
            } catch (Throwable e) {
                failure.compareAndSet(null, e);
            }
        });
        start.countDown();
        assertTrue(done.await(60, TimeUnit.SECONDS));
        pool.shutdown();
        assertEquals(null, failure.get());
    }

    @Test
    void 多个检测器互不影响() {
        SensitiveDetector a = SensitiveDetector.create(WordDictionary.create().add("赌博"));
        SensitiveDetector b = SensitiveDetector.create(WordDictionary.create().add("诈骗"));
        assertTrue(a.detects("赌博"));
        assertFalse(b.detects("赌博"));
        assertTrue(b.detects("诈骗"));
    }

    @Test
    void 传null词库等价于无词库() {
        SensitiveDetector d = SensitiveDetector.create(null);
        assertFalse(d.detects("赌博"));
        d.updateWords(null);
        assertFalse(d.detects("赌博"));
        assertFalse(SensitiveDetector.withPiiRules(null, null).detect("赌博 11010519491231002X").hasType(SensitiveType.ID_CARD));
    }
}