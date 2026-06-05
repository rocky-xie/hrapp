package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PositionRiskAsserts.*;
import static top.btmdc.hr.domain.PositionRiskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionRiskMapperTest {

    private PositionRiskMapper positionRiskMapper;

    @BeforeEach
    void setUp() {
        positionRiskMapper = new PositionRiskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPositionRiskSample1();
        var actual = positionRiskMapper.toEntity(positionRiskMapper.toDto(expected));
        assertPositionRiskAllPropertiesEquals(expected, actual);
    }
}
