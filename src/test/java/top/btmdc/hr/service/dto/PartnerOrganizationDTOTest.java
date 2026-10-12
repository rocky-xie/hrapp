package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PartnerOrganizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerOrganizationDTO.class);
        PartnerOrganizationDTO partnerOrganizationDTO1 = new PartnerOrganizationDTO();
        partnerOrganizationDTO1.setId(1L);
        PartnerOrganizationDTO partnerOrganizationDTO2 = new PartnerOrganizationDTO();
        assertThat(partnerOrganizationDTO1).isNotEqualTo(partnerOrganizationDTO2);
        partnerOrganizationDTO2.setId(partnerOrganizationDTO1.getId());
        assertThat(partnerOrganizationDTO1).isEqualTo(partnerOrganizationDTO2);
        partnerOrganizationDTO2.setId(2L);
        assertThat(partnerOrganizationDTO1).isNotEqualTo(partnerOrganizationDTO2);
        partnerOrganizationDTO1.setId(null);
        assertThat(partnerOrganizationDTO1).isNotEqualTo(partnerOrganizationDTO2);
    }
}
