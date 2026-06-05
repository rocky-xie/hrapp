package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PositionAssignmentAsserts.*;
import static top.btmdc.hr.domain.PositionAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionAssignmentMapperTest {

    private PositionAssignmentMapper positionAssignmentMapper;

    @BeforeEach
    void setUp() {
        positionAssignmentMapper = new PositionAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPositionAssignmentSample1();
        var actual = positionAssignmentMapper.toEntity(positionAssignmentMapper.toDto(expected));
        assertPositionAssignmentAllPropertiesEquals(expected, actual);
    }
}
