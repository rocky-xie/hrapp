package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SkillLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillLevel.class);
        SkillLevel skillLevel1 = getSkillLevelSample1();
        SkillLevel skillLevel2 = new SkillLevel();
        assertThat(skillLevel1).isNotEqualTo(skillLevel2);

        skillLevel2.setId(skillLevel1.getId());
        assertThat(skillLevel1).isEqualTo(skillLevel2);

        skillLevel2 = getSkillLevelSample2();
        assertThat(skillLevel1).isNotEqualTo(skillLevel2);
    }
}
