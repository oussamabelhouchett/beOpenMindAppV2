package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ReactionMapperTest {

    private ReactionMapper reactionMapper;

    @BeforeEach
    public void setUp() {
        reactionMapper = new ReactionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(reactionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(reactionMapper.fromId(null)).isNull();
    }
}
