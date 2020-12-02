package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesPostMapperTest {

    private FilesPostMapper filesPostMapper;

    @BeforeEach
    public void setUp() {
        filesPostMapper = new FilesPostMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(filesPostMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(filesPostMapper.fromId(null)).isNull();
    }
}
