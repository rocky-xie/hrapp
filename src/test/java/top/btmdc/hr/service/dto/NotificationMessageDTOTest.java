package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class NotificationMessageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationMessageDTO.class);
        NotificationMessageDTO notificationMessageDTO1 = new NotificationMessageDTO();
        notificationMessageDTO1.setId(1L);
        NotificationMessageDTO notificationMessageDTO2 = new NotificationMessageDTO();
        assertThat(notificationMessageDTO1).isNotEqualTo(notificationMessageDTO2);
        notificationMessageDTO2.setId(notificationMessageDTO1.getId());
        assertThat(notificationMessageDTO1).isEqualTo(notificationMessageDTO2);
        notificationMessageDTO2.setId(2L);
        assertThat(notificationMessageDTO1).isNotEqualTo(notificationMessageDTO2);
        notificationMessageDTO1.setId(null);
        assertThat(notificationMessageDTO1).isNotEqualTo(notificationMessageDTO2);
    }
}
