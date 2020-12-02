package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class FilesPostTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilesPost.class);
        FilesPost filesPost1 = new FilesPost();
        filesPost1.setId(1L);
        FilesPost filesPost2 = new FilesPost();
        filesPost2.setId(filesPost1.getId());
        assertThat(filesPost1).isEqualTo(filesPost2);
        filesPost2.setId(2L);
        assertThat(filesPost1).isNotEqualTo(filesPost2);
        filesPost1.setId(null);
        assertThat(filesPost1).isNotEqualTo(filesPost2);
    }
}
