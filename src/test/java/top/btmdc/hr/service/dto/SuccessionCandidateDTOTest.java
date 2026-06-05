package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SuccessionCandidateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuccessionCandidateDTO.class);
        SuccessionCandidateDTO successionCandidateDTO1 = new SuccessionCandidateDTO();
        successionCandidateDTO1.setId(1L);
        SuccessionCandidateDTO successionCandidateDTO2 = new SuccessionCandidateDTO();
        assertThat(successionCandidateDTO1).isNotEqualTo(successionCandidateDTO2);
        successionCandidateDTO2.setId(successionCandidateDTO1.getId());
        assertThat(successionCandidateDTO1).isEqualTo(successionCandidateDTO2);
        successionCandidateDTO2.setId(2L);
        assertThat(successionCandidateDTO1).isNotEqualTo(successionCandidateDTO2);
        successionCandidateDTO1.setId(null);
        assertThat(successionCandidateDTO1).isNotEqualTo(successionCandidateDTO2);
    }
}
