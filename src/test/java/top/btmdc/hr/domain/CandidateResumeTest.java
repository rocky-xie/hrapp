package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.CandidateResumeTestSamples.*;
import static top.btmdc.hr.domain.ExternalCandidateTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class CandidateResumeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateResume.class);
        CandidateResume candidateResume1 = getCandidateResumeSample1();
        CandidateResume candidateResume2 = new CandidateResume();
        assertThat(candidateResume1).isNotEqualTo(candidateResume2);

        candidateResume2.setId(candidateResume1.getId());
        assertThat(candidateResume1).isEqualTo(candidateResume2);

        candidateResume2 = getCandidateResumeSample2();
        assertThat(candidateResume1).isNotEqualTo(candidateResume2);
    }

    @Test
    void candidateTest() {
        CandidateResume candidateResume = getCandidateResumeRandomSampleGenerator();
        ExternalCandidate externalCandidateBack = getExternalCandidateRandomSampleGenerator();

        candidateResume.setCandidate(externalCandidateBack);
        assertThat(candidateResume.getCandidate()).isEqualTo(externalCandidateBack);

        candidateResume.candidate(null);
        assertThat(candidateResume.getCandidate()).isNull();
    }
}
