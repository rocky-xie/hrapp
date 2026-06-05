package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.CandidateProfileTestSamples.*;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class CandidateProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CandidateProfile.class);
        CandidateProfile candidateProfile1 = getCandidateProfileSample1();
        CandidateProfile candidateProfile2 = new CandidateProfile();
        assertThat(candidateProfile1).isNotEqualTo(candidateProfile2);

        candidateProfile2.setId(candidateProfile1.getId());
        assertThat(candidateProfile1).isEqualTo(candidateProfile2);

        candidateProfile2 = getCandidateProfileSample2();
        assertThat(candidateProfile1).isNotEqualTo(candidateProfile2);
    }

    @Test
    void personTest() {
        CandidateProfile candidateProfile = getCandidateProfileRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        candidateProfile.setPerson(personBack);
        assertThat(candidateProfile.getPerson()).isEqualTo(personBack);

        candidateProfile.person(null);
        assertThat(candidateProfile.getPerson()).isNull();
    }

    @Test
    void positionTest() {
        CandidateProfile candidateProfile = getCandidateProfileRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        candidateProfile.setPosition(positionBack);
        assertThat(candidateProfile.getPosition()).isEqualTo(positionBack);

        candidateProfile.position(null);
        assertThat(candidateProfile.getPosition()).isNull();
    }

    @Test
    void observerTest() {
        CandidateProfile candidateProfile = getCandidateProfileRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        candidateProfile.setObserver(personBack);
        assertThat(candidateProfile.getObserver()).isEqualTo(personBack);

        candidateProfile.observer(null);
        assertThat(candidateProfile.getObserver()).isNull();
    }
}
