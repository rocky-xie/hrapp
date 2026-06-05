package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PositionSkillRequirementTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;
import static top.btmdc.hr.domain.SkillTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionSkillRequirementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionSkillRequirement.class);
        PositionSkillRequirement positionSkillRequirement1 = getPositionSkillRequirementSample1();
        PositionSkillRequirement positionSkillRequirement2 = new PositionSkillRequirement();
        assertThat(positionSkillRequirement1).isNotEqualTo(positionSkillRequirement2);

        positionSkillRequirement2.setId(positionSkillRequirement1.getId());
        assertThat(positionSkillRequirement1).isEqualTo(positionSkillRequirement2);

        positionSkillRequirement2 = getPositionSkillRequirementSample2();
        assertThat(positionSkillRequirement1).isNotEqualTo(positionSkillRequirement2);
    }

    @Test
    void positionTest() {
        PositionSkillRequirement positionSkillRequirement = getPositionSkillRequirementRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        positionSkillRequirement.setPosition(positionBack);
        assertThat(positionSkillRequirement.getPosition()).isEqualTo(positionBack);

        positionSkillRequirement.position(null);
        assertThat(positionSkillRequirement.getPosition()).isNull();
    }

    @Test
    void skillTest() {
        PositionSkillRequirement positionSkillRequirement = getPositionSkillRequirementRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        positionSkillRequirement.setSkill(skillBack);
        assertThat(positionSkillRequirement.getSkill()).isEqualTo(skillBack);

        positionSkillRequirement.skill(null);
        assertThat(positionSkillRequirement.getSkill()).isNull();
    }

    @Test
    void requiredLevelTest() {
        PositionSkillRequirement positionSkillRequirement = getPositionSkillRequirementRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        positionSkillRequirement.setRequiredLevel(skillLevelBack);
        assertThat(positionSkillRequirement.getRequiredLevel()).isEqualTo(skillLevelBack);

        positionSkillRequirement.requiredLevel(null);
        assertThat(positionSkillRequirement.getRequiredLevel()).isNull();
    }

    @Test
    void preferredLevelTest() {
        PositionSkillRequirement positionSkillRequirement = getPositionSkillRequirementRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        positionSkillRequirement.setPreferredLevel(skillLevelBack);
        assertThat(positionSkillRequirement.getPreferredLevel()).isEqualTo(skillLevelBack);

        positionSkillRequirement.preferredLevel(null);
        assertThat(positionSkillRequirement.getPreferredLevel()).isNull();
    }
}
