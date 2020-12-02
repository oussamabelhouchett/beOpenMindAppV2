package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class FilesPostDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilesPostDTO.class);
        FilesPostDTO filesPostDTO1 = new FilesPostDTO();
        filesPostDTO1.setId(1L);
        FilesPostDTO filesPostDTO2 = new FilesPostDTO();
        assertThat(filesPostDTO1).isNotEqualTo(filesPostDTO2);
        filesPostDTO2.setId(filesPostDTO1.getId());
        assertThat(filesPostDTO1).isEqualTo(filesPostDTO2);
        filesPostDTO2.setId(2L);
        assertThat(filesPostDTO1).isNotEqualTo(filesPostDTO2);
        filesPostDTO1.setId(null);
        assertThat(filesPostDTO1).isNotEqualTo(filesPostDTO2);
    }
}
