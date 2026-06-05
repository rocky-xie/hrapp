package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SkillAssessmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillAssessmentDTO.class);
        SkillAssessmentDTO skillAssessmentDTO1 = new SkillAssessmentDTO();
        skillAssessmentDTO1.setId(1L);
        SkillAssessmentDTO skillAssessmentDTO2 = new SkillAssessmentDTO();
        assertThat(skillAssessmentDTO1).isNotEqualTo(skillAssessmentDTO2);
        skillAssessmentDTO2.setId(skillAssessmentDTO1.getId());
        assertThat(skillAssessmentDTO1).isEqualTo(skillAssessmentDTO2);
        skillAssessmentDTO2.setId(2L);
        assertThat(skillAssessmentDTO1).isNotEqualTo(skillAssessmentDTO2);
        skillAssessmentDTO1.setId(null);
        assertThat(skillAssessmentDTO1).isNotEqualTo(skillAssessmentDTO2);
    }
}
