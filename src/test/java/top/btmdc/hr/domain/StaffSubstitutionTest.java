package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;
import static top.btmdc.hr.domain.StaffSubstitutionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class StaffSubstitutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaffSubstitution.class);
        StaffSubstitution staffSubstitution1 = getStaffSubstitutionSample1();
        StaffSubstitution staffSubstitution2 = new StaffSubstitution();
        assertThat(staffSubstitution1).isNotEqualTo(staffSubstitution2);

        staffSubstitution2.setId(staffSubstitution1.getId());
        assertThat(staffSubstitution1).isEqualTo(staffSubstitution2);

        staffSubstitution2 = getStaffSubstitutionSample2();
        assertThat(staffSubstitution1).isNotEqualTo(staffSubstitution2);
    }

    @Test
    void positionTest() {
        StaffSubstitution staffSubstitution = getStaffSubstitutionRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        staffSubstitution.setPosition(positionBack);
        assertThat(staffSubstitution.getPosition()).isEqualTo(positionBack);

        staffSubstitution.position(null);
        assertThat(staffSubstitution.getPosition()).isNull();
    }

    @Test
    void candidatePersonTest() {
        StaffSubstitution staffSubstitution = getStaffSubstitutionRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        staffSubstitution.setCandidatePerson(personBack);
        assertThat(staffSubstitution.getCandidatePerson()).isEqualTo(personBack);

        staffSubstitution.candidatePerson(null);
        assertThat(staffSubstitution.getCandidatePerson()).isNull();
    }
}
