package com.zifang.util.devops.nexus;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NexusComponentManager 测试
 * <p>
 * 2026-10-24 起本类的三项配置（地址 / 账号 / 口令）改为**构造器注入 + 环境变量**，
 * 缺任一项即 fail-fast；因此测试不再依赖任何外部 Nexus 实例。
 * <p>
 * 下方用的是**本地假地址**（连接必然被拒），只验证「方法可调用、返回类型正确、异常语义正确」，
 * 不对内容做断言（取决于外部服务状态，本来也不该有外部依赖）。
 */

/**
 * NexusComponentManagerTest类。
 */
public class NexusComponentManagerTest {

    /**
     * 假地址：本地回环 + 保留端口 ⇒ 连接必被拒（不依赖外网、不依赖真实 Nexus）。
     */
    private static final String FAKE_URL = "http://127.0.0.1:1";
    private static final String FAKE_USERNAME = "unit-test-user";
    private static final String FAKE_PASSWORD = "unit-test-pass";

    @Test
    /**
     * testSearchReturnsList方法。
     */
    public void testSearchReturnsList() {
        NexusComponentManager manager = new NexusComponentManager(FAKE_URL, FAKE_USERNAME, FAKE_PASSWORD);
        // 验证方法签名存在、可调用、返回非null List
        java.util.List<Component> result = manager.search("maven-releases");
        assertNotNull(result);
        // 不断言内容，因为取决于 nexus 服务状态
    }

    @Test
    /**
     * testFindByGavReturnsNullOrComponent方法。
     */
    public void testFindByGavReturnsNullOrComponent() {
        NexusComponentManager manager = new NexusComponentManager(FAKE_URL, FAKE_USERNAME, FAKE_PASSWORD);
        // 用不存在的 GAV 验证返回 null 而非抛异常
        Component result = manager.findByGav("com.nonexistent", "artifact-does-not-exist", "1.0.0");
        assertNull(result);
    }

    @Test
    /**
     * testCheckExistGavReturnsBoolean方法。
     */
    public void testCheckExistGavReturnsBoolean() {
        NexusComponentManager manager = new NexusComponentManager(FAKE_URL, FAKE_USERNAME, FAKE_PASSWORD);
        // 验证返回值是 Boolean 类型
        Boolean exists = manager.checkExistGav("com.nonexistent", "artifact-does-not-exist", "1.0.0");
        assertNotNull(exists);
        assertFalse(exists);
    }

    @Test
    /**
     * testRepositoryConstant方法。
     */
    public void testRepositoryConstant() {
        assertEquals("maven-releases", NexusComponentManager.respository);
    }

    @Test
    /**
     * testConstructorRejectsMissingConfig方法。
     */
    public void testConstructorRejectsMissingConfig() {
        // fail-fast：任一项缺失/空白 ⇒ 立即抛异常，**不留兜底默认值**
        assertThrows(IllegalArgumentException.class,
                () -> new NexusComponentManager(null, FAKE_USERNAME, FAKE_PASSWORD));
        assertThrows(IllegalArgumentException.class,
                () -> new NexusComponentManager(FAKE_URL, null, FAKE_PASSWORD));
        assertThrows(IllegalArgumentException.class,
                () -> new NexusComponentManager(FAKE_URL, FAKE_USERNAME, null));
        assertThrows(IllegalArgumentException.class,
                () -> new NexusComponentManager("  ", FAKE_USERNAME, FAKE_PASSWORD));
    }

    @Test
    /**
     * testFromEnvironmentFailsFastWhenAnyVariableMissing方法。
     */
    public void testFromEnvironmentFailsFastWhenAnyVariableMissing() {
        Map<String, String> empty = new HashMap<>();
        assertThrows(IllegalStateException.class, () -> NexusComponentManager.fromEnvironment(empty::get));

        Map<String, String> onlyUrl = new HashMap<>();
        onlyUrl.put(NexusComponentManager.ENV_NEXUS_URL, FAKE_URL);
        assertThrows(IllegalStateException.class, () -> NexusComponentManager.fromEnvironment(onlyUrl::get));

        Map<String, String> all = new HashMap<>();
        all.put(NexusComponentManager.ENV_NEXUS_URL, FAKE_URL);
        all.put(NexusComponentManager.ENV_NEXUS_USERNAME, FAKE_USERNAME);
        all.put(NexusComponentManager.ENV_NEXUS_PASSWORD, FAKE_PASSWORD);
        NexusComponentManager manager = NexusComponentManager.fromEnvironment(all::get);
        assertNotNull(manager);
        assertTrue(manager.search("maven-releases") != null);
    }

    @Test
    /**
     * testNoHardcodedCredentialConstants方法。
     */
    public void testNoHardcodedCredentialConstants() {
        // 三项配置只能来自注入/环境变量 ⇒ 类上不得再有承载凭据值的静态字段。
        // 允许的静态成员只有：环境变量「名」常量（值为大写变量名，不是凭据本身）与
        // 非凭据常量（repository 名、OkHttp 客户端）。
        java.util.Set<String> allowed = new java.util.HashSet<>(java.util.Arrays.asList(
                "ENV_NEXUS_URL", "ENV_NEXUS_USERNAME", "ENV_NEXUS_PASSWORD", "respository", "HTTP_CLIENT"));
        for (java.lang.reflect.Field field : NexusComponentManager.class.getDeclaredFields()) {
            if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            assertTrue(allowed.contains(field.getName()),
                    "不得保留静态凭据字段: " + field.getName());
        }

        // 环境变量名常量只能是「变量名」，不得夹带 URL / 凭据
        assertEquals("NEXUS_URL", NexusComponentManager.ENV_NEXUS_URL);
        assertEquals("NEXUS_USERNAME", NexusComponentManager.ENV_NEXUS_USERNAME);
        assertEquals("NEXUS_PASSWORD", NexusComponentManager.ENV_NEXUS_PASSWORD);
        for (String envName : java.util.Arrays.asList(NexusComponentManager.ENV_NEXUS_URL,
                NexusComponentManager.ENV_NEXUS_USERNAME, NexusComponentManager.ENV_NEXUS_PASSWORD)) {
            assertTrue(!envName.contains("://") && !envName.contains("@"),
                    "环境变量名常量不得夹带 URL 或凭据: " + envName);
        }

        // 凭据必须是实例字段（构造器注入 / 环境变量解析而来），不能是静态共享状态
        for (java.lang.reflect.Field field : NexusComponentManager.class.getDeclaredFields()) {
            if (field.getName().equals("nexusUrl") || field.getName().equals("nexusUsername")
                    || field.getName().equals("nexusPassword")) {
                assertTrue(!java.lang.reflect.Modifier.isStatic(field.getModifiers()),
                        "凭据字段必须是实例字段: " + field.getName());
                assertTrue(java.lang.reflect.Modifier.isFinal(field.getModifiers()),
                        "凭据字段必须是 final: " + field.getName());
            }
        }
    }

    private static void assertEquals(String expected, String actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }
}
