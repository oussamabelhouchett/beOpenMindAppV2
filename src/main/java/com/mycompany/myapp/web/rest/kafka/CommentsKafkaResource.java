package com.mycompany.myapp.web.rest.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.service.kafka.producer.CommentsProducer;
import com.mycompany.myapp.domain.Comments;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Comments} through Kafka.
 */
//@RestController
//@RequestMapping("/api")
public class CommentsKafkaResource {

    private final Logger log = LoggerFactory.getLogger(CommentsKafkaResource.class);

    private final CommentsProducer commentsProducer;

    public CommentsKafkaResource(CommentsProducer commentsProducer) {
        this.commentsProducer = commentsProducer;
    }

    /**
     * {@code POST  /commentss/kafka} : Send a comments in Kafka.
     *
     * @param comments the comments to send.
     */
    @PostMapping("/commentss/kafka")
    public void sendComments(@RequestBody Comments comments) {
        log.debug("REST request to send a Comments in Kafka: {}", comments);
        commentsProducer.send(comments);
    }
}
