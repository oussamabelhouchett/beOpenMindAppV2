package com.mycompany.myapp.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class ReactionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReactionDTO.class);
        ReactionDTO reactionDTO1 = new ReactionDTO();
        reactionDTO1.setId(1L);
        ReactionDTO reactionDTO2 = new ReactionDTO();
        assertThat(reactionDTO1).isNotEqualTo(reactionDTO2);
        reactionDTO2.setId(reactionDTO1.getId());
        assertThat(reactionDTO1).isEqualTo(reactionDTO2);
        reactionDTO2.setId(2L);
        assertThat(reactionDTO1).isNotEqualTo(reactionDTO2);
        reactionDTO1.setId(null);
        assertThat(reactionDTO1).isNotEqualTo(reactionDTO2);
    }
}
