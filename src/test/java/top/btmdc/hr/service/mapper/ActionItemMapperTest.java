package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.ActionItemAsserts.*;
import static top.btmdc.hr.domain.ActionItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionItemMapperTest {

    private ActionItemMapper actionItemMapper;

    @BeforeEach
    void setUp() {
        actionItemMapper = new ActionItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getActionItemSample1();
        var actual = actionItemMapper.toEntity(actionItemMapper.toDto(expected));
        assertActionItemAllPropertiesEquals(expected, actual);
    }
}
