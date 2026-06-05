package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.KeyResponsibilityCategoryAsserts.*;
import static top.btmdc.hr.domain.KeyResponsibilityCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeyResponsibilityCategoryMapperTest {

    private KeyResponsibilityCategoryMapper keyResponsibilityCategoryMapper;

    @BeforeEach
    void setUp() {
        keyResponsibilityCategoryMapper = new KeyResponsibilityCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getKeyResponsibilityCategorySample1();
        var actual = keyResponsibilityCategoryMapper.toEntity(keyResponsibilityCategoryMapper.toDto(expected));
        assertKeyResponsibilityCategoryAllPropertiesEquals(expected, actual);
    }
}
