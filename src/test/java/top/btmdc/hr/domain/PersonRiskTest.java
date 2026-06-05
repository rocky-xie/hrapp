package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonRiskTestSamples.*;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PersonRiskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonRisk.class);
        PersonRisk personRisk1 = getPersonRiskSample1();
        PersonRisk personRisk2 = new PersonRisk();
        assertThat(personRisk1).isNotEqualTo(personRisk2);

        personRisk2.setId(personRisk1.getId());
        assertThat(personRisk1).isEqualTo(personRisk2);

        personRisk2 = getPersonRiskSample2();
        assertThat(personRisk1).isNotEqualTo(personRisk2);
    }

    @Test
    void personTest() {
        PersonRisk personRisk = getPersonRiskRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        personRisk.setPerson(personBack);
        assertThat(personRisk.getPerson()).isEqualTo(personBack);

        personRisk.person(null);
        assertThat(personRisk.getPerson()).isNull();
    }

    @Test
    void positionTest() {
        PersonRisk personRisk = getPersonRiskRandomSampleGenerator();
        Position positionBack = getPositionRandomSampleGenerator();

        personRisk.setPosition(positionBack);
        assertThat(personRisk.getPosition()).isEqualTo(positionBack);

        personRisk.position(null);
        assertThat(personRisk.getPosition()).isNull();
    }
}
