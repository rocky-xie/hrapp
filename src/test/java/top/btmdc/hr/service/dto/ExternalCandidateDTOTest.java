package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ExternalCandidateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalCandidateDTO.class);
        ExternalCandidateDTO externalCandidateDTO1 = new ExternalCandidateDTO();
        externalCandidateDTO1.setId(1L);
        ExternalCandidateDTO externalCandidateDTO2 = new ExternalCandidateDTO();
        assertThat(externalCandidateDTO1).isNotEqualTo(externalCandidateDTO2);
        externalCandidateDTO2.setId(externalCandidateDTO1.getId());
        assertThat(externalCandidateDTO1).isEqualTo(externalCandidateDTO2);
        externalCandidateDTO2.setId(2L);
        assertThat(externalCandidateDTO1).isNotEqualTo(externalCandidateDTO2);
        externalCandidateDTO1.setId(null);
        assertThat(externalCandidateDTO1).isNotEqualTo(externalCandidateDTO2);
    }
}
