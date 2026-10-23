package com.zifang.util.devops.nexus;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 凭据泄漏守卫测试（2026-10-24 · 卡 TASK-20261024-021）。
 * <p>
 * <b>为什么需要它</b>：本模块曾把第三方 Nexus 的地址 / 账号 / 口令写成源码常量，
 * 并随 jar 烤进 Maven 中央（中央件不可撤回）⇒ 必须在**仓内**留一道常驻闸门：
 * 源码与编译产物里一旦再出现那枚口令，本测试立刻变红。
 *
 * <p><b>明文零落盘</b>：本测试**只存指纹**（sha256 前 8 位 + 长度），
 * **不存口令明文** ⇒ 判据是「枚举候选字面量 → 逐个算 sha256 → 与指纹比对」。
 * 明文要真出现在文件里，也只能是别人重新写回去的那一刻。
 *
 * <p><b>阴性对照（防「恒绿」）</b>：本测试带三组阴性对照 ——
 * ① 往临时源码树里注入一个合成值 ⇒ **同一个源码扫描函数**必须命中；
 * ② 造一个含合成值的合成 class ⇒ **同一个常量池扫描函数**必须命中；
 * ③ 合成值与目标指纹**不同**、但长度相同 ⇒ 说明扫描器是按「指纹 + 长度」双条件判的。
 * ⇒ 三组对照同时成立，上面那些「命中数 = 0」才是**量具读数**，而不是**量具没开机**。
 */
public class NexusCredentialGuardTest {

    /**
     * 被清除那枚口令的指纹：sha256 前 8 位。
     * <p>⚠ **只有指纹，没有明文** —— 明文绝不落盘。
     */
    private static final String TARGET_SHA8 = "7676aaaf";
    /**
     * 被清除那枚口令的长度（字节）。
     */
    private static final int TARGET_LENGTH = 9;

    /**
     * 阴性对照用的合成值（**不是**真口令，纯粹用来证明扫描器有判别力）。
     */
    private static final String SYNTHETIC = "synth9xY!";

    /**
     * Java 源码里的字符串字面量（够用即可：转义按常见几种还原）。
     */
    private static final Pattern STRING_LITERAL = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");

    // ------------------------------------------------------------------ 判据一：源码零命中

    @Test
    /**
     * testSourceTreeHasZeroFingerprintHits方法。
     */
    public void testSourceTreeHasZeroFingerprintHits() {
        List<String> hits = scanSourceTree(repoRoot(), TARGET_SHA8, TARGET_LENGTH);
        // 只报「命中数 + 指纹」，不打印明文
        assertEquals(0, hits.size(),
                "源码里出现目标指纹的字符串字面量 " + hits.size() + " 处（sha256_8=" + TARGET_SHA8
                        + ", length=" + TARGET_LENGTH + "）：" + hits);
    }

    // ------------------------------------------------------------------ 判据二：产物零命中

    @Test
    /**
     * testCompiledArtifactHasZeroFingerprintHits方法。
     */
    public void testCompiledArtifactHasZeroFingerprintHits() {
        Path classesDir = moduleBaseDir().resolve("target").resolve("classes");
        assertTrue(Files.isDirectory(classesDir),
                "编译产物目录不存在 ⇒ 无法验证产物（fail-closed，不沉默放行）：" + classesDir);

        List<Path> classFiles = listClassFiles(classesDir);
        assertFalse(classFiles.isEmpty(), "产物里没有任何 class ⇒ 量具没开机（fail-closed）：" + classesDir);

        List<String> hits = scanClassFiles(classFiles, TARGET_SHA8, TARGET_LENGTH);
        assertEquals(0, hits.size(),
                "编译产物里出现目标指纹的常量 " + hits.size() + " 处（sha256_8=" + TARGET_SHA8
                        + ", length=" + TARGET_LENGTH + "）：" + hits);
    }

    // ------------------------------------------------------------------ 阴性对照

