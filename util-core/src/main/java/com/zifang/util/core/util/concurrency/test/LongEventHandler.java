package com.zifang.util.core.util.concurrency.test;


import com.lmax.disruptor.EventHandler;

//消费者实现为WorkHandler接口，是Disruptor框架中的类
public class LongEventHandler implements EventHandler<LongEvent>
{
    //onEvent()方法是框架的回调用法
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println("Event: " + event);
    }
}