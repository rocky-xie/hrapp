package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class TrustObservationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrustObservationDTO.class);
        TrustObservationDTO trustObservationDTO1 = new TrustObservationDTO();
        trustObservationDTO1.setId(1L);
        TrustObservationDTO trustObservationDTO2 = new TrustObservationDTO();
        assertThat(trustObservationDTO1).isNotEqualTo(trustObservationDTO2);
        trustObservationDTO2.setId(trustObservationDTO1.getId());
        assertThat(trustObservationDTO1).isEqualTo(trustObservationDTO2);
        trustObservationDTO2.setId(2L);
        assertThat(trustObservationDTO1).isNotEqualTo(trustObservationDTO2);
        trustObservationDTO1.setId(null);
        assertThat(trustObservationDTO1).isNotEqualTo(trustObservationDTO2);
    }
}
