package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.SuccessionCandidateTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SuccessionCandidateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuccessionCandidate.class);
        SuccessionCandidate successionCandidate1 = getSuccessionCandidateSample1();
        SuccessionCandidate successionCandidate2 = new SuccessionCandidate();
        assertThat(successionCandidate1).isNotEqualTo(successionCandidate2);

        successionCandidate2.setId(successionCandidate1.getId());
        assertThat(successionCandidate1).isEqualTo(successionCandidate2);

        successionCandidate2 = getSuccessionCandidateSample2();
        assertThat(successionCandidate1).isNotEqualTo(successionCandidate2);
    }

    @Test
    void positionTest() {
        SuccessionCandidate successionCandidate = getSuccessionCandidateRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        successionCandidate.setPosition(positionBack);
        assertThat(successionCandidate.getPosition()).isEqualTo(positionBack);

        successionCandidate.position(null);
        assertThat(successionCandidate.getPosition()).isNull();
    }

    @Test
    void currentOwnerTest() {
        SuccessionCandidate successionCandidate = getSuccessionCandidateRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        successionCandidate.setCurrentOwner(personBack);
        assertThat(successionCandidate.getCurrentOwner()).isEqualTo(personBack);

        successionCandidate.currentOwner(null);
        assertThat(successionCandidate.getCurrentOwner()).isNull();
    }

    @Test
    void candidateTest() {
        SuccessionCandidate successionCandidate = getSuccessionCandidateRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        successionCandidate.setCandidate(personBack);
        assertThat(successionCandidate.getCandidate()).isEqualTo(personBack);

        successionCandidate.candidate(null);
        assertThat(successionCandidate.getCandidate()).isNull();
    }
}
