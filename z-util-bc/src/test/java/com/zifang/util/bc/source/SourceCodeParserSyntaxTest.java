package com.zifang.util.bc.source;

import com.zifang.util.bc.source.generator.info.AnnotationInfo;
import com.zifang.util.bc.source.generator.info.ClassInfo;
import com.zifang.util.bc.source.generator.info.FieldInfo;
import com.zifang.util.bc.source.generator.info.MethodInfo;
import com.zifang.util.bc.source.parser.SourceCodeParser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 自研声明级解析器测试
 * <p>
 * 覆盖 SourceParserGeneratorTest 没走到的语法形态：泛型、可变参、数组、
 * 带块的控制流语句、限定名注解、接口继承、静态初始化块、多声明符字段。
 */
public class SourceCodeParserSyntaxTest {

    private final SourceCodeParser parser = new SourceCodeParser();

    private static String wrap(String body) {
        return "package com.example;\n\npublic class Sample {\n" + body + "\n}\n";
    }

    @Test
    public void genericFieldAndMethodTypes() {
        ClassInfo info = parser.parseSource(wrap(
                "    private List<String> items;\n"
                        + "    private Map<String, List<Integer>> index;\n"
                        + "    public <T> T first(List<T> xs) {\n"
                        + "        return xs.get(0);\n"
                        + "    }\n"));

        assertEquals(2, info.getFields().size());
        assertEquals("List<String>", info.getFields().get(0).getType());
        assertEquals("Map<String, List<Integer>>", info.getFields().get(1).getType());

        MethodInfo method = info.getMethods().get(0);
        assertEquals("first", method.getMethodName());
        assertEquals("T", method.getReturnType());
        assertEquals(1, method.getMethodParameterPairs().size());
        assertEquals("List<T>", method.getMethodParameterPairs().get(0).getParamType());
        assertEquals("xs", method.getMethodParameterPairs().get(0).getParamName());
    }

    @Test
    public void varargsParameter() {
        ClassInfo info = parser.parseSource(wrap(
                "    public void log(String... parts) {\n"
                        + "        System.out.println(parts.length);\n"
                        + "    }\n"));

        assertEquals("String...", info.getMethods().get(0).getMethodParameterPairs().get(0).getParamType());
    }

    @Test
    public void arrayFieldWithArrayInitializer() {
        ClassInfo info = parser.parseSource(wrap(
                "    private int[] grid = {1, 2, 3};\n"
                        + "    private static final String[] NAMES = new String[]{\"a\", \"b\"};\n"));

        assertEquals(2, info.getFields().size());
        assertEquals("int[]", info.getFields().get(0).getType());
        assertEquals("{1, 2, 3}", info.getFields().get(0).getInitializer());
        assertEquals("String[]", info.getFields().get(1).getType());
    }

    @Test
    public void blockStatementsStayWhole() {
        ClassInfo info = parser.parseSource(wrap(
                "    public void run(int n) {\n"
                        + "        int total = 0;\n"
                        + "        for (int i = 0; i < n; i++) {\n"
                        + "            total += i;\n"
                        + "        }\n"
                        + "        if (total > 0) {\n"
                        + "            System.out.println(total);\n"
                        + "        }\n"
                        + "    }\n"));

        List<String> statements = info.getMethods().get(0).getStatements();
        assertEquals(3, statements.size());
        assertEquals("int total = 0;", statements.get(0));
        assertEquals("for (int i = 0; i < n; i++) { total += i; }", statements.get(1));
        assertEquals("if (total > 0) { System.out.println(total); }", statements.get(2));
    }

    @Test
    public void arrayInitializerBraceDoesNotEndTheStatement() {
        ClassInfo info = parser.parseSource(wrap(
                "    public void set(int v) {\n"
                        + "        this.bytes = new byte[]{(byte) (v >>> 24), (byte) v};\n"
                        + "    }\n"));

        List<String> statements = info.getMethods().get(0).getStatements();
        assertEquals(1, statements.size());
        assertEquals("this.bytes = new byte[]{(byte)(v >>> 24), (byte) v};", statements.get(0));
    }

    @Test
    public void tryCatchStaysOneStatement() {
        ClassInfo info = parser.parseSource(wrap(
                "    public void run() {\n"
                        + "        try {\n"
                        + "            step();\n"
                        + "        } catch (Exception e) {\n"
                        + "            report(e);\n"
                        + "        } finally {\n"
                        + "            cleanup();\n"
                        + "        }\n"
                        + "    }\n"));

        List<String> statements = info.getMethods().get(0).getStatements();
        assertEquals(1, statements.size());
        assertTrue(statements.get(0).startsWith("try { step(); } catch (Exception e) {"));
        assertTrue(statements.get(0).endsWith("} finally { cleanup(); }"));
    }

    @Test
    public void switchBlockIsOneStatement() {
        ClassInfo info = parser.parseSource(wrap(
                "    public int size(int t) {\n"
                        + "        switch (t) {\n"
                        + "            case 1:\n"
                        + "                return 2;\n"
                        + "            default:\n"
                        + "                return 0;\n"
                        + "        }\n"
                        + "    }\n"));

        assertEquals(1, info.getMethods().get(0).getStatements().size());
    }

