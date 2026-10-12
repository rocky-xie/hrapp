package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.ApplicationEventTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ApplicationEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationEvent.class);
        ApplicationEvent applicationEvent1 = getApplicationEventSample1();
        ApplicationEvent applicationEvent2 = new ApplicationEvent();
        assertThat(applicationEvent1).isNotEqualTo(applicationEvent2);

        applicationEvent2.setId(applicationEvent1.getId());
        assertThat(applicationEvent1).isEqualTo(applicationEvent2);

        applicationEvent2 = getApplicationEventSample2();
        assertThat(applicationEvent1).isNotEqualTo(applicationEvent2);
    }

    @Test
    void applicationTest() {
        ApplicationEvent applicationEvent = getApplicationEventRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        applicationEvent.setApplication(recruitmentApplicationBack);
        assertThat(applicationEvent.getApplication()).isEqualTo(recruitmentApplicationBack);

        applicationEvent.application(null);
        assertThat(applicationEvent.getApplication()).isNull();
    }
}
