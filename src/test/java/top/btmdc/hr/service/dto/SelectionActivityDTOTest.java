package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SelectionActivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SelectionActivityDTO.class);
        SelectionActivityDTO selectionActivityDTO1 = new SelectionActivityDTO();
        selectionActivityDTO1.setId(1L);
        SelectionActivityDTO selectionActivityDTO2 = new SelectionActivityDTO();
        assertThat(selectionActivityDTO1).isNotEqualTo(selectionActivityDTO2);
        selectionActivityDTO2.setId(selectionActivityDTO1.getId());
        assertThat(selectionActivityDTO1).isEqualTo(selectionActivityDTO2);
        selectionActivityDTO2.setId(2L);
        assertThat(selectionActivityDTO1).isNotEqualTo(selectionActivityDTO2);
        selectionActivityDTO1.setId(null);
        assertThat(selectionActivityDTO1).isNotEqualTo(selectionActivityDTO2);
    }
}
