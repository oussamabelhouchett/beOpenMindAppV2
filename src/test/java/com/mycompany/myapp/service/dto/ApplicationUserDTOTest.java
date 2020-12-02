package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class ApplicationUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUserDTO.class);
        ApplicationUserDTO applicationUserDTO1 = new ApplicationUserDTO();
        applicationUserDTO1.setId(1L);
        ApplicationUserDTO applicationUserDTO2 = new ApplicationUserDTO();
        assertThat(applicationUserDTO1).isNotEqualTo(applicationUserDTO2);
        applicationUserDTO2.setId(applicationUserDTO1.getId());
        assertThat(applicationUserDTO1).isEqualTo(applicationUserDTO2);
        applicationUserDTO2.setId(2L);
        assertThat(applicationUserDTO1).isNotEqualTo(applicationUserDTO2);
        applicationUserDTO1.setId(null);
        assertThat(applicationUserDTO1).isNotEqualTo(applicationUserDTO2);
    }
}