    @Test
    public void qualifiedAnnotationWithNamedArguments() {
        ClassInfo info = parser.parseSource(
                "package com.example;\n\n@com.foo.Bar(name = \"x\", count = 2)\npublic class Sample {\n}\n");

        assertEquals(1, info.getAnnotations().size());
        AnnotationInfo annotation = info.getAnnotations().get(0);
        assertEquals("com.foo.Bar", annotation.getType());
        assertEquals(2, annotation.getMembers().size());
        assertEquals("name", annotation.getMembers().get(0).getName());
        assertEquals("\"x\"", annotation.getMembers().get(0).getValue());
        assertEquals("count", annotation.getMembers().get(1).getName());
        assertEquals("2", annotation.getMembers().get(1).getValue());
    }

    @Test
    public void interfaceExtendsGoesToInterfaceListNotSuperClass() {
        ClassInfo info = parser.parseSource(
                "package com.example;\n\npublic interface Shape extends Drawable, Sized {\n"
                        + "    double area();\n}\n");

        assertTrue(info.getInterfaceType());
        assertNull(info.getSuperClass());
        assertEquals(2, info.getInterfaces().size());
        assertEquals("Drawable", info.getInterfaces().get(0).getSimpleClassName());
        assertEquals("Sized", info.getInterfaces().get(1).getSimpleClassName());
        assertEquals(1, info.getMethods().size());
    }

    @Test
    public void staticInitializerAndThrowsClause() {
        ClassInfo info = parser.parseSource(wrap(
                "    static int count;\n"
                        + "    static {\n"
                        + "        count = 1;\n"
                        + "    }\n"
                        + "    public void close() throws IOException {\n"
                        + "    }\n"));

        assertEquals(1, info.getFields().size());
        assertEquals(1, info.getMethods().size());
        assertEquals("close", info.getMethods().get(0).getMethodName());
        assertTrue(info.getMethods().get(0).getStatements().isEmpty());
    }

    @Test
    public void multiDeclaratorFieldKeepsOnlyTheFirstOne() {
        ClassInfo info = parser.parseSource(wrap("    private int a = 1, b = 2;\n"));

        assertEquals(1, info.getFields().size());
        FieldInfo field = info.getFields().get(0);
        assertEquals("a", field.getValue());
        assertEquals("1", field.getInitializer());
    }

    @Test
    public void semicolonsInsideStringLiteralDoNotSplitStatements() {
        ClassInfo info = parser.parseSource(wrap(
                "    public String describe() {\n"
                        + "        String s = \"a;b{c\";\n"
                        + "        return s;\n"
                        + "    }\n"));

        List<String> statements = info.getMethods().get(0).getStatements();
        assertEquals(2, statements.size());
        assertEquals("String s = \"a;b{c\";", statements.get(0));
    }

    @Test
    public void javadocContainingBracesAndSemicolonsIsIgnored() {
        ClassInfo info = parser.parseSource(wrap(
                "    /**\n"
                        + "     * 见 {@link Sample#getCount()} 与 {@code a; b { c }}\n"
                        + "     */\n"
                        + "    public int getCount() {\n"
                        + "        return 1;\n"
                        + "    }\n"));

        assertEquals(1, info.getMethods().size());
        assertEquals("getCount", info.getMethods().get(0).getMethodName());
    }

    @Test
    public void superClassUsesSimpleNameOfQualifiedType() {
        ClassInfo info = parser.parseSource(
                "package com.example;\n\npublic class Impl extends com.base.AbstractBase implements java.io.Serializable {\n}\n");

        assertEquals("AbstractBase", info.getSuperClass().getSimpleClassName());
        assertEquals("Serializable", info.getInterfaces().get(0).getSimpleClassName());
    }

    @Test
    public void onlyTheFirstTopLevelTypeIsTaken() {
        ClassInfo info = parser.parseSource(
                "package com.example;\n\nclass First {\n}\n\nclass Second {\n}\n");
        assertEquals("First", info.getSimpleClassName());
    }

    @Test
    public void importsAreCollectedAndStaticKeywordStripped() {
        ClassInfo info = parser.parseSource(
                "package com.example;\n\nimport java.util.List;\nimport static java.util.Arrays.asList;\n"
                        + "\npublic class Sample {\n}\n");

        assertEquals(2, info.getImports().size());
        assertEquals("java.util.List", info.getImports().get(0));
        assertEquals("java.util.Arrays.asList", info.getImports().get(1));
    }

    @Test
    public void noTypeDeclarationIsAnError() {
        try {
            parser.parseSource("package com.example;\n\n// 只有包声明和注释\n");
            fail("应当抛异常");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("No class"));
        }
    }

    @Test
    public void typeDeclarationWithoutNameIsRejected() {
        try {
            parser.parseSource("package com.example;\n\npublic class {\n}\n");
            fail("应当抛异常");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("without a name"));
        }
    }

    @Test
    public void enumWithoutExplicitModifiersStillParses() {
        ClassInfo info = parser.parseSource(
                "package com.example;\n\nenum Plain {\n    A, B;\n\n    boolean usable() {\n        return true;\n"
                        + "    }\n}\n");

        assertFalse(info.getInterfaceType());
        assertEquals("Plain", info.getSimpleClassName());
        assertEquals(2, info.getFields().size());
        assertEquals(1, info.getMethods().size());
        assertEquals("return true;", info.getMethods().get(0).getStatements().get(0));
    }
}
