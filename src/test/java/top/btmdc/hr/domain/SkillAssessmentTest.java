package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.SkillAssessmentTestSamples.*;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;
import static top.btmdc.hr.domain.SkillTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SkillAssessmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillAssessment.class);
        SkillAssessment skillAssessment1 = getSkillAssessmentSample1();
        SkillAssessment skillAssessment2 = new SkillAssessment();
        assertThat(skillAssessment1).isNotEqualTo(skillAssessment2);

        skillAssessment2.setId(skillAssessment1.getId());
        assertThat(skillAssessment1).isEqualTo(skillAssessment2);

        skillAssessment2 = getSkillAssessmentSample2();
        assertThat(skillAssessment1).isNotEqualTo(skillAssessment2);
    }

    @Test
    void personTest() {
        SkillAssessment skillAssessment = getSkillAssessmentRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        skillAssessment.setPerson(personBack);
        assertThat(skillAssessment.getPerson()).isEqualTo(personBack);

        skillAssessment.person(null);
        assertThat(skillAssessment.getPerson()).isNull();
    }

    @Test
    void skillTest() {
        SkillAssessment skillAssessment = getSkillAssessmentRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        skillAssessment.setSkill(skillBack);
        assertThat(skillAssessment.getSkill()).isEqualTo(skillBack);

        skillAssessment.skill(null);
        assertThat(skillAssessment.getSkill()).isNull();
    }

    @Test
    void assessorTest() {
        SkillAssessment skillAssessment = getSkillAssessmentRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        skillAssessment.setAssessor(personBack);
        assertThat(skillAssessment.getAssessor()).isEqualTo(personBack);

        skillAssessment.assessor(null);
        assertThat(skillAssessment.getAssessor()).isNull();
    }

    @Test
    void newLevelTest() {
        SkillAssessment skillAssessment = getSkillAssessmentRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        skillAssessment.setNewLevel(skillLevelBack);
        assertThat(skillAssessment.getNewLevel()).isEqualTo(skillLevelBack);

        skillAssessment.newLevel(null);
        assertThat(skillAssessment.getNewLevel()).isNull();
    }
}
