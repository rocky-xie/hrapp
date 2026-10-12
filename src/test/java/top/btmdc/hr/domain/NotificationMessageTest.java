package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.NotificationMessageTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class NotificationMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationMessage.class);
        NotificationMessage notificationMessage1 = getNotificationMessageSample1();
        NotificationMessage notificationMessage2 = new NotificationMessage();
        assertThat(notificationMessage1).isNotEqualTo(notificationMessage2);

        notificationMessage2.setId(notificationMessage1.getId());
        assertThat(notificationMessage1).isEqualTo(notificationMessage2);

        notificationMessage2 = getNotificationMessageSample2();
        assertThat(notificationMessage1).isNotEqualTo(notificationMessage2);
    }

    @Test
    void applicationTest() {
        NotificationMessage notificationMessage = getNotificationMessageRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        notificationMessage.setApplication(recruitmentApplicationBack);
        assertThat(notificationMessage.getApplication()).isEqualTo(recruitmentApplicationBack);

        notificationMessage.application(null);
        assertThat(notificationMessage.getApplication()).isNull();
    }
}
