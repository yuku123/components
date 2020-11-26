package com.zifang.util.zex.components.mq.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.zifang.util.zex.components.mq.activemq.Constants;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * <p>
 * MQConsumer 点对点--消息消费者
 * <p>
 */
public class MQConsumer {

    public static void main(String[] args) {
        // 连接工厂
        ConnectionFactory connectionFactory;
        // 连接实例
        Connection connection = null;
        // 收发的线程实例
        Session session;
        // 消息发送目标地址
        Destination destination;
        try {
            // 实例化连接工厂
            connectionFactory = new ActiveMQConnectionFactory(Constants.MQ_NAME, Constants.MQ_PASSWORD, Constants.MQ_BROKETURL);
            // 获取连接实例
            connection = connectionFactory.createConnection();
            // 启动连接
            connection.start();
            // 创建接收或发送的线程实例（消费者就不需要开启事务了）
            session = connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
            // 创建队列（返回一个消息目的地）
            destination = session.createQueue("parryQuene");
            // 创建消息消费者
            MessageConsumer consumer = session.createConsumer(destination);
            //注册消息监听
            consumer.setMessageListener(new MQListerner());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}