    @Test
    /**
     * testNegativeControlInjectedSourceValueIsDetected方法。
     */
    public void testNegativeControlInjectedSourceValueIsDetected() throws IOException {
        Path tmpDir = Files.createTempDirectory("nexus-guard-neg-src-");
        try {
            Path victim = tmpDir.resolve("Injected.java");
            String injected = "private static final String SECRET = \"" + SYNTHETIC + "\";";
            Files.write(victim, injected.getBytes(StandardCharsets.UTF_8));

            // 用**同一个**源码扫描函数（只换指纹）⇒ 命中数必须 > 0
            List<String> hits = scanSourceTree(tmpDir, sha8(SYNTHETIC), SYNTHETIC.length());
            assertEquals(1, hits.size(),
                    "注入合成值后源码扫描仍报 0 命中 ⇒ 扫描器恒绿，无判别力");
        } finally {
            deleteRecursively(tmpDir);
        }
    }

    @Test
    /**
     * testNegativeControlInjectedClassConstantIsDetected方法。
     */
    public void testNegativeControlInjectedClassConstantIsDetected() {
        byte[] syntheticClass = syntheticClassWithConstant(SYNTHETIC);
        List<String> constants = classUtf8Constants(syntheticClass);

        assertTrue(constants.contains(SYNTHETIC),
                "合成 class 的常量池没读出合成值 ⇒ 常量池扫描器坏了（fail-closed）：" + constants);

        // 用**同一个**常量池扫描函数（只换指纹）⇒ 命中数必须 > 0
        Path tmpDir;
        try {
            tmpDir = Files.createTempDirectory("nexus-guard-neg-cls-");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        try {
            Path fakeClass = tmpDir.resolve("Synthetic.class");
            Files.write(fakeClass, syntheticClass);
            List<String> hits = scanClassFiles(java.util.Collections.singletonList(fakeClass),
                    sha8(SYNTHETIC), SYNTHETIC.length());
            assertEquals(1, hits.size(), "合成 class 里的合成值没被命中 ⇒ 常量池扫描器恒绿");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            deleteRecursively(tmpDir);
        }
    }

    @Test
    /**
     * testNegativeControlFingerprintIsSpecific方法。
     */
    public void testNegativeControlFingerprintIsSpecific() {
        // ① 合成值与目标指纹不同（否则上面的「0 命中」就分不清是指纹不匹配还是扫描器没开机）
        assertNotEquals(TARGET_SHA8, sha8(SYNTHETIC), "合成值与目标同指纹 ⇒ 对照组失去意义");
        // ② 长度相同 ⇒ 证明扫描器是按「指纹 + 长度」双条件判的，不是只比长度
        assertEquals(TARGET_LENGTH, SYNTHETIC.length());
        // ③ 目标指纹不会被任意字符串命中
        assertFalse(matchesFingerprint(SYNTHETIC, TARGET_SHA8, TARGET_LENGTH),
                "目标指纹命中了无关字符串 ⇒ 指纹判据过宽");
    }

    // ------------------------------------------------------------------ 扫描函数（正例与阴性对照共用）

    /**
     * 扫描一棵源码树里所有 .java 的字符串字面量，按「指纹 + 长度」判命中。
     *
     * @param root        源码根目录
     * @param sha8Prefix  目标 sha256 前 8 位
     * @param length      目标长度
     * @return 命中清单（路径 + 指纹 + 长度，**不含明文**）
     */
    static List<String> scanSourceTree(Path root, String sha8Prefix, int length) {
        List<String> hits = new ArrayList<>();
        for (Path javaFile : listJavaFiles(root)) {
            String text = readText(javaFile);
            for (String literal : javaStringLiterals(text)) {
                if (matchesFingerprint(literal, sha8Prefix, length)) {
                    hits.add(javaFile + " (sha256_8=" + sha8(literal) + ", length=" + literal.length() + ")");
                }
            }
        }
        return hits;
    }

    /**
     * 扫描一批 class 文件的常量池，按「指纹 + 长度」判命中。
     *
     * @param classFiles  class 文件清单
     * @param sha8Prefix  目标 sha256 前 8 位
     * @param length      目标长度
     * @return 命中清单（路径 + 指纹 + 长度，**不含明文**）
     */
    static List<String> scanClassFiles(List<Path> classFiles, String sha8Prefix, int length) {
        List<String> hits = new ArrayList<>();
        for (Path classFile : classFiles) {
            for (String constant : classUtf8Constants(readBytes(classFile))) {
                if (matchesFingerprint(constant, sha8Prefix, length)) {
                    hits.add(classFile + " (sha256_8=" + sha8(constant) + ", length=" + constant.length() + ")");
                }
            }
        }
        return hits;
    }

    // ------------------------------------------------------------------ 指纹判据

    /**
     * 「sha256 前 8 位 + 长度」双条件判据。
     */
    private static boolean matchesFingerprint(String candidate, String sha8Prefix, int length) {
        if (candidate == null || candidate.length() != length) {
            return false;
        }
        return sha8Prefix.equals(sha8(candidate));
    }

    /**
     * 字符串的 sha256 前 8 位（十六进制）。
     */
    private static String sha8(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 4; i++) {
                sb.append(String.format("%02x", hash[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 不可用", e);
        }
    }

    // ------------------------------------------------------------------ 源码扫描

    /**
     * 列出目录下的全部 .java 源码（排除构建产物与前端依赖目录）。
     */
    private static List<Path> listJavaFiles(Path root) {
        try (Stream<Path> stream = Files.walk(root)) {
            List<Path> out = new ArrayList<>();
            stream.filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> !p.toString().contains("/target/"))
                    .filter(p -> !p.toString().contains("/node_modules/"))
                    .filter(p -> !p.toString().contains("/.git/"))
                    .forEach(out::add);
            return out;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 抽出源码里的字符串字面量（转义还原；还原不出来就保留原文，两份都当候选）。
     */
    private static List<String> javaStringLiterals(String text) {
        List<String> out = new ArrayList<>();
        Matcher matcher = STRING_LITERAL.matcher(text);
        while (matcher.find()) {
            String raw = matcher.group(1);
            out.add(raw);
            String unescaped = unescape(raw);
            if (!unescaped.equals(raw)) {
                out.add(unescaped);
            }
        }
        return out;
    }

    /**
     * 常见 Java 转义还原。
     */
    private static String unescape(String raw) {
        StringBuilder sb = new StringBuilder(raw.length());
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c != '\\' || i + 1 >= raw.length()) {
                sb.append(c);
                continue;
            }
            char next = raw.charAt(++i);
            switch (next) {
                case 'n': sb.append('\n'); break;
                case 'r': sb.append('\r'); break;
                case 't': sb.append('\t'); break;
                case 'b': sb.append('\b'); break;
                case 'f': sb.append('\f'); break;
                case '0': sb.append('\0'); break;
                case 'u':
                    if (i + 4 < raw.length()) {
                        try {
                            sb.append((char) Integer.parseInt(raw.substring(i + 1, i + 5), 16));
                            i += 4;
                        } catch (NumberFormatException ignored) {
                            sb.append(next);
                        }
                    } else {
                        sb.append(next);
                    }
                    break;
                default: sb.append(next);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------ class 常量池扫描

    /**
     * 解析 class 文件的常量池，取出全部 CONSTANT_Utf8 条目。
     * <p>
     * 只依赖 class 文件格式本身（magic/版本/常量池）⇒ 不依赖任何字节码库。
     */
    static List<String> classUtf8Constants(byte[] classBytes) {
        List<String> out = new ArrayList<>();
        if (classBytes == null || classBytes.length < 10) {
            return out;
        }
        int pos = 8; // magic(4) + minor(2) + major(2)
        int poolCount = u2(classBytes, pos);
        pos += 2;
        for (int index = 1; index < poolCount; index++) {
            if (pos >= classBytes.length) {
                return out;
            }
            int tag = classBytes[pos++] & 0xff;
            switch (tag) {
                case 1: { // CONSTANT_Utf8
                    if (pos + 2 > classBytes.length) {
                        return out;
                    }
                    int len = u2(classBytes, pos);
                    pos += 2;
                    if (len < 0 || pos + len > classBytes.length) {
                        return out;
                    }
                    out.add(new String(classBytes, pos, len, StandardCharsets.UTF_8));
                    pos += len;
                    break;
                }
                case 7: case 8: case 16: case 19: case 20: // Class/String/MethodType/Module/Package
                    pos += 2;
                    break;
                case 15: // MethodHandle
                    pos += 3;
                    break;
                case 3: case 4: case 9: case 10: case 11: case 12: case 17: case 18: // 数值类
                    pos += 4;
                    break;
                case 5: case 6: // Long/Double 占两个常量池槽位
                    pos += 8;
                    index++;
                    break;
                default:
                    return out; // 未知 tag ⇒ 停止解析（不做臆测）
            }
        }
        return out;
    }

    /**
     * 造一个最小的 class 文件，常量池里带一个指定的 Utf8 常量（阴性对照用）。
     */
    static byte[] syntheticClassWithConstant(String value) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            out.writeInt(0xCAFEBABE);   // magic
            out.writeShort(0);           // minor
            out.writeShort(52);          // major (Java 8)
            out.writeShort(3);           // constant_pool_count（#1 Utf8 + #2 Class）
            out.writeByte(1);            // CONSTANT_Utf8
            byte[] utf8 = value.getBytes(StandardCharsets.UTF_8);
            out.writeShort(utf8.length);
            out.write(utf8);
            out.writeByte(7);            // CONSTANT_Class
            out.writeShort(1);           // name_index → #1
            out.writeShort(0x0021);      // access_flags
            out.writeShort(2);           // this_class → #2
            out.writeShort(0);           // super_class
            out.writeShort(0);           // interfaces_count
            out.writeShort(0);           // fields_count
            out.writeShort(0);           // methods_count
            out.writeShort(0);           // attributes_count
            out.flush();
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 读 class 文件的大端 u2。
     */
    private static int u2(byte[] data, int offset) {
        return ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff);
    }

    // ------------------------------------------------------------------ 路径

    /**
     * 本模块的目录（z-util-devops/z-util-devops-nexus）。
     */
    private static Path moduleBaseDir() {
        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }

    /**
     * 仓根目录（找到 artifactId 为 z-util 的那个 pom）。
     */
    private static Path repoRoot() {
        Path current = moduleBaseDir();
        while (current != null) {
            Path pom = current.resolve("pom.xml");
            if (Files.isRegularFile(pom)) {
                try {
                    String text = new String(Files.readAllBytes(pom), StandardCharsets.UTF_8);
                    if (text.contains("<artifactId>z-util</artifactId>")) {
                        return current;
                    }
                } catch (IOException ignored) {
                    // 读不到就继续往上找
                }
            }
            current = current.getParent();
        }
        throw new IllegalStateException("找不到仓根目录（未找到 artifactId=z-util 的 pom.xml）");
    }

    /**
     * 列出目录下的全部 .class 文件。
     */
    private static List<Path> listClassFiles(Path dir) {
        try (Stream<Path> stream = Files.walk(dir)) {
            List<Path> out = new ArrayList<>();
            stream.filter(p -> p.toString().endsWith(".class")).forEach(out::add);
            return out;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String readText(Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static byte[] readBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 递归删临时目录（负例对照不留残骸）。
     */
    private static void deleteRecursively(Path dir) {
        try (Stream<Path> stream = Files.walk(dir)) {
            stream.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ignored) {
                    // 尽力而为
                }
            });
        } catch (IOException ignored) {
            // 尽力而为
        }
    }
}
