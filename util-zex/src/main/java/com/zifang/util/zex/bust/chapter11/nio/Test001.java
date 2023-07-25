package com.zifang.util.zex.bust.chapter11.nio;

import org.junit.Test;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class Test001 {

    @Test
    public void test001() throws Exception {
        // 我们定义出一个数组，里面10个参数
        byte[] byteArray = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteBuffer bytebuffer = ByteBuffer.wrap(byteArray);

        // 我们先打印初始化的时候的各个核心参数的数值
        print4Point(bytebuffer); // 打印：mark:-1|position:0|limit:10|capacity:10

        // 然后我们故意来读取数据
        readTimes(bytebuffer, 3);
        print4Point(bytebuffer); // 打印：mark:-1|position:3|limit:10|capacity:10
        // mark一下
        bytebuffer.mark();
        print4Point(bytebuffer); // 打印：mark:3|position:3|limit:10|capacity:10
        // 缩小limit
        bytebuffer.limit(8);
        print4Point(bytebuffer); // 打印：mark:3|position:3|limit:8|capacity:10
        // 手动调整position
        bytebuffer.position(5);
        print4Point(bytebuffer); // 打印：mark:3|position:5|limit:8|capacity:10
    }

    @Test
    public void test002() throws Exception {
        // 我们定义出一个数组，里面10个参数
        byte[] byteArray = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteBuffer bytebuffer = ByteBuffer.wrap(byteArray);

        // 我们先打印初始化的时候的各个核心参数的数值
        print4Point(bytebuffer); // 打印：mark:-1|position:0|limit:10|capacity:10

        // 然后我们故意来读取数据
        readTimes(bytebuffer, 3);
        print4Point(bytebuffer); // 打印：mark:-1|position:3|limit:10|capacity:10
        // mark一下
        bytebuffer.mark();
        print4Point(bytebuffer); // 打印：mark:3|position:3|limit:10|capacity:10
        // 缩小limit
        bytebuffer.limit(8);
        print4Point(bytebuffer); // 打印：mark:3|position:3|limit:8|capacity:10
        // 手动调整position
        bytebuffer.position(5);
        print4Point(bytebuffer); // 打印：mark:3|position:5|limit:8|capacity:10

        // 现在故意将limit调整到2
        bytebuffer.limit(2);
        print4Point(bytebuffer); // 打印：mark:-1|position:2|limit:2|capacity:10
    }

    @Test
    public void test03() throws Exception {
        // 我们定义出一个数组，里面10个参数
        byte[] byteArray = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteBuffer bytebuffer = ByteBuffer.wrap(byteArray);

        // 我们先打印初始化的时候的各个核心参数的数值
        print4Point(bytebuffer); // 打印：mark:-1|position:0|limit:10|capacity:10

        // 然后我们故意来读取数据
        readTimes(bytebuffer, 3);
        print4Point(bytebuffer); // 打印：mark:-1|position:3|limit:10|capacity:10
        // mark一下
        bytebuffer.mark();
        print4Point(bytebuffer); // 打印：mark:3|position:3|limit:10|capacity:10
        // 缩小limit
        bytebuffer.limit(8);
        print4Point(bytebuffer); // 打印：mark:3|position:3|limit:8|capacity:10
        // 手动调整position
        bytebuffer.position(5);
        print4Point(bytebuffer); // 打印：mark:3|position:5|limit:8|capacity:10

        // 现在故意将limit调整到2
        bytebuffer.position(2);
        print4Point(bytebuffer); // 打印：mark:-1|position:2|limit:2|capacity:10
    }

    @Test
    public void test4() {
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[100]);
        ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[100], 1, 100);
        ByteBuffer byteBuffer3 = ByteBuffer.allocate(100);
        ByteBuffer byteBuffer4 = ByteBuffer.allocateDirect(100);

        byteBuffer2.get();
    }

    // 读取i次
    private void readTimes(ByteBuffer bytebuffer, int i) {
        for (int j = 0; j < i; j++) {
            bytebuffer.get();
        }
    }

    public void print4Point(ByteBuffer byteBuffer) throws Exception {
        // mark的值没有显式的获取方式，我们用反射直接获取
        Field field = Buffer.class.getDeclaredField("mark");
        field.setAccessible(true);
        int markValue = (Integer) field.get(byteBuffer);

        System.out.println(
                "mark:" + markValue + "|" +
                        "position:" + byteBuffer.position() + "|" +
                        "limit:" + byteBuffer.limit() + "|" +
                        "capacity:" + byteBuffer.capacity());
    }
}
