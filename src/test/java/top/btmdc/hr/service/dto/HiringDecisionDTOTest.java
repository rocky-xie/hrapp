package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class HiringDecisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HiringDecisionDTO.class);
        HiringDecisionDTO hiringDecisionDTO1 = new HiringDecisionDTO();
        hiringDecisionDTO1.setId(1L);
        HiringDecisionDTO hiringDecisionDTO2 = new HiringDecisionDTO();
        assertThat(hiringDecisionDTO1).isNotEqualTo(hiringDecisionDTO2);
        hiringDecisionDTO2.setId(hiringDecisionDTO1.getId());
        assertThat(hiringDecisionDTO1).isEqualTo(hiringDecisionDTO2);
        hiringDecisionDTO2.setId(2L);
        assertThat(hiringDecisionDTO1).isNotEqualTo(hiringDecisionDTO2);
        hiringDecisionDTO1.setId(null);
        assertThat(hiringDecisionDTO1).isNotEqualTo(hiringDecisionDTO2);
    }
}
