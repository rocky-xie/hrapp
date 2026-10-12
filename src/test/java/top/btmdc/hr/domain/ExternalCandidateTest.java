package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.CandidateResumeTestSamples.*;
import static top.btmdc.hr.domain.ExternalCandidateTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ExternalCandidateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalCandidate.class);
        ExternalCandidate externalCandidate1 = getExternalCandidateSample1();
        ExternalCandidate externalCandidate2 = new ExternalCandidate();
        assertThat(externalCandidate1).isNotEqualTo(externalCandidate2);

        externalCandidate2.setId(externalCandidate1.getId());
        assertThat(externalCandidate1).isEqualTo(externalCandidate2);

        externalCandidate2 = getExternalCandidateSample2();
        assertThat(externalCandidate1).isNotEqualTo(externalCandidate2);
    }

    @Test
    void resumesTest() {
        ExternalCandidate externalCandidate = getExternalCandidateRandomSampleGenerator();
        CandidateResume candidateResumeBack = getCandidateResumeRandomSampleGenerator();

        externalCandidate.addResumes(candidateResumeBack);
        assertThat(externalCandidate.getResumeses()).containsOnly(candidateResumeBack);
        assertThat(candidateResumeBack.getCandidate()).isEqualTo(externalCandidate);

        externalCandidate.removeResumes(candidateResumeBack);
        assertThat(externalCandidate.getResumeses()).doesNotContain(candidateResumeBack);
        assertThat(candidateResumeBack.getCandidate()).isNull();

        externalCandidate.resumeses(new HashSet<>(Set.of(candidateResumeBack)));
        assertThat(externalCandidate.getResumeses()).containsOnly(candidateResumeBack);
        assertThat(candidateResumeBack.getCandidate()).isEqualTo(externalCandidate);

        externalCandidate.setResumeses(new HashSet<>());
        assertThat(externalCandidate.getResumeses()).doesNotContain(candidateResumeBack);
        assertThat(candidateResumeBack.getCandidate()).isNull();
    }

    @Test
    void applicationsTest() {
        ExternalCandidate externalCandidate = getExternalCandidateRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        externalCandidate.addApplications(recruitmentApplicationBack);
        assertThat(externalCandidate.getApplicationses()).containsOnly(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getCandidate()).isEqualTo(externalCandidate);

        externalCandidate.removeApplications(recruitmentApplicationBack);
        assertThat(externalCandidate.getApplicationses()).doesNotContain(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getCandidate()).isNull();

        externalCandidate.applicationses(new HashSet<>(Set.of(recruitmentApplicationBack)));
        assertThat(externalCandidate.getApplicationses()).containsOnly(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getCandidate()).isEqualTo(externalCandidate);

        externalCandidate.setApplicationses(new HashSet<>());
        assertThat(externalCandidate.getApplicationses()).doesNotContain(recruitmentApplicationBack);
        assertThat(recruitmentApplicationBack.getCandidate()).isNull();
    }
}
