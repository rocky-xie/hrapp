package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentEvaluationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentEvaluationDTO.class);
        RecruitmentEvaluationDTO recruitmentEvaluationDTO1 = new RecruitmentEvaluationDTO();
        recruitmentEvaluationDTO1.setId(1L);
        RecruitmentEvaluationDTO recruitmentEvaluationDTO2 = new RecruitmentEvaluationDTO();
        assertThat(recruitmentEvaluationDTO1).isNotEqualTo(recruitmentEvaluationDTO2);
        recruitmentEvaluationDTO2.setId(recruitmentEvaluationDTO1.getId());
        assertThat(recruitmentEvaluationDTO1).isEqualTo(recruitmentEvaluationDTO2);
        recruitmentEvaluationDTO2.setId(2L);
        assertThat(recruitmentEvaluationDTO1).isNotEqualTo(recruitmentEvaluationDTO2);
        recruitmentEvaluationDTO1.setId(null);
        assertThat(recruitmentEvaluationDTO1).isNotEqualTo(recruitmentEvaluationDTO2);
    }
}
