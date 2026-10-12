package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PartnerOrganizationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PartnerOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PartnerOrganization.class);
        PartnerOrganization partnerOrganization1 = getPartnerOrganizationSample1();
        PartnerOrganization partnerOrganization2 = new PartnerOrganization();
        assertThat(partnerOrganization1).isNotEqualTo(partnerOrganization2);

        partnerOrganization2.setId(partnerOrganization1.getId());
        assertThat(partnerOrganization1).isEqualTo(partnerOrganization2);

        partnerOrganization2 = getPartnerOrganizationSample2();
        assertThat(partnerOrganization1).isNotEqualTo(partnerOrganization2);
    }
}
