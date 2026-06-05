package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Position.class);
        Position position1 = getPositionSample1();
        Position position2 = new Position();
        assertThat(position1).isNotEqualTo(position2);

        position2.setId(position1.getId());
        assertThat(position1).isEqualTo(position2);

        position2 = getPositionSample2();
        assertThat(position1).isNotEqualTo(position2);
    }
}
