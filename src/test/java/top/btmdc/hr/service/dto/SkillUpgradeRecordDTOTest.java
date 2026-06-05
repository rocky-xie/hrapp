package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SkillUpgradeRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillUpgradeRecordDTO.class);
        SkillUpgradeRecordDTO skillUpgradeRecordDTO1 = new SkillUpgradeRecordDTO();
        skillUpgradeRecordDTO1.setId(1L);
        SkillUpgradeRecordDTO skillUpgradeRecordDTO2 = new SkillUpgradeRecordDTO();
        assertThat(skillUpgradeRecordDTO1).isNotEqualTo(skillUpgradeRecordDTO2);
        skillUpgradeRecordDTO2.setId(skillUpgradeRecordDTO1.getId());
        assertThat(skillUpgradeRecordDTO1).isEqualTo(skillUpgradeRecordDTO2);
        skillUpgradeRecordDTO2.setId(2L);
        assertThat(skillUpgradeRecordDTO1).isNotEqualTo(skillUpgradeRecordDTO2);
        skillUpgradeRecordDTO1.setId(null);
        assertThat(skillUpgradeRecordDTO1).isNotEqualTo(skillUpgradeRecordDTO2);
    }
}
