package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ImprovementPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImprovementPlanDTO.class);
        ImprovementPlanDTO improvementPlanDTO1 = new ImprovementPlanDTO();
        improvementPlanDTO1.setId(1L);
        ImprovementPlanDTO improvementPlanDTO2 = new ImprovementPlanDTO();
        assertThat(improvementPlanDTO1).isNotEqualTo(improvementPlanDTO2);
        improvementPlanDTO2.setId(improvementPlanDTO1.getId());
        assertThat(improvementPlanDTO1).isEqualTo(improvementPlanDTO2);
        improvementPlanDTO2.setId(2L);
        assertThat(improvementPlanDTO1).isNotEqualTo(improvementPlanDTO2);
        improvementPlanDTO1.setId(null);
        assertThat(improvementPlanDTO1).isNotEqualTo(improvementPlanDTO2);
    }
}
