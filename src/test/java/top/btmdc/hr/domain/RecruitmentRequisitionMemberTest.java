package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.RecruitmentRequisitionMemberTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentRequisitionMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionMember.class);
        RecruitmentRequisitionMember recruitmentRequisitionMember1 = getRecruitmentRequisitionMemberSample1();
        RecruitmentRequisitionMember recruitmentRequisitionMember2 = new RecruitmentRequisitionMember();
        assertThat(recruitmentRequisitionMember1).isNotEqualTo(recruitmentRequisitionMember2);

        recruitmentRequisitionMember2.setId(recruitmentRequisitionMember1.getId());
        assertThat(recruitmentRequisitionMember1).isEqualTo(recruitmentRequisitionMember2);

        recruitmentRequisitionMember2 = getRecruitmentRequisitionMemberSample2();
        assertThat(recruitmentRequisitionMember1).isNotEqualTo(recruitmentRequisitionMember2);
    }

    @Test
    void requisitionTest() {
        RecruitmentRequisitionMember recruitmentRequisitionMember = getRecruitmentRequisitionMemberRandomSampleGenerator();
        RecruitmentRequisition recruitmentRequisitionBack = getRecruitmentRequisitionRandomSampleGenerator();

        recruitmentRequisitionMember.setRequisition(recruitmentRequisitionBack);
        assertThat(recruitmentRequisitionMember.getRequisition()).isEqualTo(recruitmentRequisitionBack);

        recruitmentRequisitionMember.requisition(null);
        assertThat(recruitmentRequisitionMember.getRequisition()).isNull();
    }
}
