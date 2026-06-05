package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.ImprovementPlanAsserts.*;
import static top.btmdc.hr.domain.ImprovementPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImprovementPlanMapperTest {

    private ImprovementPlanMapper improvementPlanMapper;

    @BeforeEach
    void setUp() {
        improvementPlanMapper = new ImprovementPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImprovementPlanSample1();
        var actual = improvementPlanMapper.toEntity(improvementPlanMapper.toDto(expected));
        assertImprovementPlanAllPropertiesEquals(expected, actual);
    }
}
