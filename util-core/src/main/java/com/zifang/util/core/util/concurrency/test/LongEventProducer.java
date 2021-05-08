package com.zifang.util.core.util.concurrency.test;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

//消费者实现为WorkHandler接口，是Disruptor框架中的类
public class LongEventProducer
{
    //环形缓冲区,装载生产好的数据；
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

    //将数据推入到缓冲区的方法：将数据装载到ringBuffer
    public void onData(ByteBuffer bb)
    {
        long sequence = ringBuffer.next(); // Grab the next sequence //获取下一个可用的序列号
        try
        {
            LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor //通过序列号获取空闲可用的LongEvent
// for the sequence
            event.set(bb.getLong(0)); // Fill with data //设置数值
        }
        finally
        {
            ringBuffer.publish(sequence); //数据发布，只有发布后的数据才会真正被消费者看见
        }
    }
}