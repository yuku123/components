package com.zifang.util.bc.source.parser;

import com.zifang.util.bc.source.generator.info.AnnotationInfo;
import com.zifang.util.bc.source.generator.info.ClassInfo;
import com.zifang.util.bc.source.generator.info.FieldInfo;
import com.zifang.util.bc.source.generator.info.MethodInfo;
import com.zifang.util.bc.source.generator.info.MethodParameterPair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zifang.util.bc.source.parser.JavaLexer.EOF;
import static com.zifang.util.bc.source.parser.JavaLexer.IDENTIFIER;

/**
 * 源代码解析器
 * <p>
 * 自研的声明级解析器：{@link JavaLexer} 出记号，本类在记号流上走一遍，把类、
 * 接口、枚举、注解的声明结构抽成 {@link ClassInfo}。支持从文件、InputStream
 * 或字符串解析。
 * <p>
 * 只做声明级就够了。方法体不做语法分析，按顶层分号与花括号配平切成语句、
 * 原样存文本 —— {@code ClassInfo} 里的语句本来就是待重新生成回源码的片段，
 * 表达式级的语义（求值顺序、类型推导）对这个模型毫无用处。要做到那一层就得
 * 写一个编译器前端，是完全另一个量级的东西。
 *
 * @author zifang
 */
public class SourceCodeParser {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final Map<String, Integer> MODIFIERS = new HashMap<>();

    static {
        MODIFIERS.put("public", Modifier.PUBLIC);
        MODIFIERS.put("private", Modifier.PRIVATE);
        MODIFIERS.put("protected", Modifier.PROTECTED);
        MODIFIERS.put("static", Modifier.STATIC);
        MODIFIERS.put("final", Modifier.FINAL);
        MODIFIERS.put("abstract", Modifier.ABSTRACT);
        MODIFIERS.put("synchronized", Modifier.SYNCHRONIZED);
        MODIFIERS.put("volatile", Modifier.VOLATILE);
        MODIFIERS.put("transient", Modifier.TRANSIENT);
        MODIFIERS.put("native", Modifier.NATIVE);
        MODIFIERS.put("strictfp", Modifier.STRICT);
    }

    private static final Set<String> MODIFIER_KEYWORDS = new HashSet<>(Arrays.asList(
            "public", "private", "protected", "static", "abstract", "final", "synchronized",
            "volatile", "transient", "native", "strictfp"));

    private List<JavaLexer.JavaToken> tokens = JavaLexer.empty();
    private int cursor;

    /**
     * 从文件路径解析 Java 源码
     */
    public ClassInfo parse(String filePath) throws IOException {
        return parse(new File(filePath));
    }

