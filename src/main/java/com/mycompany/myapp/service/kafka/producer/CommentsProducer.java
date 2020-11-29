package com.mycompany.myapp.service.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

import com.mycompany.myapp.config.KafkaProperties;
import com.mycompany.myapp.domain.Comments;

@Service
public class CommentsProducer {

    enum CommentsProducerKey {
        COMMENTS_CREATE, COMMENTS_READ, COMMENTS_UPDATE, COMMENTS_DELETE
    }

    private final Logger log = LoggerFactory.getLogger(CommentsProducer.class);

    private final KafkaProducer<String, Comments> kafkaProducer;

    private final String topicName;

    public CommentsProducer(@Value("${kafka.topic.comments}") final String topicName, final KafkaProperties kafkaProperties) {
        this.topicName = topicName;
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties.getProducer().get("comments"));
    }

    @PostConstruct
    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void send(final Comments message) {
        final ProducerRecord<String, Comments> record = new ProducerRecord<>(topicName, CommentsProducerKey.COMMENTS_READ.toString(), message);
        try {
            log.info("Sending asynchronously a Comments record to topic: '" + topicName + "'");
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
