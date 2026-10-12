package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class DispatchAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DispatchAssignmentDTO.class);
        DispatchAssignmentDTO dispatchAssignmentDTO1 = new DispatchAssignmentDTO();
        dispatchAssignmentDTO1.setId(1L);
        DispatchAssignmentDTO dispatchAssignmentDTO2 = new DispatchAssignmentDTO();
        assertThat(dispatchAssignmentDTO1).isNotEqualTo(dispatchAssignmentDTO2);
        dispatchAssignmentDTO2.setId(dispatchAssignmentDTO1.getId());
        assertThat(dispatchAssignmentDTO1).isEqualTo(dispatchAssignmentDTO2);
        dispatchAssignmentDTO2.setId(2L);
        assertThat(dispatchAssignmentDTO1).isNotEqualTo(dispatchAssignmentDTO2);
        dispatchAssignmentDTO1.setId(null);
        assertThat(dispatchAssignmentDTO1).isNotEqualTo(dispatchAssignmentDTO2);
    }
}
