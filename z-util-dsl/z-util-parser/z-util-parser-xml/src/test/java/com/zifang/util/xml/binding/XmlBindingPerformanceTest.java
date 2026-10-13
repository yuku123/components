package com.zifang.util.xml.binding;

import com.zifang.util.xml.binding.fixture.ReqHeader;
import com.zifang.util.xml.binding.fixture.SmsBody;
import com.zifang.util.xml.binding.fixture.SmsDeliverReq;
import com.zifang.util.xml.binding.annotation.XmlElement;
import com.zifang.util.xml.binding.annotation.XmlElementWrapper;
import com.zifang.util.xml.binding.annotation.XmlRootElement;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * XmlBinding 性能基准测试。
 * <p>
 * 覆盖小对象、大批量对象与嵌套列表三种形态的序列化/反序列化耗时，
 * 并校验 round-trip 的字段一致性。JAXB 已全域退场，此处不再有对照组。
 */
public class XmlBindingPerformanceTest {

    // ==================== 测试数据构造 ====================

    /**
     * 构造一个典型的 SmsDeliverReq（2条短信）
     */
    private SmsDeliverReq buildTypicalSms() {
        ReqHeader header = new ReqHeader();
        header.setSysId("SYSTEM001");
        header.setAuthCode("AUTH123");
        header.setReqNo("REQ456");

        SmsBody body1 = new SmsBody();
        body1.setContent("你好，这是一条测试短信内容，包含中文和English混合文本");
        body1.setSourceAddr("13800138000");
        body1.setDestAddr("10086");

        SmsBody body2 = new SmsBody();
        body2.setContent("第二条短信：The quick brown fox jumps over the lazy dog 0123456789");
        body2.setSourceAddr("13900139000");
        body2.setDestAddr("10010");

        SmsDeliverReq req = new SmsDeliverReq();
        req.setReqHeader(header);
        List<SmsBody> bodies = new ArrayList<>();
        bodies.add(body1);
        bodies.add(body2);
        req.setSmsBodys(bodies);
        return req;
    }

