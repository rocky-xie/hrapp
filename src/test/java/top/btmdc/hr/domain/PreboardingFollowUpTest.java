package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PreboardingFollowUpTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PreboardingFollowUpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreboardingFollowUp.class);
        PreboardingFollowUp preboardingFollowUp1 = getPreboardingFollowUpSample1();
        PreboardingFollowUp preboardingFollowUp2 = new PreboardingFollowUp();
        assertThat(preboardingFollowUp1).isNotEqualTo(preboardingFollowUp2);

        preboardingFollowUp2.setId(preboardingFollowUp1.getId());
        assertThat(preboardingFollowUp1).isEqualTo(preboardingFollowUp2);

        preboardingFollowUp2 = getPreboardingFollowUpSample2();
        assertThat(preboardingFollowUp1).isNotEqualTo(preboardingFollowUp2);
    }

    @Test
    void applicationTest() {
        PreboardingFollowUp preboardingFollowUp = getPreboardingFollowUpRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        preboardingFollowUp.setApplication(recruitmentApplicationBack);
        assertThat(preboardingFollowUp.getApplication()).isEqualTo(recruitmentApplicationBack);

        preboardingFollowUp.application(null);
        assertThat(preboardingFollowUp.getApplication()).isNull();
    }
}
