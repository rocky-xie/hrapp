package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.HiringDecisionTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class HiringDecisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiringDecision.class);
        HiringDecision hiringDecision1 = getHiringDecisionSample1();
        HiringDecision hiringDecision2 = new HiringDecision();
        assertThat(hiringDecision1).isNotEqualTo(hiringDecision2);

        hiringDecision2.setId(hiringDecision1.getId());
        assertThat(hiringDecision1).isEqualTo(hiringDecision2);

        hiringDecision2 = getHiringDecisionSample2();
        assertThat(hiringDecision1).isNotEqualTo(hiringDecision2);
    }

    @Test
    void applicationTest() {
        HiringDecision hiringDecision = getHiringDecisionRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        hiringDecision.setApplication(recruitmentApplicationBack);
        assertThat(hiringDecision.getApplication()).isEqualTo(recruitmentApplicationBack);

        hiringDecision.application(null);
        assertThat(hiringDecision.getApplication()).isNull();
    }
}