    /**
     * 构造大批量数据（100条短信）
     */
    private SmsDeliverReq buildLargeSms() {
        ReqHeader header = new ReqHeader();
        header.setSysId("BATCH_SYSTEM");
        header.setAuthCode("BATCH_AUTH");
        header.setReqNo("BATCH_001");

        SmsDeliverReq req = new SmsDeliverReq();
        req.setReqHeader(header);
        List<SmsBody> bodies = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SmsBody body = new SmsBody();
            body.setContent("短信内容 #" + i + "：这是一段较长的测试文本，用于验证大数据量下的序列化性能表现。"
                    + "The quick brown fox jumps over the lazy dog. 0123456789!@#$%^&*()");
            body.setSourceAddr("138" + String.format("%08d", i));
            body.setDestAddr("100" + String.format("%06d", i));
            bodies.add(body);
        }
        req.setSmsBodys(bodies);
        return req;
    }

    /**
     * 构造自研注解的简单 bean（嵌套列表形态）
     */
    @XmlRootElement(name = "SimpleItem")
    public static class SimpleItem {
        private String name;
        private int value;
        private boolean active;

        public SimpleItem() {}
        public SimpleItem(String name, int value, boolean active) {
            this.name = name;
            this.value = value;
            this.active = active;
        }

        @XmlElement(name = "NAME")
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @XmlElement(name = "VALUE")
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }

        @XmlElement(name = "ACTIVE")
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }

    @XmlRootElement(name = "ItemList")
    public static class ItemList {
        private List<SimpleItem> items;

        public ItemList() {}
        public ItemList(List<SimpleItem> items) { this.items = items; }

        @XmlElementWrapper(name = "ITEMS")
        @XmlElement(name = "ITEM")
        public List<SimpleItem> getItems() { return items; }
        public void setItems(List<SimpleItem> items) { this.items = items; }
    }

    // ==================== 性能测试 ====================

    /**
     * 性能基准：序列化
     */
    @Test
    public void testSerializationPerformance() throws Exception {
        SmsDeliverReq typical = buildTypicalSms();
        SmsDeliverReq large = buildLargeSms();

        // 预热
        for (int i = 0; i < 100; i++) {
            XmlBinding.toXml(typical);
        }

        int warmup = 100;
        int iterations = 1000;

        // === 小对象序列化 ===
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            XmlBinding.toXml(typical);
        }
        long selfSmall = System.nanoTime() - start;
        String selfSmallXml = XmlBinding.toXml(typical);

        // === 大对象序列化 ===
        start = System.nanoTime();
        for (int i = 0; i < warmup; i++) {
            XmlBinding.toXml(large);
        }
        long selfLargeStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            XmlBinding.toXml(large);
        }
        long selfLarge = System.nanoTime() - selfLargeStart;

        // === 嵌套列表序列化 ===
        ItemList list = new ItemList();
        List<SimpleItem> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(new SimpleItem("item_" + i, i, i % 2 == 0));
        }
        list.setItems(items);

        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            XmlBinding.toXml(list);
        }
        long selfSimple = System.nanoTime() - start;

        // 输出结果
        System.out.println("=== 序列化性能 (" + iterations + " 次) ===");
        printResult("小对象 SmsDeliverReq (2条)", selfSmall);
        printResult("大对象 SmsDeliverReq (100条)", selfLarge);
        printResult("嵌套列表 ItemList (50项)", selfSimple);

        // 功能性验证
        assertTrue("输出应包含根元素", selfSmallXml.contains("<SMSDELIVERREQ>"));
        assertTrue("输出应包含头部", selfSmallXml.contains("<REQHEADER>"));
        assertTrue("输出应包含短信列表", selfSmallXml.contains("<SMSBODYS>"));
    }

    /**
     * 性能基准：反序列化
     */
    @Test
    public void testDeserializationPerformance() throws Exception {
        SmsDeliverReq typical = buildTypicalSms();
        SmsDeliverReq large = buildLargeSms();

        String typicalXml = XmlBinding.toXml(typical);
        String largeXml = XmlBinding.toXml(large);

        ItemList list = new ItemList();
        List<SimpleItem> items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add(new SimpleItem("item_" + i, i, i % 2 == 0));
        }
        list.setItems(items);
        String listXml = XmlBinding.toXml(list);

        // 预热
        for (int i = 0; i < 100; i++) {
            XmlBinding.fromXml(typicalXml, SmsDeliverReq.class);
        }

        int iterations = 1000;
        int warmup = 100;

        // === 小对象反序列化 ===
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            XmlBinding.fromXml(typicalXml, SmsDeliverReq.class);
        }
        long selfSmall = System.nanoTime() - start;

        // === 大对象反序列化 ===
        start = System.nanoTime();
        for (int i = 0; i < warmup; i++) {
            XmlBinding.fromXml(largeXml, SmsDeliverReq.class);
        }
        long selfLargeStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            XmlBinding.fromXml(largeXml, SmsDeliverReq.class);
        }
        long selfLarge = System.nanoTime() - selfLargeStart;

        // === 嵌套列表反序列化 ===
        start = System.nanoTime();
        for (int i = 0; i < warmup; i++) {
            XmlBinding.fromXml(listXml, ItemList.class);
        }
        long selfListStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            XmlBinding.fromXml(listXml, ItemList.class);
        }
        long selfList = System.nanoTime() - selfListStart;

        System.out.println("\n=== 反序列化性能 (" + iterations + " 次) ===");
        printResult("小对象 SmsDeliverReq (2条)", selfSmall);
        printResult("大对象 SmsDeliverReq (100条)", selfLarge);
        printResult("嵌套列表 ItemList (50项)", selfList);

        // 功能验证
        SmsDeliverReq parsed = XmlBinding.fromXml(typicalXml, SmsDeliverReq.class);
        assertNotNull(parsed);
        assertEquals("SYSTEM001", parsed.getReqHeader().getSysId());
        assertEquals(2, parsed.getSmsBodys().size());
    }

    /**
     * Round-trip 一致性验证
     */
    @Test
    public void testRoundTripConsistency() throws Exception {
        SmsDeliverReq req = buildTypicalSms();

        // 自研方案 round-trip
        String selfXml = XmlBinding.toXml(req);
        SmsDeliverReq selfParsed = XmlBinding.fromXml(selfXml, SmsDeliverReq.class);

        // 验证字段一致
        assertNotNull(selfParsed);
        assertNotNull(selfParsed.getReqHeader());
        assertEquals(req.getReqHeader().getSysId(), selfParsed.getReqHeader().getSysId());
        assertEquals(req.getReqHeader().getAuthCode(), selfParsed.getReqHeader().getAuthCode());
        assertEquals(req.getReqHeader().getReqNo(), selfParsed.getReqHeader().getReqNo());
        assertNotNull(selfParsed.getSmsBodys());
        assertEquals(req.getSmsBodys().size(), selfParsed.getSmsBodys().size());
        for (int i = 0; i < req.getSmsBodys().size(); i++) {
            assertEquals(req.getSmsBodys().get(i).getContent(),
                    selfParsed.getSmsBodys().get(i).getContent());
            assertEquals(req.getSmsBodys().get(i).getSourceAddr(),
                    selfParsed.getSmsBodys().get(i).getSourceAddr());
            assertEquals(req.getSmsBodys().get(i).getDestAddr(),
                    selfParsed.getSmsBodys().get(i).getDestAddr());
        }

        System.out.println("\n=== Round-trip 一致性 ===");
        System.out.println("自研方案 XML 长度: " + selfXml.length() + " bytes");
        System.out.println("所有字段完全一致: PASS");
    }

    // ==================== 辅助方法 ====================

    private void printResult(String label, long nanos) {
        System.out.printf("  %-30s %8.2fms%n", label, nanos / 1_000_000.0);
    }
}
