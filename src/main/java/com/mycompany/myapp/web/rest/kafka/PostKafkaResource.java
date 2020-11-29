package com.mycompany.myapp.web.rest.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.service.kafka.producer.PostProducer;
import com.mycompany.myapp.domain.Post;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Post} through Kafka.
 */
@RestController
@RequestMapping("/api")
public class PostKafkaResource {

    private final Logger log = LoggerFactory.getLogger(PostKafkaResource.class);

    private final PostProducer postProducer;

    public PostKafkaResource(PostProducer postProducer) {
        this.postProducer = postProducer;
    }

    /**
     * {@code POST  /posts/kafka} : Send a post in Kafka.
     *
     * @param post the post to send.
     */
    @PostMapping("/posts/kafka")
    public void sendPost(@RequestBody Post post) {
        log.debug("REST request to send a Post in Kafka: {}", post);
        postProducer.send(post);
    }
}
