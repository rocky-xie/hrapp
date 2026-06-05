package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PositionAsserts.*;
import static top.btmdc.hr.domain.PositionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionMapperTest {

    private PositionMapper positionMapper;

    @BeforeEach
    void setUp() {
        positionMapper = new PositionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPositionSample1();
        var actual = positionMapper.toEntity(positionMapper.toDto(expected));
        assertPositionAllPropertiesEquals(expected, actual);
    }
}
