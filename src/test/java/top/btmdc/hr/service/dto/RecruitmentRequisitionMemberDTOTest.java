package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentRequisitionMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionMemberDTO.class);
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO1 = new RecruitmentRequisitionMemberDTO();
        recruitmentRequisitionMemberDTO1.setId(1L);
        RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO2 = new RecruitmentRequisitionMemberDTO();
        assertThat(recruitmentRequisitionMemberDTO1).isNotEqualTo(recruitmentRequisitionMemberDTO2);
        recruitmentRequisitionMemberDTO2.setId(recruitmentRequisitionMemberDTO1.getId());
        assertThat(recruitmentRequisitionMemberDTO1).isEqualTo(recruitmentRequisitionMemberDTO2);
        recruitmentRequisitionMemberDTO2.setId(2L);
        assertThat(recruitmentRequisitionMemberDTO1).isNotEqualTo(recruitmentRequisitionMemberDTO2);
        recruitmentRequisitionMemberDTO1.setId(null);
        assertThat(recruitmentRequisitionMemberDTO1).isNotEqualTo(recruitmentRequisitionMemberDTO2);
    }
}
