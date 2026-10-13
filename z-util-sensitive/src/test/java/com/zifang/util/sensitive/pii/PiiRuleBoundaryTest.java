package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveMatch;
import com.zifang.util.sensitive.SensitiveType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 各 PII 规则的正反例边界：正则召回 + 校验器定精确率这条分工是否真的落地。
 */
class PiiRuleBoundaryTest {

    private static List<SensitiveMatch> detect(PiiRule rule, String text) {
        return rule.detect(text);
    }

    // ---------------- 身份证 ----------------

    @Test
    void 十五位老号不收() {
        // 只收 18 位：15 位是 2000 年前的老号，正常文本里几乎不出现
        assertTrue(detect(new IdCardRule(), "110105491231001").isEmpty());
        assertEquals(1, detect(new IdCardRule(), "11010519491231002X").size());
    }

    @Test
    void 小写x也认() {
        List<SensitiveMatch> hits = detect(new IdCardRule(), "11010519491231002x");
        assertEquals(1, hits.size());
        assertEquals("11010519491231002x", hits.get(0).value());
    }

    @Test
    void 校验位按大写x匹配所以大写x是规范写法() {
        // 同一个号码两种大小写都能被校验通过，说明比较前做了大小写归一
        assertEquals(1, detect(new IdCardRule(), "11010519491231002X").size());
        assertEquals(1, detect(new IdCardRule(), "11010519491231002x").size());
    }

    @Test
    void 世纪只收19与20() {
        assertTrue(detect(new IdCardRule(), "11010518991231002X").isEmpty());
        assertTrue(detect(new IdCardRule(), "11010521991231002X").isEmpty());
    }

    @Test
    void 农历二月三十不存在() {
        assertTrue(detect(new IdCardRule(), "110105194902300016").isEmpty());
    }

    @Test
    void 标签可自定义() {
        assertEquals("自定义", detect(new IdCardRule("自定义"), "11010519491231002X").get(0).tag());
    }

    // ---------------- 银行卡 ----------------

    @Test
    void luhn校验位错的一位就不认() {
        assertEquals(1, detect(new BankCardRule(), "4111111111111111").size());
        assertTrue(detect(new BankCardRule(), "4111111111111112").isEmpty());
    }

    @Test
    void 银联62开头的长卡号可命中() {
        // 6222021234567890 格式对但校验位不过，必须被 accept 挡掉
        assertTrue(detect(new BankCardRule(), "6222021234567890").isEmpty());
        assertEquals(1, detect(new BankCardRule(), "6212123456789016").size());
    }

    @Test
    void 从长数字串里不截银行卡() {
        assertTrue(detect(new BankCardRule(), "订单号 94111111111111112345").isEmpty());
    }

    @Test
    void luhn静态方法本身可单独校验() {
        assertTrue(BankCardRule.luhn("4111111111111111"));
        assertFalse(BankCardRule.luhn("4111111111111112"));
    }

    // ---------------- 手机号 ----------------

    @Test
    void 二位不是三到九的号段不收() {
        assertTrue(detect(new MobileRule(), "12800138000").isEmpty());
        assertTrue(detect(new MobileRule(), "10800138000").isEmpty());
        assertEquals(1, detect(new MobileRule(), "13800138000").size());
        assertEquals(1, detect(new MobileRule(), "19900139000").size());
    }

    @Test
    void 号码位数不对时不截也不补() {
        assertTrue(detect(new MobileRule(), "1380013800").isEmpty());
        assertTrue(detect(new MobileRule(), "138001380000").isEmpty());
        assertTrue(detect(new MobileRule(), "订单 138001380001234").isEmpty());
    }

    // ---------------- 邮箱 ----------------

    @Test
    void 双at只认得后半截() {
        // 正则的域名段不允许出现 @，所以整串不匹配，但 b@c.com 本身是合法邮箱
        List<SensitiveMatch> hits = detect(new EmailRule(), "a@b@c.com");
        assertEquals(1, hits.size());
        assertEquals("b@c.com", hits.get(0).value());
        assertEquals(1, detect(new EmailRule(), "a.b+c@sub.example.com").size());
    }

    @Test
    void 顶级域名至少两位() {
        assertTrue(detect(new EmailRule(), "user@example.c").isEmpty());
        assertEquals(1, detect(new EmailRule(), "user@example.cn").size());
    }

    @Test
    void 邮箱两侧是中文时照常命中() {
        // 中文不属于邮箱字符集，前后带中文是最常见的正常写法，不能因为这个就漏掉
        List<SensitiveMatch> hits = detect(new EmailRule(), "前缀user@example.com后缀");
        assertEquals(1, hits.size());
        assertEquals("user@example.com", hits.get(0).value());
    }

    // ---------------- IP ----------------

    @Test
    void 段值越界不认() {
        assertTrue(detect(new IpRule(), "999.999.999.999").isEmpty());
        assertTrue(detect(new IpRule(), "192.168.1.256").isEmpty());
        assertEquals(1, detect(new IpRule(), "0.0.0.0").size());
        assertEquals(1, detect(new IpRule(), "255.255.255.255").size());
    }

    @Test
    void 不从版本号里截ip() {
        assertTrue(detect(new IpRule(), "版本 1.2.3.4.5").isEmpty());
    }

    // ---------------- URL ----------------

    @Test
    void 裸www也算url() {
        assertEquals(1, detect(new UrlRule(), "见 www.example.com/a?b=1").size());
    }

    @Test
    void 大写协议也认() {
        assertEquals(1, detect(new UrlRule(), "HTTP://EXAMPLE.COM/A").size());
    }

    @Test
    void url不吞掉后随的引号() {
        SensitiveMatch hit = detect(new UrlRule(), "\"http://a.cn/b\"").get(0);
        assertEquals("http://a.cn/b", hit.value());
    }

    // ---------------- 通用 ----------------

    @Test
    void 每条规则都带自己的类型与标签() {
        assertEquals(SensitiveType.ID_CARD, new IdCardRule().type());
        assertEquals(SensitiveType.BANK_CARD, new BankCardRule().type());
        assertEquals(SensitiveType.MOBILE, new MobileRule().type());
        assertEquals(SensitiveType.EMAIL, new EmailRule().type());
        assertEquals(SensitiveType.IP, new IpRule().type());
        assertEquals(SensitiveType.URL, new UrlRule().type());
        assertEquals("ip", new IpRule().tag());
    }

    @Test
    void 规则对空输入返回空列表而不是报错() {
        for (PiiRule rule : new PiiRule[]{new IdCardRule(), new BankCardRule(), new MobileRule(),
                new EmailRule(), new IpRule(), new UrlRule()}) {
            assertTrue(rule.detect(null).isEmpty());
            assertTrue(rule.detect("").isEmpty());
            assertFalse(rule.detects(null));
        }
    }

    @Test
    void 命中区间与原文一致() {
        String text = "手机号:13800138000 结束";
        SensitiveMatch hit = detect(new MobileRule(), text).get(0);
        assertEquals("13800138000", text.substring(hit.start(), hit.end()));
        assertEquals(11, hit.length());
    }
}