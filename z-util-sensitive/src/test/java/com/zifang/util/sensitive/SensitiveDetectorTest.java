package com.zifang.util.sensitive;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensitiveDetectorTest {

    private static SensitiveDetector words() {
        return SensitiveDetector.create(WordDictionary.create()
                .add("赌博", SensitiveType.WORD, "gamble")
                .add("博彩")
                .add("法轮"));
    }

    // ---------------- DFA ----------------

    @Test
    void dfa_命中中文敏感词并给出区间() {
        SensitiveResult r = words().detect("这里有赌博网站，请访问");
        assertEquals(1, r.size());
        SensitiveMatch m = r.matches().get(0);
        assertEquals(SensitiveType.WORD, m.type());
        assertEquals("赌博", m.value());
        assertEquals("gamble", m.tag());
        assertEquals(3, m.start());
        assertEquals(5, m.end());
        assertEquals("赌博", "这里有赌博网站，请访问".substring(m.start(), m.end()));
    }

    @Test
    void dfa_同起点取最长匹配不层层重叠() {
        WordDictionary dict = WordDictionary.create().add("赌").add("赌博").add("赌博网站");
        SensitiveResult r = SensitiveDetector.create(dict).detect("赌博网站");
        assertEquals(1, r.size(), "三个词互相包含，只该报最长的那个");
        assertEquals("赌博网站", r.matches().get(0).value());
    }

    @Test
    void dfa_不误伤邻近文本() {
        assertFalse(words().detects("赌具店的营业公告"), "赌具 ≠ 赌博，不该命中");
        assertFalse(words().detects("今天运气不错"));
        assertTrue(words().detects("涉及博彩"));
    }

    @Test
    void dfa_空词库与空文本都不炸() {
        SensitiveDetector d = SensitiveDetector.create();
        assertEquals(0, SensitiveDetector.create(WordDictionary.create()).detect("任意文本").size());
        assertTrue(d.detect(null).isEmpty());
        assertTrue(d.detect("").isEmpty());
    }

    @Test
    void dfa_大小写与标点不影响匹配() {
        assertTrue(words().detects("【赌博】，赌博"));
        assertEquals(2, words().detect("【赌博】，赌博").size());
    }

    @Test
    void 词条空白被拒() {
        assertThrows(IllegalArgumentException.class, () -> WordEntry.of("   "));
        assertThrows(IllegalArgumentException.class, () -> WordEntry.of(null));
    }

    @Test
    void 词库热更新是换引用_更新后新词可命中() {
        SensitiveDetector d = words();
        assertFalse(d.detect("新增违禁词").hasType(SensitiveType.WORD));
        d.updateWords(WordDictionary.create().add("新增违禁词"));
        assertTrue(d.detect("新增违禁词").hasType(SensitiveType.WORD));
    }

    // ---------------- PII ----------------

    @Test
    void 身份证_校验位错的必须拒掉() {
        SensitiveDetector d = SensitiveDetector.create();
        assertTrue(d.detect("身份证 11010519491231002X").hasType(SensitiveType.ID_CARD));
        assertFalse(d.detect("编号 110105194912310021").hasType(SensitiveType.ID_CARD),
                "格式对但校验位错，不得当成身份证");
    }

    @Test
    void 身份证_非法日期与非法省码拒掉() {
        SensitiveDetector d = SensitiveDetector.create();
        assertFalse(d.detect("110101199013073319").hasType(SensitiveType.ID_CARD), "13 月不存在");
        assertFalse(d.detect("990101194912310029").hasType(SensitiveType.ID_CARD), "省码 99 不存在");
    }

    @Test
    void 身份证_不从长数字串里截取() {
        SensitiveDetector d = SensitiveDetector.create();
        assertFalse(d.detect("订单号 911010519491231002X88").hasType(SensitiveType.ID_CARD));
    }

    @Test
    void 银行卡_走luhn校验() {
        SensitiveDetector d = SensitiveDetector.create();
        assertTrue(d.detect("卡号 4111111111111111").hasType(SensitiveType.BANK_CARD));
        assertFalse(d.detect("卡号 4111111111111112").hasType(SensitiveType.BANK_CARD), "Luhn 不过，不是卡号");
        assertFalse(d.detect("金额 6222021234567890").hasType(SensitiveType.BANK_CARD));
    }

    @Test
    void 手机号_号段与长度() {
        SensitiveDetector d = SensitiveDetector.create();
        assertTrue(d.detect("联系 13812345678").hasType(SensitiveType.MOBILE));
        assertFalse(d.detect("座机 12812345678").hasType(SensitiveType.MOBILE), "128 不是移动号段");
        assertFalse(d.detect("订单 913812345678").hasType(SensitiveType.MOBILE), "13 位不是手机号");
    }

    @Test
    void 邮箱_ip_url() {
        SensitiveDetector d = SensitiveDetector.create();
        SensitiveResult r = d.detect("联系 zhang.san@example.com，服务器 192.168.1.10，站点 www.example.com/a?b=1");
        assertTrue(r.hasType(SensitiveType.EMAIL));
        assertTrue(r.hasType(SensitiveType.IP));
        assertTrue(r.hasType(SensitiveType.URL));
    }

    @Test
    void ip_段值越界拒掉() {
        SensitiveDetector d = SensitiveDetector.create();
        assertTrue(d.detect("来自 10.0.0.1").hasType(SensitiveType.IP));
        assertFalse(d.detect("版本 999.999.999.999").hasType(SensitiveType.IP));
    }

    @Test
    void 同一段文本多类命中互不干扰且不重叠() {
        SensitiveDetector d = words();
        SensitiveResult r = d.detect("赌博网站，手机 13812345678，邮箱 a@b.com");
        assertTrue(r.size() >= 3);
        for (int i = 1; i < r.matches().size(); i++) {
            assertTrue(r.matches().get(i).start() >= r.matches().get(i - 1).end(),
                    "命中区间不应重叠: " + r);
        }
    }

    // ---------------- 脱敏 ----------------

    @Test
    void 脱敏_保留头尾且长度不变() {
        String text = "身份证 11010519491231002X";
        String masked = SensitiveDetector.create().mask(text, MaskStyle.HEAD_TAIL);
        assertEquals("身份证 110105********002X", masked);
    }

    @Test
    void 脱敏_全遮与哈希() {
        String text = "手机 13812345678";
        SensitiveDetector d = SensitiveDetector.create();
        String full = d.mask(text, MaskStyle.FULL);
        assertEquals("手机 ***********", full);

        String hashed = d.mask(text, MaskStyle.HASH);
        assertTrue(hashed.startsWith("手机 "));
        assertEquals(8, hashed.substring(3).length());
        assertEquals(hashed, d.mask(text, MaskStyle.HASH), "同值哈希稳定");
        assertFalse(d.mask("手机 13900000000", MaskStyle.HASH).equals(hashed));
    }

    @Test
    void 脱敏_邮箱保域名() {
        String masked = SensitiveDetector.create().mask("zhangsan@example.com", MaskStyle.HEAD_TAIL);
        assertTrue(masked.endsWith("@example.com"), "域名是要留的可用信息: " + masked);
        assertFalse(masked.contains("zhangsan"));
    }

    @Test
    void 脱敏_无命中原文不动() {
        String text = "一段干净文本";
        assertEquals(text, SensitiveDetector.create().mask(text, MaskStyle.FULL));
        assertNull(SensitiveDetector.create().mask(null, MaskStyle.FULL));
    }

    @Test
    void 结果可按类型筛选() {
        SensitiveResult r = SensitiveDetector.create().detect("a@b.com 和 13812345678");
        assertEquals(1, r.matchesOf(SensitiveType.EMAIL).size());
        assertEquals(1, r.matchesOf(SensitiveType.MOBILE).size());
        assertEquals(0, r.matchesOf(SensitiveType.ID_CARD).size());
        assertEquals(2, r.types().size());
        assertNotNull(r.toString());
    }

    // ---------------- 并发 ----------------

    @Test
    void 检测器多线程共享安全() throws Exception {
        SensitiveDetector detector = words();
        String text = "赌博网站 11010519491231002X";
        List<String> expected = detector.detect(text).matches().stream()
                .map(SensitiveMatch::value).collect(java.util.stream.Collectors.toList());

        int threads = 8;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    for (int n = 0; n < 3000; n++) {
                        List<String> got = detector.detect(text).matches().stream()
                                .map(SensitiveMatch::value).collect(java.util.stream.Collectors.toList());
                        assertEquals(expected, got);
                    }
                } catch (Throwable t) {
                    failure.compareAndSet(null, t);
                } finally {
                    done.countDown();
                }
            });
        }
        for (int n = 0; n < 200; n++) {
            detector.updateWords(WordDictionary.create().add("赌博", SensitiveType.WORD, "gamble"));
        }
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS));
        pool.shutdownNow();
        assertEquals(null, failure.get(), "并发检测不得抛异常: " + failure.get());
    }
}
