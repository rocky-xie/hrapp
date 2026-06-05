package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.ImprovementPlanTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.SkillTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ImprovementPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImprovementPlan.class);
        ImprovementPlan improvementPlan1 = getImprovementPlanSample1();
        ImprovementPlan improvementPlan2 = new ImprovementPlan();
        assertThat(improvementPlan1).isNotEqualTo(improvementPlan2);

        improvementPlan2.setId(improvementPlan1.getId());
        assertThat(improvementPlan1).isEqualTo(improvementPlan2);

        improvementPlan2 = getImprovementPlanSample2();
        assertThat(improvementPlan1).isNotEqualTo(improvementPlan2);
    }

    @Test
    void positionTest() {
        ImprovementPlan improvementPlan = getImprovementPlanRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        improvementPlan.setPosition(positionBack);
        assertThat(improvementPlan.getPosition()).isEqualTo(positionBack);

        improvementPlan.position(null);
        assertThat(improvementPlan.getPosition()).isNull();
    }

    @Test
    void skillTest() {
        ImprovementPlan improvementPlan = getImprovementPlanRandomSampleGenerator();
        Skill skillBack = getSkillRandomSampleGenerator();

        improvementPlan.setSkill(skillBack);
        assertThat(improvementPlan.getSkill()).isEqualTo(skillBack);

        improvementPlan.skill(null);
        assertThat(improvementPlan.getSkill()).isNull();
    }
}
