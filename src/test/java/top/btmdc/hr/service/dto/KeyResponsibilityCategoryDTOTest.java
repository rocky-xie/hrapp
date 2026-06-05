package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class KeyResponsibilityCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyResponsibilityCategoryDTO.class);
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO1 = new KeyResponsibilityCategoryDTO();
        keyResponsibilityCategoryDTO1.setId(1L);
        KeyResponsibilityCategoryDTO keyResponsibilityCategoryDTO2 = new KeyResponsibilityCategoryDTO();
        assertThat(keyResponsibilityCategoryDTO1).isNotEqualTo(keyResponsibilityCategoryDTO2);
        keyResponsibilityCategoryDTO2.setId(keyResponsibilityCategoryDTO1.getId());
        assertThat(keyResponsibilityCategoryDTO1).isEqualTo(keyResponsibilityCategoryDTO2);
        keyResponsibilityCategoryDTO2.setId(2L);
        assertThat(keyResponsibilityCategoryDTO1).isNotEqualTo(keyResponsibilityCategoryDTO2);
        keyResponsibilityCategoryDTO1.setId(null);
        assertThat(keyResponsibilityCategoryDTO1).isNotEqualTo(keyResponsibilityCategoryDTO2);
    }
}
