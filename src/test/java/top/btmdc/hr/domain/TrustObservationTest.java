package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PersonTestSamples.*;
import static top.btmdc.hr.domain.TrustObservationTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class TrustObservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrustObservation.class);
        TrustObservation trustObservation1 = getTrustObservationSample1();
        TrustObservation trustObservation2 = new TrustObservation();
        assertThat(trustObservation1).isNotEqualTo(trustObservation2);

        trustObservation2.setId(trustObservation1.getId());
        assertThat(trustObservation1).isEqualTo(trustObservation2);

        trustObservation2 = getTrustObservationSample2();
        assertThat(trustObservation1).isNotEqualTo(trustObservation2);
    }

    @Test
    void personTest() {
        TrustObservation trustObservation = getTrustObservationRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        trustObservation.setPerson(personBack);
        assertThat(trustObservation.getPerson()).isEqualTo(personBack);

        trustObservation.person(null);
        assertThat(trustObservation.getPerson()).isNull();
    }

    @Test
    void observerTest() {
        TrustObservation trustObservation = getTrustObservationRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        trustObservation.setObserver(personBack);
        assertThat(trustObservation.getObserver()).isEqualTo(personBack);

        trustObservation.observer(null);
        assertThat(trustObservation.getObserver()).isNull();
    }
}
