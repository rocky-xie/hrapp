package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;
import static top.btmdc.hr.domain.SkillTestSamples.*;
import static top.btmdc.hr.domain.SkillUpgradeRecordTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SkillUpgradeRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillUpgradeRecord.class);
        SkillUpgradeRecord skillUpgradeRecord1 = getSkillUpgradeRecordSample1();
        SkillUpgradeRecord skillUpgradeRecord2 = new SkillUpgradeRecord();
        assertThat(skillUpgradeRecord1).isNotEqualTo(skillUpgradeRecord2);

        skillUpgradeRecord2.setId(skillUpgradeRecord1.getId());
        assertThat(skillUpgradeRecord1).isEqualTo(skillUpgradeRecord2);

        skillUpgradeRecord2 = getSkillUpgradeRecordSample2();
        assertThat(skillUpgradeRecord1).isNotEqualTo(skillUpgradeRecord2);
    }

    @Test
    void personTest() {
        SkillUpgradeRecord skillUpgradeRecord = getSkillUpgradeRecordRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        skillUpgradeRecord.setPerson(personBack);
        assertThat(skillUpgradeRecord.getPerson()).isEqualTo(personBack);

        skillUpgradeRecord.person(null);
        assertThat(skillUpgradeRecord.getPerson()).isNull();
    }

    @Test
    void skillTest() {
        SkillUpgradeRecord skillUpgradeRecord = getSkillUpgradeRecordRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        skillUpgradeRecord.setSkill(skillBack);
        assertThat(skillUpgradeRecord.getSkill()).isEqualTo(skillBack);

        skillUpgradeRecord.skill(null);
        assertThat(skillUpgradeRecord.getSkill()).isNull();
    }

    @Test
    void oldLevelTest() {
        SkillUpgradeRecord skillUpgradeRecord = getSkillUpgradeRecordRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        skillUpgradeRecord.setOldLevel(skillLevelBack);
        assertThat(skillUpgradeRecord.getOldLevel()).isEqualTo(skillLevelBack);

        skillUpgradeRecord.oldLevel(null);
        assertThat(skillUpgradeRecord.getOldLevel()).isNull();
    }

    @Test
    void newLevelTest() {
        SkillUpgradeRecord skillUpgradeRecord = getSkillUpgradeRecordRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        skillUpgradeRecord.setNewLevel(skillLevelBack);
        assertThat(skillUpgradeRecord.getNewLevel()).isEqualTo(skillLevelBack);

        skillUpgradeRecord.newLevel(null);
        assertThat(skillUpgradeRecord.getNewLevel()).isNull();
    }

    @Test
    void assessorTest() {
        SkillUpgradeRecord skillUpgradeRecord = getSkillUpgradeRecordRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        skillUpgradeRecord.setAssessor(personBack);
        assertThat(skillUpgradeRecord.getAssessor()).isEqualTo(personBack);

        skillUpgradeRecord.assessor(null);
        assertThat(skillUpgradeRecord.getAssessor()).isNull();
    }
}
