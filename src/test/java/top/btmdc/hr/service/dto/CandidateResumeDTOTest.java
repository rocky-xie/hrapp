package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class CandidateResumeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateResumeDTO.class);
        CandidateResumeDTO candidateResumeDTO1 = new CandidateResumeDTO();
        candidateResumeDTO1.setId(1L);
        CandidateResumeDTO candidateResumeDTO2 = new CandidateResumeDTO();
        assertThat(candidateResumeDTO1).isNotEqualTo(candidateResumeDTO2);
        candidateResumeDTO2.setId(candidateResumeDTO1.getId());
        assertThat(candidateResumeDTO1).isEqualTo(candidateResumeDTO2);
        candidateResumeDTO2.setId(2L);
        assertThat(candidateResumeDTO1).isNotEqualTo(candidateResumeDTO2);
        candidateResumeDTO1.setId(null);
        assertThat(candidateResumeDTO1).isNotEqualTo(candidateResumeDTO2);
    }
}
