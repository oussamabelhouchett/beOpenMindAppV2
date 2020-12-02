package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PostMapperTest {

    private PostMapper postMapper;

    @BeforeEach
    public void setUp() {
        postMapper = new PostMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(postMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(postMapper.fromId(null)).isNull();
    }
}
