package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionMatchTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionMatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionMatch.class);
        PositionMatch positionMatch1 = getPositionMatchSample1();
        PositionMatch positionMatch2 = new PositionMatch();
        assertThat(positionMatch1).isNotEqualTo(positionMatch2);

        positionMatch2.setId(positionMatch1.getId());
        assertThat(positionMatch1).isEqualTo(positionMatch2);

        positionMatch2 = getPositionMatchSample2();
        assertThat(positionMatch1).isNotEqualTo(positionMatch2);
    }

    @Test
    void personTest() {
        PositionMatch positionMatch = getPositionMatchRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        positionMatch.setPerson(personBack);
        assertThat(positionMatch.getPerson()).isEqualTo(personBack);

        positionMatch.person(null);
        assertThat(positionMatch.getPerson()).isNull();
    }

    @Test
    void positionTest() {
        PositionMatch positionMatch = getPositionMatchRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        positionMatch.setPosition(positionBack);
        assertThat(positionMatch.getPosition()).isEqualTo(positionBack);

        positionMatch.position(null);
        assertThat(positionMatch.getPosition()).isNull();
    }
}
