package com.mycompany.myapp.service.kafka.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

import com.mycompany.myapp.service.kafka.serde.PostSerializer;
import com.mycompany.myapp.service.kafka.serde.PostDeserializer;

public class PostSerde<Post> implements Serde<Post> {
    private final Serializer serializer = new PostSerializer();
    private final Deserializer deserializer = new PostDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        this.serializer.configure(configs, isKey);
        this.deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        this.serializer.close();
        this.deserializer.close();
    }

    @Override
    public Serializer<Post> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<Post> deserializer() {
        return deserializer;
    }
}
