package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionMemberTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionVersionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisition.class);
        RecruitmentRequisition recruitmentRequisition1 = getRecruitmentRequisitionSample1();
        RecruitmentRequisition recruitmentRequisition2 = new RecruitmentRequisition();
        assertThat(recruitmentRequisition1).isNotEqualTo(recruitmentRequisition2);

        recruitmentRequisition2.setId(recruitmentRequisition1.getId());
        assertThat(recruitmentRequisition1).isEqualTo(recruitmentRequisition2);

        recruitmentRequisition2 = getRecruitmentRequisitionSample2();
        assertThat(recruitmentRequisition1).isNotEqualTo(recruitmentRequisition2);
    }

    @Test
    void versionsTest() {
        RecruitmentRequisition recruitmentRequisition = getRecruitmentRequisitionRandomSampleGenerator();
        RecruitmentRequisitionVersion recruitmentRequisitionVersionBack = getRecruitmentRequisitionVersionRandomSampleGenerator();

        recruitmentRequisition.addVersions(recruitmentRequisitionVersionBack);
        assertThat(recruitmentRequisition.getVersionses()).containsOnly(recruitmentRequisitionVersionBack);
        assertThat(recruitmentRequisitionVersionBack.getRequisition()).isEqualTo(recruitmentRequisition);

        recruitmentRequisition.removeVersions(recruitmentRequisitionVersionBack);
        assertThat(recruitmentRequisition.getVersionses()).doesNotContain(recruitmentRequisitionVersionBack);
        assertThat(recruitmentRequisitionVersionBack.getRequisition()).isNull();

        recruitmentRequisition.versionses(new HashSet<>(Set.of(recruitmentRequisitionVersionBack)));
        assertThat(recruitmentRequisition.getVersionses()).containsOnly(recruitmentRequisitionVersionBack);
        assertThat(recruitmentRequisitionVersionBack.getRequisition()).isEqualTo(recruitmentRequisition);

        recruitmentRequisition.setVersionses(new HashSet<>());
        assertThat(recruitmentRequisition.getVersionses()).doesNotContain(recruitmentRequisitionVersionBack);
        assertThat(recruitmentRequisitionVersionBack.getRequisition()).isNull();
    }

    @Test
    void membersTest() {
        RecruitmentRequisition recruitmentRequisition = getRecruitmentRequisitionRandomSampleGenerator();
        RecruitmentRequisitionMember recruitmentRequisitionMemberBack = getRecruitmentRequisitionMemberRandomSampleGenerator();

        recruitmentRequisition.addMembers(recruitmentRequisitionMemberBack);
        assertThat(recruitmentRequisition.getMemberses()).containsOnly(recruitmentRequisitionMemberBack);
        assertThat(recruitmentRequisitionMemberBack.getRequisition()).isEqualTo(recruitmentRequisition);

        recruitmentRequisition.removeMembers(recruitmentRequisitionMemberBack);
        assertThat(recruitmentRequisition.getMemberses()).doesNotContain(recruitmentRequisitionMemberBack);
        assertThat(recruitmentRequisitionMemberBack.getRequisition()).isNull();

        recruitmentRequisition.memberses(new HashSet<>(Set.of(recruitmentRequisitionMemberBack)));
        assertThat(recruitmentRequisition.getMemberses()).containsOnly(recruitmentRequisitionMemberBack);
        assertThat(recruitmentRequisitionMemberBack.getRequisition()).isEqualTo(recruitmentRequisition);

        recruitmentRequisition.setMemberses(new HashSet<>());
        assertThat(recruitmentRequisition.getMemberses()).doesNotContain(recruitmentRequisitionMemberBack);
        assertThat(recruitmentRequisitionMemberBack.getRequisition()).isNull();
    }

    @Test
    void applicationsTest() {
        RecruitmentRequisition recruitmentRequisition = getRecruitmentRequisitionRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        recruitmentRequisition.addApplications(recruitmentApplicationBack);
        assertThat(recruitmentRequisition.getApplicationses()).containsOnly(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisition()).isEqualTo(recruitmentRequisition);

        recruitmentRequisition.removeApplications(recruitmentApplicationBack);
        assertThat(recruitmentRequisition.getApplicationses()).doesNotContain(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisition()).isNull();

        recruitmentRequisition.applicationses(new HashSet<>(Set.of(recruitmentApplicationBack)));
        assertThat(recruitmentRequisition.getApplicationses()).containsOnly(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisition()).isEqualTo(recruitmentRequisition);

        recruitmentRequisition.setApplicationses(new HashSet<>());
        assertThat(recruitmentRequisition.getApplicationses()).doesNotContain(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisition()).isNull();
    }
}
