package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.EvaluationTestSamples.*;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.TrainingGoalTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class EvaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluation.class);
        Evaluation evaluation1 = getEvaluationSample1();
        Evaluation evaluation2 = new Evaluation();
        assertThat(evaluation1).isNotEqualTo(evaluation2);

        evaluation2.setId(evaluation1.getId());
        assertThat(evaluation1).isEqualTo(evaluation2);

        evaluation2 = getEvaluationSample2();
        assertThat(evaluation1).isNotEqualTo(evaluation2);
    }

    @Test
    void personTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        evaluation.setPerson(personBack);
        assertThat(evaluation.getPerson()).isEqualTo(personBack);

        evaluation.person(null);
        assertThat(evaluation.getPerson()).isNull();
    }

    @Test
    void positionTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        evaluation.setPosition(positionBack);
        assertThat(evaluation.getPosition()).isEqualTo(positionBack);

        evaluation.position(null);
        assertThat(evaluation.getPosition()).isNull();
    }

    @Test
    void trainingGoalTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        TrainingGoal trainingGoalBack = getTrainingGoalRandomSampleGenerator();

        evaluation.setTrainingGoal(trainingGoalBack);
        assertThat(evaluation.getTrainingGoal()).isEqualTo(trainingGoalBack);

        evaluation.trainingGoal(null);
        assertThat(evaluation.getTrainingGoal()).isNull();
    }

    @Test
    void evaluatorTest() {
        Evaluation evaluation = getEvaluationRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        evaluation.setEvaluator(personBack);
        assertThat(evaluation.getEvaluator()).isEqualTo(personBack);

        evaluation.evaluator(null);
        assertThat(evaluation.getEvaluator()).isNull();
    }
}
