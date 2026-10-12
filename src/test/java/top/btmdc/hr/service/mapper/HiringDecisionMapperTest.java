package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.HiringDecisionAsserts.*;
import static top.btmdc.hr.domain.HiringDecisionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HiringDecisionMapperTest {

    private HiringDecisionMapper hiringDecisionMapper;

    @BeforeEach
    void setUp() {
        hiringDecisionMapper = new HiringDecisionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHiringDecisionSample1();
        var actual = hiringDecisionMapper.toEntity(hiringDecisionMapper.toDto(expected));
        assertHiringDecisionAllPropertiesEquals(expected, actual);
    }
}
