package com.example.springkakatest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer implements Closeable {
    private final String topic;
    private final KafkaConsumer<String, String> consumer;

    public Consumer(String topic) {
        this.topic = topic;
        this.consumer = initConsumer();
    }

    private KafkaConsumer<String, String> initConsumer() {
        Properties props = new Properties();

        String kafkaPath = System.getenv("KAFKA_PATH"); //"localhost:9092"

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPath);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }

    public void consumer(java.util.function.Consumer<ConsumerRecord<String, String>> recordsConsumer) {
        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                records.forEach(recordsConsumer);
            }
        }).start();
    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
