package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.KeyResponsibilityCategoryTestSamples.*;
import static top.btmdc.hr.domain.PositionRiskTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionRiskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionRisk.class);
        PositionRisk positionRisk1 = getPositionRiskSample1();
        PositionRisk positionRisk2 = new PositionRisk();
        assertThat(positionRisk1).isNotEqualTo(positionRisk2);

        positionRisk2.setId(positionRisk1.getId());
        assertThat(positionRisk1).isEqualTo(positionRisk2);

        positionRisk2 = getPositionRiskSample2();
        assertThat(positionRisk1).isNotEqualTo(positionRisk2);
    }

    @Test
    void positionTest() {
        PositionRisk positionRisk = getPositionRiskRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        positionRisk.setPosition(positionBack);
        assertThat(positionRisk.getPosition()).isEqualTo(positionBack);

        positionRisk.position(null);
        assertThat(positionRisk.getPosition()).isNull();
    }

    @Test
    void categoryTest() {
        PositionRisk positionRisk = getPositionRiskRandomSampleGenerator();
        KeyResponsibilityCategory keyResponsibilityCategoryBack = getKeyResponsibilityCategoryRandomSampleGenerator();

        positionRisk.setCategory(keyResponsibilityCategoryBack);
        assertThat(positionRisk.getCategory()).isEqualTo(keyResponsibilityCategoryBack);

        positionRisk.category(null);
        assertThat(positionRisk.getCategory()).isNull();
    }
}
