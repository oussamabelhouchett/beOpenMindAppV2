package com.mycompany.myapp.service.kafka.consumer;

import io.vavr.control.Either;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import com.mycompany.myapp.config.KafkaProperties;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.service.kafka.serde.DeserializationError;

@Service
public class PostConsumer extends GenericConsumer<Post> {

    private final Logger log = LoggerFactory.getLogger(PostConsumer.class);

    public PostConsumer(@Value("${kafka.topic.post}") final String topicName, final KafkaProperties kafkaProperties) {
        super(topicName, kafkaProperties.getConsumer().get("post"), kafkaProperties.getPollingTimeout());
    }

    @Override
    protected void handleMessage(final ConsumerRecord<String, Either<DeserializationError, Post>> record) {

        // TODO /!\ Maybe you could delete the next log calls to avoid disclosing personal user information

        final Either<DeserializationError, Post> value = record.value();

        if (value == null) {
            log.error("Null value in record {}", record);
            return;
        }

        if (value.isLeft()) {
            log.error("Deserialization record failure: {}", value.getLeft());
            return;
        }

        log.info("Handling record: {}", value.get());

        // TODO: Here is where you can handle your records
    }

    @Bean
    public void executeKafkaPostRunner() {
        new SimpleAsyncTaskExecutor().execute(this);
    }
}
