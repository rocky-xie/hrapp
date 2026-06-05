package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PersonRiskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonRiskDTO.class);
        PersonRiskDTO personRiskDTO1 = new PersonRiskDTO();
        personRiskDTO1.setId(1L);
        PersonRiskDTO personRiskDTO2 = new PersonRiskDTO();
        assertThat(personRiskDTO1).isNotEqualTo(personRiskDTO2);
        personRiskDTO2.setId(personRiskDTO1.getId());
        assertThat(personRiskDTO1).isEqualTo(personRiskDTO2);
        personRiskDTO2.setId(2L);
        assertThat(personRiskDTO1).isNotEqualTo(personRiskDTO2);
        personRiskDTO1.setId(null);
        assertThat(personRiskDTO1).isNotEqualTo(personRiskDTO2);
    }
}
