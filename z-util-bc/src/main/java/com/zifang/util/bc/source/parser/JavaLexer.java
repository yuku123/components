package com.zifang.util.bc.source.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Java 词法分析器
 * <p>
 * 把源码切成记号流：标识符、关键字、数值字面量、字符串与字符字面量、标点。
 * 注释和空白直接丢掉 —— 上层要的是声明结构与方法体文本，不需要保留排版。
 * <p>
 * 字面量按源码原文保留（含引号与转义），不做反转义：{@code ClassInfo} 里的
 * 字段初始值和方法语句都是要原样再生成回源码的文本，一旦反转义就会把
 * {@code "a\nb"} 变成含裸换行的字符串字面量，生成出来的源码直接不能编译。
 *
 * @author zifang
 */
public final class JavaLexer {

    public static final int IDENTIFIER = 1;
    public static final int KEYWORD = 2;
    public static final int NUMBER = 3;
    public static final int STRING = 4;
    public static final int CHAR = 5;
    public static final int PUNCT = 6;
    public static final int EOF = 7;

    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally",
            "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected", "public", "return", "short",
            "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "true", "false", "null"));

    /** 多字符运算符，长的排前面，匹配时取最长命中 */
    private static final String[] OPERATORS = {
            ">>>=", "<<=", ">>=", ">>>", "...", "->", "::", "==", "!=", "<=", ">=", "&&", "||",
            "++", "--", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<", ">>"};

    private final String source;
    private int pos;

    public JavaLexer(String source) {
        this.source = source == null ? "" : source;
    }

    /**
     * 扫描到记号流末尾
     *
     * @return List&lt;JavaToken&gt;类型返回值，最后一个记号是 {@link #EOF}
     */
    public static List<JavaToken> tokenize(String source) {
        return new JavaLexer(source).scan();
    }

    /**
     * 切分记号
     *
     * @return List&lt;JavaToken&gt;类型返回值
     */
    public List<JavaToken> scan() {
        List<JavaToken> tokens = new ArrayList<>();
        while (true) {
            int skippedFrom = pos;
            skipIgnorable();
            boolean spaceBefore = hasWhitespace(skippedFrom, pos);
            if (pos >= source.length()) {
                tokens.add(new JavaToken(EOF, "", pos, spaceBefore));
                return tokens;
            }
            int start = pos;
            char c = source.charAt(pos);
            if (Character.isJavaIdentifierStart(c)) {
                readIdentifier();
                String text = source.substring(start, pos);
                int type = KEYWORDS.contains(text) ? KEYWORD : IDENTIFIER;
                tokens.add(new JavaToken(type, text, start, spaceBefore));
            } else if (Character.isDigit(c)
                    || (c == '.' && pos + 1 < source.length() && Character.isDigit(source.charAt(pos + 1)))) {
                readNumber();
                tokens.add(new JavaToken(NUMBER, source.substring(start, pos), start, spaceBefore));
            } else if (c == '"') {
                readQuoted('"', STRING, start, spaceBefore, tokens);
            } else if (c == '\'') {
                readQuoted('\'', CHAR, start, spaceBefore, tokens);
            } else {
                tokens.add(new JavaToken(PUNCT, readOperator(), start, spaceBefore));
            }
        }
    }

    private boolean hasWhitespace(int from, int to) {
        for (int i = from; i < to && i < source.length(); i++) {
            if (Character.isWhitespace(source.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /** 跳过空白与两种注释 */
    private void skipIgnorable() {
        while (pos < source.length()) {
            char c = source.charAt(pos);
            if (Character.isWhitespace(c)) {
                pos++;
            } else if (c == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '/') {
                while (pos < source.length() && source.charAt(pos) != '\n') {
                    pos++;
                }
            } else if (c == '/' && pos + 1 < source.length() && source.charAt(pos + 1) == '*') {
                pos += 2;
                while (pos < source.length()
                        && !(source.charAt(pos) == '*' && pos + 1 < source.length() && source.charAt(pos + 1) == '/')) {
                    pos++;
                }
                pos = Math.min(pos + 2, source.length());
            } else {
                return;
            }
        }
    }

    private void readIdentifier() {
        while (pos < source.length() && Character.isJavaIdentifierPart(source.charAt(pos))) {
            pos++;
        }
    }

    /** 数值字面量：十进制/十六进制、小数点、指数、后缀与下划线分隔符 */
    private void readNumber() {
        if (source.charAt(pos) == '0' && pos + 1 < source.length()
                && (source.charAt(pos + 1) == 'x' || source.charAt(pos + 1) == 'X')) {
            pos += 2;
            while (pos < source.length() && (isHexDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
                pos++;
            }
        } else {
            while (pos < source.length()
                    && (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
                pos++;
            }
            if (pos < source.length() && source.charAt(pos) == '.') {
                pos++;
                while (pos < source.length() && (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
                    pos++;
                }
            }
            if (pos < source.length() && (source.charAt(pos) == 'e' || source.charAt(pos) == 'E')) {
                int save = pos;
                pos++;
                if (pos < source.length() && (source.charAt(pos) == '+' || source.charAt(pos) == '-')) {
                    pos++;
                }
                if (pos < source.length() && Character.isDigit(source.charAt(pos))) {
                    while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
                        pos++;
                    }
                } else {
                    pos = save;
                }
            }
        }
        if (pos < source.length() && "lLfFdD".indexOf(source.charAt(pos)) >= 0) {
            pos++;
        }
    }

    /**
     * 读取引号包裹的字面量，遇反斜杠跳过被转义的那个字符
     */
    private void readQuoted(char quote, int type, int start, boolean spaceBefore, List<JavaToken> tokens) {
        pos++;
        while (pos < source.length()) {
            char c = source.charAt(pos);
            if (c == '\\') {
                pos += 2;
                continue;
            }
            pos++;
            if (c == quote) {
                break;
            }
        }
        tokens.add(new JavaToken(type, source.substring(start, Math.min(pos, source.length())), start, spaceBefore));
    }

    private String readOperator() {
        for (String op : OPERATORS) {
            if (source.startsWith(op, pos)) {
                pos += op.length();
                return op;
            }
        }
        pos++;
        return source.substring(pos - 1, pos);
    }

    private static boolean isHexDigit(char c) {
        return Character.isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * 词法记号
     */
    public static final class JavaToken {

        private final int type;
        private final String text;
        private final int start;
        private final boolean spaceBefore;

        JavaToken(int type, String text, int start, boolean spaceBefore) {
            this.type = type;
            this.text = text;
            this.start = start;
            this.spaceBefore = spaceBefore;
        }

        public int getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public int getStart() {
            return start;
        }

        /**
         * 本记号在源码里前面是否隔了空白
         * <p>
         * 比较运算的 {@code a &gt; b} 与泛型闭合的 {@code List&lt;T&gt; x} 在记号层面长得
         * 一模一样，靠原文有没有空格区分：源码里 {@code >} 后头有空格就是运算，没有就是泛型。
         *
         * @return boolean类型返回值
         */
        public boolean isSpaceBefore() {
            return spaceBefore;
        }

        public boolean is(String text) {
            return this.text.equals(text);
        }

        public boolean isKeyword(String keyword) {
            return type == KEYWORD && text.equals(keyword);
        }

        public boolean isIdentifier() {
            return type == IDENTIFIER;
        }

        public boolean is(int expectedType) {
            return type == expectedType;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * 把记号拼回规范化的源码文本
     * <p>
     * 空格只在真正需要的地方插：紧贴 {@code .} 与调用括号、逗号分号之后不插，
     * 关键字后与二元运算符两侧要插。这样 {@code return a + b;} 与
     * {@code return this.name;} 这类断言里的原文能原样对上。
     */
    public static String join(List<JavaToken> tokens) {
        return join(tokens, 0, tokens.size());
    }

    /**
     * 拼接一段记号为规范化文本
     *
     * @param tokens 记号列表
     * @param from   起始下标（含）
     * @param to     结束下标（不含）
     * @return String类型返回值
     */
    public static String join(List<JavaToken> tokens, int from, int to) {
        StringBuilder sb = new StringBuilder();
        for (int i = Math.max(from, 0); i < Math.min(to, tokens.size()); i++) {
            JavaToken token = tokens.get(i);
            if (token.is(EOF)) {
                continue;
            }
            JavaToken prev = i > from ? tokens.get(i - 1) : null;
            if (prev != null && needsSpace(prev, token)) {
                sb.append(' ');
            }
            sb.append(token.getText());
        }
        return sb.toString();
    }

    private static boolean needsSpace(JavaToken prev, JavaToken cur) {
        String p = prev.getText();
        String c = cur.getText();

        // 左括号只在「实参/下标/泛型/强制转型」左边的紧贴形态下不留空格，if (、for (、new ( 要留
        if (c.equals("(")) {
            return !(prev.isIdentifier() || p.equals(")") || p.equals("]") || p.equals("{")
                    || isGenericClose(p));
        }
        if (p.equals("(") || p.equals("[")) {
            return false;
        }
        if (p.equals(".") || c.equals(".")) {
            return false;
        }
        // 紧贴右括号/右方括号/逗号/分号/省略号
        if (c.equals(")") || c.equals("]") || c.equals(",") || c.equals(";") || c.equals("...")) {
            return false;
        }
        if (p.equals("...") || p.equals(",") || p.equals(";")) {
            return true;
        }
        // 花括号两侧照抄源码的留白：{1, 2} 与 { y; } 两种写法都能原样留住
        if (c.equals("{") || c.equals("}") || p.equals("{") || p.equals("}")) {
            return cur.isSpaceBefore();
        }
        // 数组类型 int[] x 与下标 a[0] 一样贴紧，方括号两侧都不留空格
        if (c.equals("[")) {
            return false;
        }
        if (p.equals("]")) {
            return true;
        }
        // 泛型与比较共用尖括号，靠原文留白区分：List<T> x 贴紧，a > b 留空格
        if (isGenericClose(p) || p.equals("<") || isGenericClose(c) || c.equals("<")) {
            return cur.isSpaceBefore();
        }
        // 自增自减贴着操作数
        if (c.equals("++") || c.equals("--")) {
            return !(prev.isIdentifier() || p.equals(")") || p.equals("]"));
        }
        if (p.equals("++") || p.equals("--")) {
            return !(c.equals(";") || c.equals(",") || c.equals(")") || c.equals(".") || c.equals("]"));
        }
        // 其余一律留空格：二元运算符、赋值、关键字与后续记号
        return true;
    }

    /** {@code >} 与合并成单个记号的移位符 {@code >> >>>=} 之类 */
    private static boolean isGenericClose(String text) {
        return !text.isEmpty() && text.charAt(0) == '>';
    }

    /**
     * 空记号列表
     *
     * @return List&lt;JavaToken&gt;类型返回值
     */
    public static List<JavaToken> empty() {
        return Collections.emptyList();
    }
}
