package com.zifang.util.strategy;

import com.zifang.util.strategy.rule.Condition;
import com.zifang.util.strategy.rule.CompareCondition;
import com.zifang.util.strategy.rule.Conditions;
import com.zifang.util.strategy.rule.Effect;
import com.zifang.util.strategy.rule.Rule;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 条件表达式本身的行为：翻译出的 DSL 文本、空值判定、通配/正则、字面量转义与字段名校验。
 */
class ConditionDslTest {

    private static StrategyContext facts(Object... kv) {
        StrategyContext ctx = new StrategyContext();
        for (int i = 0; i < kv.length; i += 2) {
            ctx.fact(String.valueOf(kv[i]), kv[i + 1]);
        }
        return ctx;
    }

    /** 条件成立返回 REVIEW，不成立返回 PASS —— 一眼能看出命中没命中 */
    private static DecisionOutcome eval(Condition c, StrategyContext ctx) {
        return DecisionPackage.builder("p", "1").runMode(RunMode.ACTIVE)
                .rule(Rule.builder("R").condition(c).effect(Effect.of(DecisionOutcome.REVIEW)).build())
                .build()
                .evaluate(ctx)
                .outcome();
    }

    // ---------------- 空值判定 ----------------

    @Test
    void 缺失键与显式null键都算空值() {
        StrategyContext ctx = facts("amount", 100, "n", null);
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.isNull("nope"), ctx));
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.isNull("n"), ctx));
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.absent("nope"), ctx));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.isNull("amount"), ctx));
    }

    @Test
    void 存在性判定是空值判定的取反() {
        StrategyContext ctx = facts("amount", 100);
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.isNotNull("amount"), ctx));
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.exists("amount"), ctx));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.isNotNull("nope"), ctx));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.exists("nope"), ctx));
    }

    @Test
    void 空值判定只能出isnull与is_not_null两种函数() {
        assertEquals("ISNULL(amount)", Conditions.isNull("amount").toDsl());
        assertEquals("IS_NOT_NULL(amount)", Conditions.isNotNull("amount").toDsl());
        assertThrows(IllegalArgumentException.class,
                () -> new com.zifang.util.strategy.rule.FuncCondition("TRIM", "amount"));
    }

    // ---------------- 通配与正则 ----------------

    @Test
    void 下划线匹配单字符而非任意长() {
        StrategyContext ctx = facts("code", "AB");
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.like("code", "A_"), ctx));
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.like("code", "%B%"), ctx));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.like("code", "A_"), facts("code", "ABC")));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.like("code", "%b%"), ctx));
    }

    @Test
    void 正则是整体匹配不是子串匹配() {
        StrategyContext ctx = facts("code", "AB");
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.matches("code", "[A-Z]{2}"), ctx));
        // 'A' 只是子串，整体不匹配
        assertEquals(DecisionOutcome.PASS, eval(Conditions.matches("code", "A"), ctx));
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.matches("code", ".*"), ctx));
    }

    // ---------------- 逻辑组合 ----------------

    @Test
    void not双层取反回到原条件() {
        StrategyContext ctx = facts("amount", 100);
        Condition c = Conditions.not(Conditions.not(Conditions.eq("amount", 100)));
        assertEquals(DecisionOutcome.REVIEW, eval(c, ctx));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.not(c), ctx));
    }

    @Test
    void 实例方法链与静态工厂等价() {
        StrategyContext ctx = facts("amount", 100);
        Condition chained = Conditions.gt("amount", 50).and(Conditions.lt("amount", 200)).not();
        Condition built = Conditions.not(Conditions.and(Conditions.gt("amount", 50), Conditions.lt("amount", 200)));
        assertEquals(built.toDsl(), chained.toDsl());
        assertEquals(DecisionOutcome.PASS, eval(chained, ctx));
    }

    @Test
    void 空集合的in退化为常真而不是恒假() {
        // 恒假会让"配置漏了值"变成整条规则静默失效，比常真更容易被忽略
        assertEquals("(1 = 1)", Conditions.in("amount").toDsl());
        assertEquals("(1 = 1)", Conditions.in("amount", Collections.emptyList()).toDsl());
        assertEquals("(1 = 1)", Conditions.in("amount", (java.util.List<?>) null).toDsl());
        assertEquals("(1 = 1)", Conditions.in("amount", (Object[]) null).toDsl());
    }

    @Test
    void in展开成等值或链() {
        String dsl = Conditions.in("country", "CN", "US", "JP").toDsl();
        assertEquals("(((country = 'CN') OR (country = 'US')) OR (country = 'JP'))", dsl);
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.in("country", "CN", "US"), facts("country", "US")));
        assertEquals(DecisionOutcome.PASS, eval(Conditions.in("country", "CN", "US"), facts("country", "DE")));
    }

    @Test
    void 逻辑组合的入参校验() {
        assertThrows(IllegalArgumentException.class, () -> Conditions.and(new Condition[0]));
        assertThrows(IllegalArgumentException.class, () -> Conditions.and((java.util.List<Condition>) null));
        assertThrows(IllegalArgumentException.class,
                () -> new com.zifang.util.strategy.rule.LogicalCondition("NOT",
                        Conditions.eq("a", 1), Conditions.eq("b", 2)));
        assertThrows(IllegalArgumentException.class,
                () -> new com.zifang.util.strategy.rule.LogicalCondition("XOR", Condition.alwaysTrue()));
        // 逻辑操作数里的 null 直接滤掉，剩余为空才报错
        assertEquals("(((a = 1)))", Conditions.and(Conditions.eq("a", 1), null).and(null).toDsl());
        assertThrows(IllegalArgumentException.class, () -> Conditions.and((Condition) null));
    }

    @Test
    void 布尔与数字字面量不加引号() {
        assertEquals("(flag = TRUE)", Conditions.eq("flag", true).toDsl());
        assertEquals("(flag = FALSE)", Conditions.eq("flag", false).toDsl());
        assertEquals("(amount > 10.5)", Conditions.gt("amount", 10.5d).toDsl());
        assertEquals("(amount > 10)", Conditions.gt("amount", 10L).toDsl());
        assertEquals("(code = NULL)", Conditions.eq("code", null).toDsl());
    }

    @Test
    void 字面量里的引号与反斜杠被转义() {
        Condition c = Conditions.eq("name", "O'Brien");
        assertEquals("(name = 'O\\'Brien')", c.toDsl());
        // 转义后仍能求值：值里的引号没把表达式提前闭合
        assertEquals(DecisionOutcome.REVIEW, eval(c, facts("name", "O'Brien")));

        Condition backslash = Conditions.eq("path", "C:\\tmp");
        assertEquals(DecisionOutcome.REVIEW, eval(backslash, facts("path", "C:\\tmp")));
    }

    // ---------------- 操作符白名单 ----------------

    @Test
    void 操作符只放行白名单里的那些() {
        assertEquals("=", ((CompareCondition) Conditions.compare("a", "==", 1)).operator());
        assertEquals("!=", ((CompareCondition) Conditions.compare("a", "!=", 1)).operator());
        assertEquals("<>", ((CompareCondition) Conditions.compare("a", "<>", 1)).operator());
        assertEquals("LIKE", ((CompareCondition) Conditions.compare("a", "like", "%x%")).operator());
        assertThrows(IllegalArgumentException.class, () -> Conditions.compare("a", "+", 1));
        assertThrows(IllegalArgumentException.class, () -> Conditions.compare("a", "OR", 1));
        assertThrows(IllegalArgumentException.class, () -> Conditions.compare("a", null, 1));
    }

    @Test
    void 字段名挡掉会改变表达式结构的字符() {
        for (String bad : new String[]{"", " ", "1abc", "a b", "a)OR(1=1", "a'b", "a,b", "a[0]"}) {
            assertThrows(IllegalArgumentException.class, () -> Conditions.eq(bad, 1), "应被拒: [" + bad + "]");
        }
        assertThrows(IllegalArgumentException.class, () -> Conditions.eq(null, 1));
        assertThrows(IllegalArgumentException.class, () -> Conditions.isNull(null));
    }

    @Test
    void 字段名放行标识符下划线与点号路径() {
        assertEquals("(user_id = 1)", Conditions.eq("user_id", 1).toDsl());
        assertEquals("(user.profile.vip = 1)", Conditions.eq("user.profile.vip", 1).toDsl());
        assertEquals("(_id = 1)", Conditions.eq("_id", 1).toDsl());
    }

    // ---------------- 原始 DSL ----------------

    @Test
    void 原始dsl文本两边补括号且空白被裁掉() {
        assertEquals("(amount > 1000)", Conditions.dsl("  amount > 1000  ").toDsl());
        assertEquals(DecisionOutcome.REVIEW, eval(Conditions.dsl("amount > 1000"), facts("amount", 2000)));
        assertThrows(IllegalArgumentException.class, () -> Conditions.dsl(null));
        assertThrows(IllegalArgumentException.class, () -> Conditions.dsl("   "));
    }

    @Test
    void 常真条件在任意事实集上都命中() {
        assertEquals(DecisionOutcome.REVIEW, eval(Condition.alwaysTrue(), facts()));
        assertEquals(DecisionOutcome.REVIEW, eval(Condition.alwaysTrue(), facts("a", 1)));
        assertEquals("(1 = 1)", Condition.alwaysTrue().toDsl());
    }

    @Test
    void 规则未给条件时按常真处理() {
        Decision d = DecisionPackage.builder("p", "1").runMode(RunMode.ACTIVE)
                .rule(Rule.builder("R").effect(Effect.of(DecisionOutcome.MANUAL)).build())
                .build()
                .evaluate(facts());
        assertEquals(DecisionOutcome.MANUAL, d.outcome());
    }

    @Test
    void 条件树不可变() {
        java.util.List<Condition> list = Arrays.asList(Conditions.eq("a", 1), Conditions.eq("b", 2));
        Condition and = Conditions.and(list);
        assertEquals("((a = 1) AND (b = 2))", and.toDsl());
        java.util.List<Condition> operands = ((com.zifang.util.strategy.rule.LogicalCondition) and).conditions();
        assertThrows(UnsupportedOperationException.class, () -> operands.add(Conditions.eq("c", 3)));
    }
}