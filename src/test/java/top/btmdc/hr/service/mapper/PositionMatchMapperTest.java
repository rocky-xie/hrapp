package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PositionMatchAsserts.*;
import static top.btmdc.hr.domain.PositionMatchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionMatchMapperTest {

    private PositionMatchMapper positionMatchMapper;

    @BeforeEach
    void setUp() {
        positionMatchMapper = new PositionMatchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPositionMatchSample1();
        var actual = positionMatchMapper.toEntity(positionMatchMapper.toDto(expected));
        assertPositionMatchAllPropertiesEquals(expected, actual);
    }
}