    /**
     * 从 File 对象解析 Java 源码
     */
    public ClassInfo parse(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parse(fis);
        }
    }

    /**
     * 从 InputStream 解析 Java 源码
     */
    public ClassInfo parse(InputStream is) {
        return parseSource(readAll(is));
    }

    /**
     * 从字符串解析 Java 源码
     */
    public ClassInfo parseSource(String sourceCode) {
        if (sourceCode == null) {
            throw new RuntimeException("Failed to parse Java source");
        }
        tokens = JavaLexer.tokenize(sourceCode);
        cursor = 0;
        return convertToClassInfo();
    }

    private static String readAll(InputStream is) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192];
            int n;
            while ((n = is.read(chunk)) > 0) {
                out.write(chunk, 0, n);
            }
            return new String(out.toByteArray(), UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Java source", e);
        }
    }

    // ---------------------------------------------------------------- 编译单元

    /**
     * 扫编译单元：包名、导入、第一个类型声明
     */
    private ClassInfo convertToClassInfo() {
        String packageName = "";
        List<String> imports = new ArrayList<>();
        ClassInfo first = null;

        while (!atEnd()) {
            if (is("package")) {
                next();
                packageName = joinUntilAny(";").replace(" ", "");
            } else if (is("import")) {
                next();
                imports.add(stripModifiers(joinUntilAny(";")));
            } else if (startsTypeDeclaration(cursor)) {
                int end = typeDeclarationEnd(cursor);
                if (first == null) {
                    first = parseTypeDeclaration(cursor, end);
                }
                cursor = end;
            } else {
                next();
            }
        }

        if (first == null) {
            throw new RuntimeException("No class, interface, enum or annotation declaration found");
        }
        first.setPackageName(packageName);
        first.setImports(imports);
        return first;
    }

    // ---------------------------------------------------------------- 类型声明

    /**
     * 定位类型声明的结束下标（不含）：跳过类型头后跳过整段花括号
     */
    private int typeDeclarationEnd(int from) {
        int i = skipDecorators(from);
        i = is(i, "@") && isKeyword(i + 1, "interface") ? i + 2 : i + 1;
        if (at(i).is(IDENTIFIER)) {
            i++;
        }
        i = skipBalanced(i, "<", ">");
        while (!atEndAt(i) && !is(i, "{") && !is(i, ";")) {
            i++;
        }
        return is(i, "{") ? skipBalanced(i, "{", "}") : i + 1;
    }

    /**
     * 解析一个类型声明，外层类型与内部类走同一条路径
     *
     * @param from 声明起点
     * @param to   声明结束下标（不含）
     * @return ClassInfo类型返回值
     */
    private ClassInfo parseTypeDeclaration(int from, int to) {
        ClassInfo classInfo = new ClassInfo();
        cursor = from;

        int modifiers = 0;
        List<AnnotationInfo> annotations = new ArrayList<>();
        while (true) {
            if (is("@") && at(cursor + 1).is(IDENTIFIER)) {
                annotations.add(parseAnnotation());
            } else if (MODIFIER_KEYWORDS.contains(text())) {
                modifiers |= MODIFIERS.get(text());
                next();
            } else {
                break;
            }
        }

        boolean annotationDecl = is("@") && isKeyword(cursor + 1, "interface");
        boolean interfaceType = !annotationDecl && text().equals("interface");
        boolean enumDecl = !annotationDecl && text().equals("enum");
        cursor += annotationDecl ? 2 : 1;

        if (!at(cursor).is(IDENTIFIER)) {
            throw new RuntimeException("Type declaration without a name near: " + text());
        }
        classInfo.setSimpleClassName(next().getText());
        classInfo.setModifiers(modifiers);
        classInfo.setAnnotations(annotations);
        classInfo.setInterfaceType(interfaceType);
        cursor = skipBalanced(cursor, "<", ">");

        if (is("extends") && !interfaceType) {
            next();
            ClassInfo superInfo = namedType(joinUntilAny(",", "{", ";", "implements"));
            if (superInfo != null) {
                classInfo.setSuperClass(superInfo);
            }
        }

        List<ClassInfo> interfaces = new ArrayList<>();
        if (is("implements") || is("extends")) {
            next();
            while (!atEnd() && !is("{") && !is(";")) {
                if (is(",")) {
                    next();
                    continue;
                }
                ClassInfo iface = namedType(joinUntilAny(",", "{", ";"));
                if (iface != null) {
                    interfaces.add(iface);
                }
            }
        }
        classInfo.setInterfaces(interfaces);

        List<FieldInfo> fields = new ArrayList<>();
        List<MethodInfo> methods = new ArrayList<>();
        List<ClassInfo> innerClasses = new ArrayList<>();

        if (is("{")) {
            next();
            boolean enumConstantsDone = !enumDecl;
            while (!atEnd() && !is("}")) {
                if (!enumConstantsDone) {
                    if (is(";")) {
                        next();
                        enumConstantsDone = true;
                    } else if (!is(",")) {
                        fields.add(parseEnumConstant(classInfo.getSimpleClassName()));
                    } else {
                        next();
                    }
                    continue;
                }
                if (startsTypeDeclaration(cursor)) {
                    int end = typeDeclarationEnd(cursor);
                    innerClasses.add(parseTypeDeclaration(cursor, end));
                    cursor = end;
                    continue;
                }
                parseMember(classInfo.getSimpleClassName(), annotationDecl, fields, methods);
            }
        }

        classInfo.setFields(fields);
        classInfo.setMethods(methods);
        classInfo.setInnerClasses(innerClasses);

        cursor = to;
        return classInfo;
    }

    // ---------------------------------------------------------------- 成员声明

    /**
     * 解析一个字段或方法声明。构造器与初始化块不产出模型对象
     *
     * @param typeName       宿主类型名，用来识别构造器
     * @param annotationDecl 宿主是否为注解类型
     * @param fields         字段收集袋
     * @param methods        方法收集袋
     */
    private void parseMember(String typeName, boolean annotationDecl,
                             List<FieldInfo> fields, List<MethodInfo> methods) {
        int modifiers = 0;
        List<AnnotationInfo> annotations = new ArrayList<>();
        while (true) {
            if (is("@") && at(cursor + 1).is(IDENTIFIER)) {
                annotations.add(parseAnnotation());
            } else if (MODIFIER_KEYWORDS.contains(text())) {
                modifiers |= MODIFIERS.get(text());
                next();
            } else {
                break;
            }
        }

        if (is(";") || is("{")) {
            skipInitializerOrEmptyBody();
            return;
        }
        if (is("<")) {
            cursor = skipBalanced(cursor, "<", ">");
        }

        int typeStart = cursor;
        int nameIndex = findDeclaredName();
        if (nameIndex < 0) {
            next();
            return;
        }

        String name = tokens.get(nameIndex).getText();
        String type = JavaLexer.join(tokens, typeStart, nameIndex);
        cursor = nameIndex + 1;

        if (!is("(")) {
            fields.add(parseFieldTail(name, type, modifiers, annotations));
        } else if (!name.equals(typeName)) {
            methods.add(parseMethodTail(name, type, modifiers, annotations, annotationDecl));
        } else {
            skipMethodRest();
        }
    }

    /**
     * 找到成员名：紧跟 {@code (} 的标识符是方法名，否则是 {@code = ; ,} 之前
     * 最后一个标识符。一行 {@code int a = 1, b = 2;} 只取第一个声明符，与旧行为一致。
     *
     * @return int类型返回值，找不到时返回 -1
     */
    private int findDeclaredName() {
        int depth = 0;
        int lastIdentifier = -1;
        for (int i = cursor; !atEndAt(i); i++) {
            String text = tokens.get(i).getText();
            if (text.equals("{") || text.equals("(") || text.equals("[") || text.equals("<")) {
                depth++;
            } else if (text.startsWith(">")) {
                // >> 与 >>> 是合并记号，一个记号要收掉多层泛型
                depth -= text.length();
            } else if (text.equals("}") || text.equals(")") || text.equals("]")) {
                depth--;
            } else if (depth <= 0) {
                if (text.equals("=") || text.equals(";") || text.equals(",")) {
                    return lastIdentifier;
                }
                if (tokens.get(i).is(IDENTIFIER) && is(i + 1, "(")) {
                    return i;
                }
            }
            if (tokens.get(i).is(IDENTIFIER)) {
                lastIdentifier = i;
            }
        }
        return lastIdentifier;
    }

    private FieldInfo parseFieldTail(String name, String type, int modifiers,
                                     List<AnnotationInfo> annotations) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setType(type);
        fieldInfo.setValue(name);
        fieldInfo.setModifiers(new int[]{modifiers});
        fieldInfo.setAnnotations(annotations);

        String initializer = "null";
        if (is("=")) {
            next();
            int from = cursor;
            skipToInitializerEnd();
            initializer = JavaLexer.join(tokens, from, cursor);
            skipToDeclarationEnd();
        } else {
            skipToDeclarationEnd();
        }
        fieldInfo.setInitializer(initializer);
        return fieldInfo;
    }

    /** 初始值止于顶层的 {@code ,} 或 {@code ;}，一行多声明符时后续声明符归 skipToDeclarationEnd */
    private void skipToInitializerEnd() {
        int depth = 0;
        while (!atEnd()) {
            String text = text();
            if (text.equals("{") || text.equals("(") || text.equals("[")) {
                depth++;
            } else if (text.equals("}") || text.equals(")") || text.equals("]")) {
                if (depth == 0) {
                    return;
                }
                depth--;
            } else if (depth == 0 && (text.equals(",") || text.equals(";"))) {
                return;
            }
            next();
        }
    }

    private MethodInfo parseMethodTail(String name, String type, int modifiers,
                                       List<AnnotationInfo> annotations, boolean annotationDecl) {
        List<MethodParameterPair> params = parseParameters();

        if (is("throws")) {
            while (!atEnd() && !is("{") && !is(";")) {
                next();
            }
        }

        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setMethodName(name);
        methodInfo.setReturnType(type);
        methodInfo.setMethodParameterPairs(params);
        methodInfo.setAnnotations(annotations);

        if (annotationDecl) {
            // 注解成员恒为 public abstract，默认值按语句形式记下来
            methodInfo.setModifier(Modifier.PUBLIC | Modifier.ABSTRACT);
            List<String> statements = new ArrayList<>();
            if (is("default")) {
                next();
                int from = cursor;
                skipToDeclarationEnd();
                statements.add("default " + JavaLexer.join(tokens, from, cursor - 1));
            } else if (is(";")) {
                next();
            }
            methodInfo.setStatements(statements);
        } else {
            methodInfo.setModifier(modifiers);
            methodInfo.setStatements(is("{") ? parseBlockStatements() : emptyStatements());
        }
        return methodInfo;
    }

    /** 吃掉构造器剩下的一切：参数表、throws 与方法体 */
    private void skipMethodRest() {
        if (is("(")) {
            cursor = skipBalanced(cursor, "(", ")");
        }
        while (!atEnd() && !is("{") && !is(";")) {
            next();
        }
        if (is("{")) {
            cursor = skipBalanced(cursor, "{", "}");
        } else if (is(";")) {
            next();
        }
    }

    private void skipInitializerOrEmptyBody() {
        if (is("{")) {
            cursor = skipBalanced(cursor, "{", "}");
        } else if (is(";")) {
            next();
        }
    }

    private List<String> emptyStatements() {
        if (is(";")) {
            next();
        }
        return new ArrayList<>();
    }

    // ---------------------------------------------------------------- 方法体与参数

    /**
     * 解析方法体，按顶层分号与花括号配平切成语句
     *
     * @return List&lt;String&gt;类型返回值
     */
    private List<String> parseBlockStatements() {
        next();
        List<String> statements = new ArrayList<>();
        List<Boolean> braceKinds = new ArrayList<>();
        int statementStart = cursor;
        int depth = 0;

        while (!atEnd()) {
            String text = text();
            if (text.equals("{") || text.equals("(") || text.equals("[")) {
                // 紧跟 ] 的花括号是数组初始化 {1, 2}，不是语句块
                if (text.equals("{")) {
                    braceKinds.add(!is(cursor - 1, "]"));
                }
                depth++;
            } else if (text.equals("}") || text.equals(")") || text.equals("]")) {
                if (depth == 0) {
                    if (statementStart < cursor) {
                        addStatement(statements, statementStart, cursor + 1);
                    }
                    next();
                    return statements;
                }
                boolean statementBlock = text.equals("}") && braceKinds.remove(braceKinds.size() - 1);
                depth--;
                // 顶层的 if / for / while 语句以配平的右花括号收尾，没有分号；
                // 后头跟着 else / catch / finally / while 时属于同一条语句，不在此处断开
                if (depth == 0 && statementBlock && !continuesStatement(cursor + 1)) {
                    addStatement(statements, statementStart, cursor + 1);
                    statementStart = cursor + 1;
                }
            } else if (depth == 0 && text.equals(";")) {
                // 上一条语句已由 } 收尾时，这个分号是多余的，不单独成句
                if (statementStart < cursor) {
                    addStatement(statements, statementStart, cursor + 1);
                }
                statementStart = cursor + 1;
            }
            next();
        }
        if (statementStart < cursor) {
            addStatement(statements, statementStart, cursor);
        }
        return statements;
    }

    private void addStatement(List<String> statements, int from, int to) {
        if (to > from) {
            statements.add(JavaLexer.join(tokens, from, to));
        }
    }

    /** try-catch 与 do-while 的后半截属于同一条语句，方法体收尾的 } 之后不会跟这些记号 */
    private boolean continuesStatement(int index) {
        String text = at(index).getText();
        return text.equals("else") || text.equals("catch") || text.equals("finally") || text.equals("while");
    }

    private List<MethodParameterPair> parseParameters() {
        List<MethodParameterPair> params = new ArrayList<>();
        next();
        int paramStart = cursor;
        int depth = 0;

        while (!atEnd()) {
            String text = text();
            if (text.equals("(") || text.equals("[") || text.equals("{")) {
                depth++;
            } else if (text.equals(")") || text.equals("]") || text.equals("}")) {
                if (depth == 0) {
                    addParameter(params, paramStart, cursor);
                    next();
                    return params;
                }
                depth--;
            } else if (depth == 0 && text.equals(",")) {
                addParameter(params, paramStart, cursor);
                paramStart = cursor + 1;
            }
            next();
        }
        return params;
    }

    /** 参数形如 {@code final @Deprecated String name}：修饰符与注解不算类型的一部分 */
    private void addParameter(List<MethodParameterPair> params, int from, int to) {
        int nameIndex = to - 1;
        while (nameIndex >= from && !tokens.get(nameIndex).is(IDENTIFIER)) {
            nameIndex--;
        }
        int typeStart = from;
        while (typeStart < nameIndex) {
            if (MODIFIER_KEYWORDS.contains(tokens.get(typeStart).getText())) {
                typeStart++;
            } else if (tokens.get(typeStart).is("@")) {
                typeStart++;
                while (typeStart < to && (tokens.get(typeStart).is(IDENTIFIER) || is(typeStart, "."))) {
                    typeStart++;
                }
                if (is(typeStart, "(")) {
                    typeStart = skipBalanced(typeStart, "(", ")");
                }
            } else {
                break;
            }
        }
        if (nameIndex < typeStart) {
            return;
        }
        MethodParameterPair pair = new MethodParameterPair();
        pair.setParamType(JavaLexer.join(tokens, typeStart, nameIndex));
        pair.setParamName(tokens.get(nameIndex).getText());
        params.add(pair);
    }

    // ---------------------------------------------------------------- 枚举与注解

    /**
     * 解析一个枚举常量，记成 {@code {PUBLIC|STATIC|FINAL}} 的字段
     */
    private FieldInfo parseEnumConstant(String enumName) {
        while (is("@") && at(cursor + 1).is(IDENTIFIER)) {
            parseAnnotation();
        }
        String name = next().getText();
        if (is("(")) {
            cursor = skipBalanced(cursor, "(", ")");
        }
        if (is("{")) {
            cursor = skipBalanced(cursor, "{", "}");
        }

        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setType(enumName);
        fieldInfo.setValue(name);
        fieldInfo.setModifiers(new int[]{Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL});
        fieldInfo.setInitializer("null");
        return fieldInfo;
    }

    private AnnotationInfo parseAnnotation() {
        next();
        int nameStart = cursor;
        while (!atEnd() && (at(cursor).is(IDENTIFIER) || is("."))) {
            next();
        }

        AnnotationInfo info = new AnnotationInfo();
        info.setType(JavaLexer.join(tokens, nameStart, cursor));

        if (is("(")) {
            int open = cursor;
            cursor = skipBalanced(open, "(", ")");
            parseAnnotationArguments(info, open, cursor - 1);
        }
        return info;
    }

    /** 逗号分隔的 {@code name = value} 或单值；嵌套的花括号数组值靠配平跳过逗号 */
    private void parseAnnotationArguments(AnnotationInfo info, int open, int close) {
        int depth = 0;
        int argStart = open + 1;
        for (int i = open + 1; i <= close; i++) {
            String text = tokens.get(i).getText();
            if (text.equals("{") || text.equals("(") || text.equals("[")) {
                depth++;
            } else if (text.equals("}") || text.equals(")") || text.equals("]")) {
                depth--;
            } else if (depth == 0 && text.equals(",")) {
                addAnnotationArgument(info, argStart, i);
                argStart = i + 1;
            }
        }
        addAnnotationArgument(info, argStart, close);
    }

    private void addAnnotationArgument(AnnotationInfo info, int from, int to) {
        if (tokens.get(from).is(IDENTIFIER) && is(from + 1, "=")) {
            info.addMember(tokens.get(from).getText(), JavaLexer.join(tokens, from + 2, to));
        } else if (to > from) {
            info.addMember("", JavaLexer.join(tokens, from, to));
        }
    }

    // ---------------------------------------------------------------- 记号流工具

    private boolean startsTypeDeclaration(int from) {
        int i = skipDecorators(from);
        if (is(i, "@") && isKeyword(i + 1, "interface")) {
            return true;
        }
        String text = at(i).getText();
        return text.equals("class") || text.equals("interface") || text.equals("enum");
    }

    /** 跳过注解与修饰符，返回下一个记号下标 */
    private int skipDecorators(int from) {
        int i = from;
        while (!atEndAt(i)) {
            if (is(i, "@") && at(i + 1).is(IDENTIFIER)) {
                i++;
                while (!atEndAt(i) && (at(i).is(IDENTIFIER) || is(i, "."))) {
                    i++;
                }
                if (is(i, "(")) {
                    i = skipBalanced(i, "(", ")");
                }
            } else if (MODIFIER_KEYWORDS.contains(at(i).getText())) {
                i++;
            } else {
                break;
            }
        }
        return i;
    }

    /** 吃掉到声明末尾（{@code ;}）为止，逗号之后的续声明符一并吃掉 */
    private void skipToDeclarationEnd() {
        int depth = 0;
        while (!atEnd()) {
            String text = text();
            if (text.equals("{") || text.equals("(") || text.equals("[")) {
                depth++;
            } else if (text.equals("}") || text.equals(")") || text.equals("]")) {
                if (depth == 0) {
                    return;
                }
                depth--;
            } else if (depth == 0 && text.equals(";")) {
                next();
                return;
            }
            next();
        }
    }

    /**
     * 从 from 处跳过成对符号，返回配对符之后的下标；from 处不是开括号则原样返回
     */
    private int skipBalanced(int from, String open, String close) {
        if (!is(from, open)) {
            return from;
        }
        int i = from + 1;
        int depth = 1;
        while (!atEndAt(i) && depth > 0) {
            if (is(i, open)) {
                depth++;
            } else if (is(i, close)) {
                depth--;
            }
            i++;
        }
        return i;
    }

    /** 吃掉到任一终止记号为止（终止符不消耗），返回中间记号的规范化文本 */
    private String joinUntilAny(String... stops) {
        Set<String> stopSet = new HashSet<>(Arrays.asList(stops));
        int from = cursor;
        while (!atEnd() && !stopSet.contains(text())) {
            next();
        }
        return JavaLexer.join(tokens, from, cursor);
    }

    private static ClassInfo namedType(String text) {
        String name = simpleName(text);
        if (name.isEmpty()) {
            return null;
        }
        ClassInfo info = new ClassInfo();
        info.setSimpleClassName(name);
        info.setPackageName("");
        return info;
    }

    private static String stripModifiers(String importName) {
        int space = importName.lastIndexOf(' ');
        return space < 0 ? importName : importName.substring(space + 1);
    }

    /** 取 {@code java.util.List} 这样的名字里的简单名，泛型实参一并丢掉 */
    private static String simpleName(String qualified) {
        String text = qualified;
        int generic = text.indexOf('<');
        if (generic >= 0) {
            text = text.substring(0, generic);
        }
        int dot = text.lastIndexOf('.');
        return dot >= 0 ? text.substring(dot + 1) : text;
    }

    private JavaLexer.JavaToken at(int index) {
        return tokens.get(Math.max(0, Math.min(index, tokens.size() - 1)));
    }

    private boolean is(int index, String text) {
        return at(index).is(text);
    }

    private boolean isKeyword(int index, String keyword) {
        return at(index).isKeyword(keyword);
    }

    private boolean is(String text) {
        return text().equals(text);
    }

    private String text() {
        return at(cursor).getText();
    }

    private JavaLexer.JavaToken next() {
        JavaLexer.JavaToken token = at(cursor);
        if (cursor < tokens.size() - 1) {
            cursor++;
        }
        return token;
    }

    private boolean atEnd() {
        return at(cursor).is(EOF);
    }

    private boolean atEndAt(int index) {
        return at(index).is(EOF);
    }
}
