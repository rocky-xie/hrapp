package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.ApplicationStageTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ApplicationStageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationStage.class);
        ApplicationStage applicationStage1 = getApplicationStageSample1();
        ApplicationStage applicationStage2 = new ApplicationStage();
        assertThat(applicationStage1).isNotEqualTo(applicationStage2);

        applicationStage2.setId(applicationStage1.getId());
        assertThat(applicationStage1).isEqualTo(applicationStage2);

        applicationStage2 = getApplicationStageSample2();
        assertThat(applicationStage1).isNotEqualTo(applicationStage2);
    }

    @Test
    void applicationTest() {
        ApplicationStage applicationStage = getApplicationStageRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        applicationStage.setApplication(recruitmentApplicationBack);
        assertThat(applicationStage.getApplication()).isEqualTo(recruitmentApplicationBack);

        applicationStage.application(null);
        assertThat(applicationStage.getApplication()).isNull();
    }
}
