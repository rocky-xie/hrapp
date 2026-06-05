package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class CandidateProfileDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateProfileDTO.class);
        CandidateProfileDTO candidateProfileDTO1 = new CandidateProfileDTO();
        candidateProfileDTO1.setId(1L);
        CandidateProfileDTO candidateProfileDTO2 = new CandidateProfileDTO();
        assertThat(candidateProfileDTO1).isNotEqualTo(candidateProfileDTO2);
        candidateProfileDTO2.setId(candidateProfileDTO1.getId());
        assertThat(candidateProfileDTO1).isEqualTo(candidateProfileDTO2);
        candidateProfileDTO2.setId(2L);
        assertThat(candidateProfileDTO1).isNotEqualTo(candidateProfileDTO2);
        candidateProfileDTO1.setId(null);
        assertThat(candidateProfileDTO1).isNotEqualTo(candidateProfileDTO2);
    }
}
