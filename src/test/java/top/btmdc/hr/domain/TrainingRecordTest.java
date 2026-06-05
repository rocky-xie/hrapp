package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.TrainingGoalTestSamples.*;
import static top.btmdc.hr.domain.TrainingRecordTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class TrainingRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingRecord.class);
        TrainingRecord trainingRecord1 = getTrainingRecordSample1();
        TrainingRecord trainingRecord2 = new TrainingRecord();
        assertThat(trainingRecord1).isNotEqualTo(trainingRecord2);

        trainingRecord2.setId(trainingRecord1.getId());
        assertThat(trainingRecord1).isEqualTo(trainingRecord2);

        trainingRecord2 = getTrainingRecordSample2();
        assertThat(trainingRecord1).isNotEqualTo(trainingRecord2);
    }

    @Test
    void personTest() {
        TrainingRecord trainingRecord = getTrainingRecordRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        trainingRecord.setPerson(personBack);
        assertThat(trainingRecord.getPerson()).isEqualTo(personBack);

        trainingRecord.person(null);
        assertThat(trainingRecord.getPerson()).isNull();
    }

    @Test
    void trainingGoalTest() {
        TrainingRecord trainingRecord = getTrainingRecordRandomSampleGenerator();
        TrainingGoal trainingGoalBack = getTrainingGoalRandomSampleGenerator();

        trainingRecord.setTrainingGoal(trainingGoalBack);
        assertThat(trainingRecord.getTrainingGoal()).isEqualTo(trainingGoalBack);

        trainingRecord.trainingGoal(null);
        assertThat(trainingRecord.getTrainingGoal()).isNull();
    }

    @Test
    void positionTest() {
        TrainingRecord trainingRecord = getTrainingRecordRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        trainingRecord.setPosition(positionBack);
        assertThat(trainingRecord.getPosition()).isEqualTo(positionBack);

        trainingRecord.position(null);
        assertThat(trainingRecord.getPosition()).isNull();
    }

    @Test
    void mentorTest() {
        TrainingRecord trainingRecord = getTrainingRecordRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        trainingRecord.setMentor(personBack);
        assertThat(trainingRecord.getMentor()).isEqualTo(personBack);

        trainingRecord.mentor(null);
        assertThat(trainingRecord.getMentor()).isNull();
    }
}
