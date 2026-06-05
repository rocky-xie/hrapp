package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionMatchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionMatchDTO.class);
        PositionMatchDTO positionMatchDTO1 = new PositionMatchDTO();
        positionMatchDTO1.setId(1L);
        PositionMatchDTO positionMatchDTO2 = new PositionMatchDTO();
        assertThat(positionMatchDTO1).isNotEqualTo(positionMatchDTO2);
        positionMatchDTO2.setId(positionMatchDTO1.getId());
        assertThat(positionMatchDTO1).isEqualTo(positionMatchDTO2);
        positionMatchDTO2.setId(2L);
        assertThat(positionMatchDTO1).isNotEqualTo(positionMatchDTO2);
        positionMatchDTO1.setId(null);
        assertThat(positionMatchDTO1).isNotEqualTo(positionMatchDTO2);
    }
}
