package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionAssignmentTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionAssignment.class);
        PositionAssignment positionAssignment1 = getPositionAssignmentSample1();
        PositionAssignment positionAssignment2 = new PositionAssignment();
        assertThat(positionAssignment1).isNotEqualTo(positionAssignment2);

        positionAssignment2.setId(positionAssignment1.getId());
        assertThat(positionAssignment1).isEqualTo(positionAssignment2);

        positionAssignment2 = getPositionAssignmentSample2();
        assertThat(positionAssignment1).isNotEqualTo(positionAssignment2);
    }

    @Test
    void personTest() {
        PositionAssignment positionAssignment = getPositionAssignmentRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        positionAssignment.setPerson(personBack);
        assertThat(positionAssignment.getPerson()).isEqualTo(personBack);

        positionAssignment.person(null);
        assertThat(positionAssignment.getPerson()).isNull();
    }

    @Test
    void positionTest() {
        PositionAssignment positionAssignment = getPositionAssignmentRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        positionAssignment.setPosition(positionBack);
        assertThat(positionAssignment.getPosition()).isEqualTo(positionBack);

        positionAssignment.position(null);
        assertThat(positionAssignment.getPosition()).isNull();
    }
}
