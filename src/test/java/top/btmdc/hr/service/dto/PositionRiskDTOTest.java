package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionRiskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionRiskDTO.class);
        PositionRiskDTO positionRiskDTO1 = new PositionRiskDTO();
        positionRiskDTO1.setId(1L);
        PositionRiskDTO positionRiskDTO2 = new PositionRiskDTO();
        assertThat(positionRiskDTO1).isNotEqualTo(positionRiskDTO2);
        positionRiskDTO2.setId(positionRiskDTO1.getId());
        assertThat(positionRiskDTO1).isEqualTo(positionRiskDTO2);
        positionRiskDTO2.setId(2L);
        assertThat(positionRiskDTO1).isNotEqualTo(positionRiskDTO2);
        positionRiskDTO1.setId(null);
        assertThat(positionRiskDTO1).isNotEqualTo(positionRiskDTO2);
    }
}
