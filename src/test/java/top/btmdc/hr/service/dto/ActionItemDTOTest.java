package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ActionItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionItemDTO.class);
        ActionItemDTO actionItemDTO1 = new ActionItemDTO();
        actionItemDTO1.setId(1L);
        ActionItemDTO actionItemDTO2 = new ActionItemDTO();
        assertThat(actionItemDTO1).isNotEqualTo(actionItemDTO2);
        actionItemDTO2.setId(actionItemDTO1.getId());
        assertThat(actionItemDTO1).isEqualTo(actionItemDTO2);
        actionItemDTO2.setId(2L);
        assertThat(actionItemDTO1).isNotEqualTo(actionItemDTO2);
        actionItemDTO1.setId(null);
        assertThat(actionItemDTO1).isNotEqualTo(actionItemDTO2);
    }
}
