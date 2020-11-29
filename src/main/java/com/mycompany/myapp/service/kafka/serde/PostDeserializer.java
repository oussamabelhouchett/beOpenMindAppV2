package com.mycompany.myapp.service.kafka.serde;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.vavr.control.Either;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import com.mycompany.myapp.domain.Post;

public class PostDeserializer implements Deserializer<Either<DeserializationError, Post>> {

    private final Logger log = LoggerFactory.getLogger(PostDeserializer.class);

    private final ObjectMapper objectMapper;

    public PostDeserializer() {
        this.objectMapper =
            new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setDateFormat(new StdDateFormat());
    }

    @Override
    public Either<DeserializationError, Post> deserialize(final String topicName, final byte[] data) {
        try {
            final Post value = objectMapper.readValue(data, Post.class);
            return Either.right(value);
        } catch (final IOException e) {
            return Either.left(new DeserializationError(data, e));
        }
    }
}
