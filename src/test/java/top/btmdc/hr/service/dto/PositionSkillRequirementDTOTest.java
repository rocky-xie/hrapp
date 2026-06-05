package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionSkillRequirementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionSkillRequirementDTO.class);
        PositionSkillRequirementDTO positionSkillRequirementDTO1 = new PositionSkillRequirementDTO();
        positionSkillRequirementDTO1.setId(1L);
        PositionSkillRequirementDTO positionSkillRequirementDTO2 = new PositionSkillRequirementDTO();
        assertThat(positionSkillRequirementDTO1).isNotEqualTo(positionSkillRequirementDTO2);
        positionSkillRequirementDTO2.setId(positionSkillRequirementDTO1.getId());
        assertThat(positionSkillRequirementDTO1).isEqualTo(positionSkillRequirementDTO2);
        positionSkillRequirementDTO2.setId(2L);
        assertThat(positionSkillRequirementDTO1).isNotEqualTo(positionSkillRequirementDTO2);
        positionSkillRequirementDTO1.setId(null);
        assertThat(positionSkillRequirementDTO1).isNotEqualTo(positionSkillRequirementDTO2);
    }
}
