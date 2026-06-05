package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PositionRiskEvaluationTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionRiskEvaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionRiskEvaluation.class);
        PositionRiskEvaluation positionRiskEvaluation1 = getPositionRiskEvaluationSample1();
        PositionRiskEvaluation positionRiskEvaluation2 = new PositionRiskEvaluation();
        assertThat(positionRiskEvaluation1).isNotEqualTo(positionRiskEvaluation2);

        positionRiskEvaluation2.setId(positionRiskEvaluation1.getId());
        assertThat(positionRiskEvaluation1).isEqualTo(positionRiskEvaluation2);

        positionRiskEvaluation2 = getPositionRiskEvaluationSample2();
        assertThat(positionRiskEvaluation1).isNotEqualTo(positionRiskEvaluation2);
    }

    @Test
    void positionTest() {
        PositionRiskEvaluation positionRiskEvaluation = getPositionRiskEvaluationRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        positionRiskEvaluation.setPosition(positionBack);
        assertThat(positionRiskEvaluation.getPosition()).isEqualTo(positionBack);

        positionRiskEvaluation.position(null);
        assertThat(positionRiskEvaluation.getPosition()).isNull();
    }
}
