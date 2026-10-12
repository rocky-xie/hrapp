package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.SelectionActivityAsserts.*;
import static top.btmdc.hr.domain.SelectionActivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SelectionActivityMapperTest {

    private SelectionActivityMapper selectionActivityMapper;

    @BeforeEach
    void setUp() {
        selectionActivityMapper = new SelectionActivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSelectionActivitySample1();
        var actual = selectionActivityMapper.toEntity(selectionActivityMapper.toDto(expected));
        assertSelectionActivityAllPropertiesEquals(expected, actual);
    }
}
