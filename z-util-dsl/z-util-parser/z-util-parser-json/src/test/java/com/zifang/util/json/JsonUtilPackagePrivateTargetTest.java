package com.zifang.util.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 目标是包私有类时也能反序列化。
 *
 * <p>从 z-lc 的 {@code ZLcDeepCopyUtilTest} 迁出来的：它的被拷贝类是测试里的
 * package-private 静态嵌套类，而 JsonUtil 本身在 {@code com.zifang.util.json} 包里。
 * 原来直接 {@code getDeclaredConstructor().newInstance()}，跨包访问包私有类被拒，
 * 抛 IllegalAccessException。Jackson 走 forceAccess，不会踩这条。</p>
 */
public class JsonUtilPackagePrivateTargetTest {

    // 故意不给 public：整个类（含构造器）对 JsonUtil 所在包不可见
    static class PackagePrivateTarget {
        private String name;
        private int count;

        public String getName() { return name; }
        public void setName(String n) { this.name = n; }
        public int getCount() { return count; }
        public void setCount(int c) { this.count = c; }
    }

    private static class PrivateNested {
        private String v;
    }

    @Test
    public void packagePrivateClassCanBeDeserialized() {
        PackagePrivateTarget back =
                JsonUtil.fromJson("{\"name\":\"x\",\"count\":7}", PackagePrivateTarget.class);
        assertEquals("x", back.getName());
        assertEquals(7, back.getCount());
    }

    @Test
    public void privateNestedClassCanBeDeserialized() {
        PrivateNested back = JsonUtil.fromJson("{\"v\":\"y\"}", PrivateNested.class);
        assertEquals("y", back.v);
    }

    @Test
    public void packagePrivateClassCanBeSerialized() {
        PackagePrivateTarget src = new PackagePrivateTarget();
        src.setName("x");
        src.setCount(7);
        String json = JsonUtil.toJson(src);
        assertEquals(true, json.contains("\"name\":\"x\""), json);
    }

    @Test
    public void packagePrivateTargetNestedInsideAnotherPojo() {
        String json = "{\"child\":{\"name\":\"deep\",\"count\":1}}";
        Outer back = JsonUtil.fromJson(json, Outer.class);
        assertEquals("deep", back.child.getName());
        assertEquals(1, back.child.getCount());
    }

    public static class Outer {
        private PackagePrivateTarget child;
    }
}
