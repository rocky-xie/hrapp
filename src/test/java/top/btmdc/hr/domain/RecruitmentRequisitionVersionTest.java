package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionVersionTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class RecruitmentRequisitionVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentRequisitionVersion.class);
        RecruitmentRequisitionVersion recruitmentRequisitionVersion1 = getRecruitmentRequisitionVersionSample1();
        RecruitmentRequisitionVersion recruitmentRequisitionVersion2 = new RecruitmentRequisitionVersion();
        assertThat(recruitmentRequisitionVersion1).isNotEqualTo(recruitmentRequisitionVersion2);

        recruitmentRequisitionVersion2.setId(recruitmentRequisitionVersion1.getId());
        assertThat(recruitmentRequisitionVersion1).isEqualTo(recruitmentRequisitionVersion2);

        recruitmentRequisitionVersion2 = getRecruitmentRequisitionVersionSample2();
        assertThat(recruitmentRequisitionVersion1).isNotEqualTo(recruitmentRequisitionVersion2);
    }

    @Test
    void applicationsTest() {
        RecruitmentRequisitionVersion recruitmentRequisitionVersion = getRecruitmentRequisitionVersionRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        recruitmentRequisitionVersion.addApplications(recruitmentApplicationBack);
        assertThat(recruitmentRequisitionVersion.getApplicationses()).containsOnly(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisitionVersion()).isEqualTo(recruitmentRequisitionVersion);

        recruitmentRequisitionVersion.removeApplications(recruitmentApplicationBack);
        assertThat(recruitmentRequisitionVersion.getApplicationses()).doesNotContain(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisitionVersion()).isNull();

        recruitmentRequisitionVersion.applicationses(new HashSet<>(Set.of(recruitmentApplicationBack)));
        assertThat(recruitmentRequisitionVersion.getApplicationses()).containsOnly(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisitionVersion()).isEqualTo(recruitmentRequisitionVersion);

        recruitmentRequisitionVersion.setApplicationses(new HashSet<>());
        assertThat(recruitmentRequisitionVersion.getApplicationses()).doesNotContain(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getRequisitionVersion()).isNull();
    }

    @Test
    void requisitionTest() {
        RecruitmentRequisitionVersion recruitmentRequisitionVersion = getRecruitmentRequisitionVersionRandomSampleGenerator();
        RecruitmentRequisition recruitmentRequisitionBack = getRecruitmentRequisitionRandomSampleGenerator();

        recruitmentRequisitionVersion.setRequisition(recruitmentRequisitionBack);
        assertThat(recruitmentRequisitionVersion.getRequisition()).isEqualTo(recruitmentRequisitionBack);

        recruitmentRequisitionVersion.requisition(null);
        assertThat(recruitmentRequisitionVersion.getRequisition()).isNull();
    }
}
