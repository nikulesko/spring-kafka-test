package com.example.springkakatest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Producer implements Closeable {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public Producer(String topic) {
        producer = initProducer();
        this.topic = topic;
    }

    private KafkaProducer<String, String> initProducer() {
        Properties props = new Properties();

        String kafkaPath = System.getenv("KAFKA_PATH"); //"localhost:9092"

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPath);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "clientId");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<String, String> (props);
    }

    public void send(String key, String value) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<>(topic, key, value)).get();
    }

    @Override
    public void close() throws IOException {
        producer.close();
    }
}
