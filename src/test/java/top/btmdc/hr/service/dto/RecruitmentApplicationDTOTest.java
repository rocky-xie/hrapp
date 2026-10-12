package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentApplicationDTO.class);
        RecruitmentApplicationDTO recruitmentApplicationDTO1 = new RecruitmentApplicationDTO();
        recruitmentApplicationDTO1.setId(1L);
        RecruitmentApplicationDTO recruitmentApplicationDTO2 = new RecruitmentApplicationDTO();
        assertThat(recruitmentApplicationDTO1).isNotEqualTo(recruitmentApplicationDTO2);
        recruitmentApplicationDTO2.setId(recruitmentApplicationDTO1.getId());
        assertThat(recruitmentApplicationDTO1).isEqualTo(recruitmentApplicationDTO2);
        recruitmentApplicationDTO2.setId(2L);
        assertThat(recruitmentApplicationDTO1).isNotEqualTo(recruitmentApplicationDTO2);
        recruitmentApplicationDTO1.setId(null);
        assertThat(recruitmentApplicationDTO1).isNotEqualTo(recruitmentApplicationDTO2);
    }
}
