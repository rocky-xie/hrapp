package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.DispatchAssignmentAsserts.*;
import static top.btmdc.hr.domain.DispatchAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatchAssignmentMapperTest {

    private DispatchAssignmentMapper dispatchAssignmentMapper;

    @BeforeEach
    void setUp() {
        dispatchAssignmentMapper = new DispatchAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDispatchAssignmentSample1();
        var actual = dispatchAssignmentMapper.toEntity(dispatchAssignmentMapper.toDto(expected));
        assertDispatchAssignmentAllPropertiesEquals(expected, actual);
    }
}
