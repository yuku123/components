package com.zifang.util.zex.demo.thirdpart.components.mq.activemq.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
/**
 * <p>
 * MQListerner 生产者监听器
 * <p>
 */
public class MQListerner implements MessageListener{

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(System.currentTimeMillis());
            System.out.println(((TextMessage)message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}