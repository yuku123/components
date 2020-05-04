package com.zifang.util.redis.basic;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;


public class BasicTest {

    Jedis jedis = new Jedis("127.0.0.1", 6379);

    @Test
    public void del(){
        jedis.del("key");                           // 清空
        assertFalse(jedis.exists("key"));           // 判断是否清空
        jedis.set("key", "v吃饭");
        assertTrue(jedis.exists("key"));
        assertEquals("v吃饭",jedis.get("key")); // 获得key值
    }

    @Test
    public void test2(){
        jedis.del("a1","a2","a3");                // 清空
        jedis.set("a1","a1");
        jedis.set("a2","a2");
        jedis.set("a3","a3");
        assertEquals("[a1, a2, a3]",jedis.mget("a1","a2","a3").toString()); // 获得key值
    }


    @Test
    public void test3(){
        //自增 计数
        jedis.del("a1");// 清空
        jedis.set("a1","2");// 设值
        jedis.incr("a1");// 自增
        assertEquals(3,Integer.valueOf(jedis.get("a1"))); // 获得自增之后的值
    }

    @Test
    public void test4(){
        //队列 先进先出 先进1 那么久先出1
        jedis.del("list");// 清空
        jedis.rpush("list","1","2","3");// 入队   list -> [1,2,3]
        assertEquals(3,jedis.llen("list"));//判断列表是否为三个
        assertEquals("1",jedis.lpop("list"));// 出队列
        assertEquals("2",jedis.lpop("list"));// 出队列
        assertEquals("3",jedis.lpop("list"));// 出队列
        assertEquals(null,jedis.lpop("list"));// 出队列 队列里面没了那就返回null

    }

    @Test
    public void test5(){
        //栈 先进后出 先进1，2，3 那么久先出3
        jedis.del("list");// 清空
        jedis.rpush("list","1","2","3");// 入队   list -> [1,2,3]
        assertEquals(3,jedis.llen("list"));//判断列表是否为三个
        assertEquals("3",jedis.rpop("list"));// 出队列
        assertEquals("2",jedis.rpop("list"));// 出队列
        assertEquals("1",jedis.rpop("list"));// 出队列
        assertEquals(null,jedis.rpop("list"));// 出队列 队列里面没了那就返回null
    }

    @Test
    public void test6(){
        //list 元素获取操作
        jedis.del("list");// 清空
        jedis.rpush("list","1","2","3");// 入队   list -> [1,2,3]
        assertEquals(3,jedis.llen("list"));//判断列表是否为三个
        assertEquals("1",jedis.lindex("list",0));// 出队列
        assertEquals(3,jedis.llen("list"));//判断列表是否为三个 可以返现index只是获取没有弹出
        assertEquals("[1, 2]",jedis.lrange("list",0,1).toString());

    }

    @Test
    public void test7(){
        //list 元素获取操作
        jedis.del("list");// 清空
        jedis.rpush("list","1","2","3");// 入队   list -> [1,2,3]
        jedis.ltrim("list",1,2);// 1-2 区间内的砍掉
        assertEquals(2,jedis.llen("list"));//判断列表是否为三个
        assertEquals("2",jedis.lpop("list"));// 出队列
    }

    @Test
    public void test8(){
        //hash 相关的操作
        jedis.del("hash");// 清空
        jedis.hset("hash","a","a1");
        jedis.hset("hash","b","2");
        assertEquals(2,jedis.hlen("hash"));
        assertEquals("a1",jedis.hget("hash","a"));
        assertEquals("a1",jedis.hget("hash","a"));
        assertEquals("{b=2, a=a1}",jedis.hgetAll("hash").toString());
        jedis.hincrBy("hash","b",1);
        assertEquals("3",jedis.hget("hash","b"));
    }

    @Test
    public void test9(){
        //set 相关的操作
        jedis.del("set");// 清空
        jedis.sadd("set","set1");
        jedis.sadd("set","set2");
        assertTrue(jedis.sismember("set","set1"));  // 判断在set里面
        assertFalse(jedis.sismember("set","set3")); // 判断不在里面
        assertEquals(2,jedis.scard("set"));
        System.out.println(jedis.smembers("set"));
        System.out.println(jedis.spop("set")); // 弹出一个

    }

    @Test
    public void test10(){
        //zset 相关的操作
        jedis.del("zset");// 清空
        jedis.zadd("zset",12d,"zset1");
        jedis.zadd("zset",11d,"zset2");
        assertEquals("[zset2, zset1]",jedis.zrange("zset",0,-1).toString());
        assertEquals("[zset1, zset2]",jedis.zrevrange("zset",0,-1).toString());
        assertEquals(2,jedis.zcard("zset"));
        System.out.println(jedis.zrange("zset",0,-1));
        System.out.println(jedis.zscore("zset","zset1"));

        // zrank
        // zrangebyscore
        // zrem
    }
}
