package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.ApplicationStageAsserts.*;
import static top.btmdc.hr.domain.ApplicationStageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationStageMapperTest {

    private ApplicationStageMapper applicationStageMapper;

    @BeforeEach
    void setUp() {
        applicationStageMapper = new ApplicationStageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApplicationStageSample1();
        var actual = applicationStageMapper.toEntity(applicationStageMapper.toDto(expected));
        assertApplicationStageAllPropertiesEquals(expected, actual);
    }
}
