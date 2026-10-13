package com.zifang.util.sensitive;

import com.zifang.util.sensitive.pii.BankCardRule;
import com.zifang.util.sensitive.pii.EmailRule;
import com.zifang.util.sensitive.pii.IdCardRule;
import com.zifang.util.sensitive.pii.IpRule;
import com.zifang.util.sensitive.pii.MobileRule;
import com.zifang.util.sensitive.pii.PiiRule;
import com.zifang.util.sensitive.pii.UrlRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 敏感识别门面：敏感词 DFA + PII 规则，一次扫描出全部命中。
 * <p>
 * 除词库槽位外全部不可变，可多线程共享。词库热更新是唯一可变的地方：
 * {@link #updateWords(WordDictionary)} 用新词库建一个新匹配器再原子换引用，
 * 读侧无锁，更新期间在跑的请求不受影响。
 */
public class SensitiveDetector {

    private static final Comparator<SensitiveMatch> BY_POSITION =
            Comparator.comparingInt(SensitiveMatch::start);

    private final List<PiiRule> piiRules;
    private volatile DfaMatcher wordMatcher;

    private SensitiveDetector(DfaMatcher wordMatcher, List<PiiRule> piiRules) {
        this.wordMatcher = wordMatcher;
        this.piiRules = piiRules;
    }

    /** 默认构造：六类 PII 规则，无敏感词库 */
    public static SensitiveDetector create() {
        return new SensitiveDetector(DfaMatcher.empty(), defaultPiiRules());
    }

    public static SensitiveDetector create(WordDictionary dictionary) {
        return new SensitiveDetector(dictionary == null ? DfaMatcher.empty() : dictionary.build(), defaultPiiRules());
    }

    /** 自定义 PII 规则集（传空集合则只跑敏感词） */
    public static SensitiveDetector withPiiRules(WordDictionary dictionary, List<PiiRule> rules) {
        return new SensitiveDetector(dictionary == null ? DfaMatcher.empty() : dictionary.build(),
                rules == null ? Collections.<PiiRule>emptyList() : new ArrayList<>(rules));
    }

    public static List<PiiRule> defaultPiiRules() {
        return Collections.unmodifiableList(Arrays.<PiiRule>asList(
                new IdCardRule(), new BankCardRule(), new MobileRule(),
                new EmailRule(), new IpRule(), new UrlRule()));
    }

    /** 热更新词库。匹配器不可变，所以这里只换引用 */
    public void updateWords(WordDictionary dictionary) {
        this.wordMatcher = dictionary == null ? DfaMatcher.empty() : dictionary.build();
    }

    public SensitiveResult detect(String text) {
        if (text == null || text.isEmpty()) {
            return SensitiveResult.empty();
        }
        List<SensitiveMatch> all = new ArrayList<>();
        all.addAll(wordMatcher.match(text));
        for (PiiRule rule : piiRules) {
            all.addAll(rule.detect(text));
        }
        if (all.isEmpty()) {
            return SensitiveResult.empty();
        }
        all.sort(BY_POSITION);
        return new SensitiveResult(dropOverlaps(all));
    }

    public boolean detects(String text) {
        return !detect(text).isEmpty();
    }

    /** 识别并直接返回脱敏后的文本 */
    public String mask(String text, MaskStyle style) {
        return detect(text).mask(text, style);
    }

    /**
     * 去掉重叠命中。同起点留更长的；与已保留项相交的直接丢 ——
     * 否则掩码会互相覆盖，第二次替换落在已改写过的区间上。
     */
    private static List<SensitiveMatch> dropOverlaps(List<SensitiveMatch> sorted) {
        List<SensitiveMatch> kept = new ArrayList<>();
        int reach = -1;
        for (SensitiveMatch m : sorted) {
            if (!kept.isEmpty()) {
                SensitiveMatch last = kept.get(kept.size() - 1);
                if (m.start() == last.start()) {
                    if (m.length() <= last.length()) {
                        continue;
                    }
                    kept.remove(kept.size() - 1);
                } else if (m.start() < reach) {
                    continue;
                }
            }
            kept.add(m);
            reach = m.end();
        }
        return kept;
    }
}
