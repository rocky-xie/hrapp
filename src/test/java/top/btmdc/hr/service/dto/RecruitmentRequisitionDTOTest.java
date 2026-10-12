package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentRequisitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionDTO.class);
        RecruitmentRequisitionDTO recruitmentRequisitionDTO1 = new RecruitmentRequisitionDTO();
        recruitmentRequisitionDTO1.setId(1L);
        RecruitmentRequisitionDTO recruitmentRequisitionDTO2 = new RecruitmentRequisitionDTO();
        assertThat(recruitmentRequisitionDTO1).isNotEqualTo(recruitmentRequisitionDTO2);
        recruitmentRequisitionDTO2.setId(recruitmentRequisitionDTO1.getId());
        assertThat(recruitmentRequisitionDTO1).isEqualTo(recruitmentRequisitionDTO2);
        recruitmentRequisitionDTO2.setId(2L);
        assertThat(recruitmentRequisitionDTO1).isNotEqualTo(recruitmentRequisitionDTO2);
        recruitmentRequisitionDTO1.setId(null);
        assertThat(recruitmentRequisitionDTO1).isNotEqualTo(recruitmentRequisitionDTO2);
    }
}
