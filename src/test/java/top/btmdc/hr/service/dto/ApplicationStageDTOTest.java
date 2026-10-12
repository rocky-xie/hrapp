package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ApplicationStageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationStageDTO.class);
        ApplicationStageDTO applicationStageDTO1 = new ApplicationStageDTO();
        applicationStageDTO1.setId(1L);
        ApplicationStageDTO applicationStageDTO2 = new ApplicationStageDTO();
        assertThat(applicationStageDTO1).isNotEqualTo(applicationStageDTO2);
        applicationStageDTO2.setId(applicationStageDTO1.getId());
        assertThat(applicationStageDTO1).isEqualTo(applicationStageDTO2);
        applicationStageDTO2.setId(2L);
        assertThat(applicationStageDTO1).isNotEqualTo(applicationStageDTO2);
        applicationStageDTO1.setId(null);
        assertThat(applicationStageDTO1).isNotEqualTo(applicationStageDTO2);
    }
}
