package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentRequisitionVersionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionVersionDTO.class);
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO1 = new RecruitmentRequisitionVersionDTO();
        recruitmentRequisitionVersionDTO1.setId(1L);
        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO2 = new RecruitmentRequisitionVersionDTO();
        assertThat(recruitmentRequisitionVersionDTO1).isNotEqualTo(recruitmentRequisitionVersionDTO2);
        recruitmentRequisitionVersionDTO2.setId(recruitmentRequisitionVersionDTO1.getId());
        assertThat(recruitmentRequisitionVersionDTO1).isEqualTo(recruitmentRequisitionVersionDTO2);
        recruitmentRequisitionVersionDTO2.setId(2L);
        assertThat(recruitmentRequisitionVersionDTO1).isNotEqualTo(recruitmentRequisitionVersionDTO2);
        recruitmentRequisitionVersionDTO1.setId(null);
        assertThat(recruitmentRequisitionVersionDTO1).isNotEqualTo(recruitmentRequisitionVersionDTO2);
    }
}
