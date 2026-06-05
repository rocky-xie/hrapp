package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;
import static top.btmdc.hr.domain.SkillTestSamples.*;
import static top.btmdc.hr.domain.TrainingGoalTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class TrainingGoalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingGoal.class);
        TrainingGoal trainingGoal1 = getTrainingGoalSample1();
        TrainingGoal trainingGoal2 = new TrainingGoal();
        assertThat(trainingGoal1).isNotEqualTo(trainingGoal2);

        trainingGoal2.setId(trainingGoal1.getId());
        assertThat(trainingGoal1).isEqualTo(trainingGoal2);

        trainingGoal2 = getTrainingGoalSample2();
        assertThat(trainingGoal1).isNotEqualTo(trainingGoal2);
    }

    @Test
    void personTest() {
        TrainingGoal trainingGoal = getTrainingGoalRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        trainingGoal.setPerson(personBack);
        assertThat(trainingGoal.getPerson()).isEqualTo(personBack);

        trainingGoal.person(null);
        assertThat(trainingGoal.getPerson()).isNull();
    }

    @Test
    void positionTest() {
        TrainingGoal trainingGoal = getTrainingGoalRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        trainingGoal.setPosition(positionBack);
        assertThat(trainingGoal.getPosition()).isEqualTo(positionBack);

        trainingGoal.position(null);
        assertThat(trainingGoal.getPosition()).isNull();
    }

    @Test
    void skillTest() {
        TrainingGoal trainingGoal = getTrainingGoalRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        trainingGoal.setSkill(skillBack);
        assertThat(trainingGoal.getSkill()).isEqualTo(skillBack);

        trainingGoal.skill(null);
        assertThat(trainingGoal.getSkill()).isNull();
    }

    @Test
    void targetLevelTest() {
        TrainingGoal trainingGoal = getTrainingGoalRandomSampleGenerator();
        SkillLevel skillLevelBack = getSkillLevelRandomSampleGenerator();

        trainingGoal.setTargetLevel(skillLevelBack);
        assertThat(trainingGoal.getTargetLevel()).isEqualTo(skillLevelBack);

        trainingGoal.targetLevel(null);
        assertThat(trainingGoal.getTargetLevel()).isNull();
    }
}
