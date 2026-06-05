package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class StaffSubstitutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaffSubstitutionDTO.class);
        StaffSubstitutionDTO staffSubstitutionDTO1 = new StaffSubstitutionDTO();
        staffSubstitutionDTO1.setId(1L);
        StaffSubstitutionDTO staffSubstitutionDTO2 = new StaffSubstitutionDTO();
        assertThat(staffSubstitutionDTO1).isNotEqualTo(staffSubstitutionDTO2);
        staffSubstitutionDTO2.setId(staffSubstitutionDTO1.getId());
        assertThat(staffSubstitutionDTO1).isEqualTo(staffSubstitutionDTO2);
        staffSubstitutionDTO2.setId(2L);
        assertThat(staffSubstitutionDTO1).isNotEqualTo(staffSubstitutionDTO2);
        staffSubstitutionDTO1.setId(null);
        assertThat(staffSubstitutionDTO1).isNotEqualTo(staffSubstitutionDTO2);
    }
}
