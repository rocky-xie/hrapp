package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PreboardingFollowUpDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreboardingFollowUpDTO.class);
        PreboardingFollowUpDTO preboardingFollowUpDTO1 = new PreboardingFollowUpDTO();
        preboardingFollowUpDTO1.setId(1L);
        PreboardingFollowUpDTO preboardingFollowUpDTO2 = new PreboardingFollowUpDTO();
        assertThat(preboardingFollowUpDTO1).isNotEqualTo(preboardingFollowUpDTO2);
        preboardingFollowUpDTO2.setId(preboardingFollowUpDTO1.getId());
        assertThat(preboardingFollowUpDTO1).isEqualTo(preboardingFollowUpDTO2);
        preboardingFollowUpDTO2.setId(2L);
        assertThat(preboardingFollowUpDTO1).isNotEqualTo(preboardingFollowUpDTO2);
        preboardingFollowUpDTO1.setId(null);
        assertThat(preboardingFollowUpDTO1).isNotEqualTo(preboardingFollowUpDTO2);
    }
}
