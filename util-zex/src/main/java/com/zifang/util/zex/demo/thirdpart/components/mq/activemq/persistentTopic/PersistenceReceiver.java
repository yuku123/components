package com.zifang.util.zex.demo.thirdpart.components.mq.activemq.persistentTopic;
import com.zifang.util.zex.demo.thirdpart.components.mq.activemq.Constants;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class PersistenceReceiver {


    public static void main(String[] args) {
        ConnectionFactory connectionFactory;//连接工厂
        Connection connection = null;//连接

        Session session;//会话 接受或者发送消息的线程
        Topic topic;//消息的目的地

        //实例化连接工厂(连接到ActiveMQ服务器)
        connectionFactory = new ActiveMQConnectionFactory(Constants.MQ_NAME, Constants.MQ_PASSWORD, Constants.MQ_BROKETURL);

        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            connection.setClientID("winner_0715");
            //创建session
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //生产者将消息发送到MyTopic，所以消费者要到MyTopic去取
            topic = session.createTopic("MyTopic");
            //创建消息消费者
            TopicSubscriber consumer = session.createDurableSubscriber(topic, "t1");

            //启动连接
            connection.start();

            Message message = consumer.receive();
            while (message != null) {
                TextMessage txtMsg = (TextMessage) message;
                System.out.println("收到消 息：" + txtMsg.getText());
                //没这句有错
                message = consumer.receive(1000L);
            }
            session.commit();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}