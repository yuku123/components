package com.zifang.util.bc.source;

import com.zifang.util.bc.source.parser.JavaLexer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 自研词法分析器测试
 */
public class JavaLexerTest {

    /**
     * 记号文本序列，最后的 EOF 不计
     */
    private static List<String> texts(String source) {
        List<String> result = new ArrayList<>();
        for (JavaLexer.JavaToken token : JavaLexer.tokenize(source)) {
            if (!token.is(JavaLexer.EOF)) {
                result.add(token.getText());
            }
        }
        return result;
    }

    @Test
    public void keywordsAndIdentifiersAreDistinguished() {
        List<String> tokens = texts("public class Foo { int bar; }");
        assertEquals(8, tokens.size());
        assertEquals(JavaLexer.KEYWORD, JavaLexer.tokenize("public").get(0).getType());
        assertEquals(JavaLexer.IDENTIFIER, JavaLexer.tokenize("public Foo").get(1).getType());
    }

    @Test
    public void commentsAreDropped() {
        assertEquals(texts("int a = 1;"),
                texts("// 注释里有分号 ; {}\nint a /* 块注释 } \" 引号 */ = 1; // 行尾注释"));
    }

    @Test
    public void stringLiteralKeepsEscapesAndSlashes() {
        List<String> tokens = texts("String s = \"a\\nb\\\"//c\";");
        assertEquals("\"a\\nb\\\"//c\"", tokens.get(3));
    }

    @Test
    public void charLiteralWithEscapedQuote() {
        List<String> tokens = texts("char c = '\\'';");
        assertEquals("'\\''", tokens.get(3));
        assertEquals(JavaLexer.CHAR, JavaLexer.tokenize("'\\''").get(0).getType());
    }

    @Test
    public void numericLiteralForms() {
        assertEquals("0xFFL", texts("long a = 0xFFL;").get(3));
        assertEquals("1.5e-3", texts("double a = 1.5e-3;").get(3));
        assertEquals("100", texts("int a = 100;").get(3));
        assertEquals(JavaLexer.NUMBER, JavaLexer.tokenize("0xFFL").get(0).getType());
    }

    @Test
    public void shiftOperatorIsNotSplitSoNestedGenericsSurvive() {
        assertEquals("Map<String, List<Integer>>", JavaLexer.join(JavaLexer.tokenize("Map<String, List<Integer>>")));
        assertEquals(1, count(texts("int a = 1 >> 2;"), ">>"));
    }

    @Test
    public void angleBracketIsOperatorOrGenericDependingOnSourceSpacing() {
        assertEquals("if (total > 0) {", join("if (total > 0) {"));
        assertEquals("if (total>0) {", join("if (total>0) {"));
        assertEquals("List<String> items", join("List<String> items"));
        assertEquals("Map<String, List<Integer>> index", join("Map<String, List<Integer>> index"));
        assertEquals("new ArrayList<>()", join("new ArrayList<>()"));
    }

    private static int count(List<String> tokens, String text) {
        int n = 0;
        for (String token : tokens) {
            if (token.equals(text)) {
                n++;
            }
        }
        return n;
    }

    @Test
    public void joinNormalizesSpacing() {
        assertEquals("return this.name;", join("return    this . name ;"));
        assertEquals("return a + b;", join("return a+b;"));
        assertEquals("System.out.println(\"Running\");", join("System . out . println ( \"Running\" ) ;"));
        assertEquals("int[] grid", join("int [ ] grid"));
        assertEquals("String... parts", join("String ... parts"));
        assertEquals("new ArrayList<>()", join("new ArrayList<> ()"));
    }

    private static String join(String source) {
        return JavaLexer.join(JavaLexer.tokenize(source));
    }

    @Test
    public void emptySourceYieldsOnlyEof() {
        List<JavaLexer.JavaToken> tokens = JavaLexer.tokenize("");
        assertEquals(1, tokens.size());
        assertTrue(tokens.get(0).is(JavaLexer.EOF));
    }
}
