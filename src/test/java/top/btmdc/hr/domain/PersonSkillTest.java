package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonSkillTestSamples.*;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;
import static top.btmdc.hr.domain.SkillTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PersonSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonSkill.class);
        PersonSkill personSkill1 = getPersonSkillSample1();
        PersonSkill personSkill2 = new PersonSkill();
        assertThat(personSkill1).isNotEqualTo(personSkill2);

        personSkill2.setId(personSkill1.getId());
        assertThat(personSkill1).isEqualTo(personSkill2);

        personSkill2 = getPersonSkillSample2();
        assertThat(personSkill1).isNotEqualTo(personSkill2);
    }

    @Test
    void personTest() {
        PersonSkill personSkill = getPersonSkillRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        personSkill.setPerson(personBack);
        assertThat(personSkill.getPerson()).isEqualTo(personBack);

        personSkill.person(null);
        assertThat(personSkill.getPerson()).isNull();
    }

    @Test
    void skillTest() {
        PersonSkill personSkill = getPersonSkillRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        personSkill.setSkill(skillBack);
        assertThat(personSkill.getSkill()).isEqualTo(skillBack);

        personSkill.skill(null);
        assertThat(personSkill.getSkill()).isNull();
    }

    @Test
    void currentLevelTest() {
        PersonSkill personSkill = getPersonSkillRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        personSkill.setCurrentLevel(skillLevelBack);
        assertThat(personSkill.getCurrentLevel()).isEqualTo(skillLevelBack);

        personSkill.currentLevel(null);
        assertThat(personSkill.getCurrentLevel()).isNull();
    }

    @Test
    void previousLevelTest() {
        PersonSkill personSkill = getPersonSkillRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        personSkill.setPreviousLevel(skillLevelBack);
        assertThat(personSkill.getPreviousLevel()).isEqualTo(skillLevelBack);

        personSkill.previousLevel(null);
        assertThat(personSkill.getPreviousLevel()).isNull();
    }
}
