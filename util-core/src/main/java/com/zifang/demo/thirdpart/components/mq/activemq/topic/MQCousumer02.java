package com.zifang.demo.thirdpart.components.mq.activemq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.zifang.demo.thirdpart.components.mq.activemq.Constants;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * <p>
 * MQCousumer02 订阅-发布模式 订阅者02
 * <p>
 */
public class MQCousumer02 {

    public static void main(String[] args) {
        // 连接工厂
        ConnectionFactory factory;
        // 连接实例
        Connection connection = null;
        // 收发的线程实例
        Session session;
        // 消息发送目标地址
        Destination destination;
        try {
            // 实例化连接工厂
            factory = new ActiveMQConnectionFactory(Constants.MQ_NAME, Constants.MQ_PASSWORD, Constants.MQ_BROKETURL);
            // 获取连接实例
            connection = factory.createConnection();
            // 启动连接
            connection.start();
            // 创建接收或发送的线程实例
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            // 创建队列（返回一个消息目的地）
            destination = session.createTopic("parryTopic");
            // 创建消息订阅者
            MessageConsumer consumer = session.createConsumer(destination);
            // 消息发布者添加监听器
            consumer.setMessageListener(new Listerner02());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}