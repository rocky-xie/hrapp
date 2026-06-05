package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionAssignmentDTO.class);
        PositionAssignmentDTO positionAssignmentDTO1 = new PositionAssignmentDTO();
        positionAssignmentDTO1.setId(1L);
        PositionAssignmentDTO positionAssignmentDTO2 = new PositionAssignmentDTO();
        assertThat(positionAssignmentDTO1).isNotEqualTo(positionAssignmentDTO2);
        positionAssignmentDTO2.setId(positionAssignmentDTO1.getId());
        assertThat(positionAssignmentDTO1).isEqualTo(positionAssignmentDTO2);
        positionAssignmentDTO2.setId(2L);
        assertThat(positionAssignmentDTO1).isNotEqualTo(positionAssignmentDTO2);
        positionAssignmentDTO1.setId(null);
        assertThat(positionAssignmentDTO1).isNotEqualTo(positionAssignmentDTO2);
    }
}
