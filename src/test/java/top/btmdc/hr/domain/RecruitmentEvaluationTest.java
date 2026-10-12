package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentEvaluationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentEvaluationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentEvaluation.class);
        RecruitmentEvaluation recruitmentEvaluation1 = getRecruitmentEvaluationSample1();
        RecruitmentEvaluation recruitmentEvaluation2 = new RecruitmentEvaluation();
        assertThat(recruitmentEvaluation1).isNotEqualTo(recruitmentEvaluation2);

        recruitmentEvaluation2.setId(recruitmentEvaluation1.getId());
        assertThat(recruitmentEvaluation1).isEqualTo(recruitmentEvaluation2);

        recruitmentEvaluation2 = getRecruitmentEvaluationSample2();
        assertThat(recruitmentEvaluation1).isNotEqualTo(recruitmentEvaluation2);
    }

    @Test
    void applicationTest() {
        RecruitmentEvaluation recruitmentEvaluation = getRecruitmentEvaluationRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        recruitmentEvaluation.setApplication(recruitmentApplicationBack);
        assertThat(recruitmentEvaluation.getApplication()).isEqualTo(recruitmentApplicationBack);

        recruitmentEvaluation.application(null);
        assertThat(recruitmentEvaluation.getApplication()).isNull();
    }
}
