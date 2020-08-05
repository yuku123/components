package com.zifang.util.zex.kafka.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerFastStart {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);


        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
//        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello, Kafka!");
        try {
            System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>(topic, String.valueOf(i)));
            }

        System.out.println(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }
}