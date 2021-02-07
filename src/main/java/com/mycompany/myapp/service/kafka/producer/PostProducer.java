package com.mycompany.myapp.service.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

import com.mycompany.myapp.config.KafkaProperties;
import com.mycompany.myapp.domain.Post;

//@Service
public class PostProducer {

    enum PostProducerKey {
        POST_CREATE, POST_READ, POST_UPDATE, POST_DELETE
    }

    private final Logger log = LoggerFactory.getLogger(PostProducer.class);

    private final KafkaProducer<String, Post> kafkaProducer;

    private final String topicName;

    public PostProducer(@Value("${kafka.topic.post}") final String topicName, final KafkaProperties kafkaProperties) {
        this.topicName = topicName;
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties.getProducer().get("post"));
    }

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void send(final Post message) {
        final ProducerRecord<String, Post> record = new ProducerRecord<>(topicName, PostProducerKey.POST_READ.toString(), message);
        try {
            log.info("Sending asynchronously a Post record to topic: '" + topicName + "'");
            kafkaProducer.send(record);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void shutdown() {
        log.info("Shutdown Kafka producer");
        kafkaProducer.close();
    }
}
