package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.SkillTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Skill.class);
        Skill skill1 = getSkillSample1();
        Skill skill2 = new Skill();
        assertThat(skill1).isNotEqualTo(skill2);

        skill2.setId(skill1.getId());
        assertThat(skill1).isEqualTo(skill2);

        skill2 = getSkillSample2();
        assertThat(skill1).isNotEqualTo(skill2);
    }
}
