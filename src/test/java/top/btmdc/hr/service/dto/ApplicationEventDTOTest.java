package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ApplicationEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationEventDTO.class);
        ApplicationEventDTO applicationEventDTO1 = new ApplicationEventDTO();
        applicationEventDTO1.setId(1L);
        ApplicationEventDTO applicationEventDTO2 = new ApplicationEventDTO();
        assertThat(applicationEventDTO1).isNotEqualTo(applicationEventDTO2);
        applicationEventDTO2.setId(applicationEventDTO1.getId());
        assertThat(applicationEventDTO1).isEqualTo(applicationEventDTO2);
        applicationEventDTO2.setId(2L);
        assertThat(applicationEventDTO1).isNotEqualTo(applicationEventDTO2);
        applicationEventDTO1.setId(null);
        assertThat(applicationEventDTO1).isNotEqualTo(applicationEventDTO2);
    }
}
