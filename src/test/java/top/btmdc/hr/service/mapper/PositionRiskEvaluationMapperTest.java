package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PositionRiskEvaluationAsserts.*;
import static top.btmdc.hr.domain.PositionRiskEvaluationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionRiskEvaluationMapperTest {

    private PositionRiskEvaluationMapper positionRiskEvaluationMapper;

    @BeforeEach
    void setUp() {
        positionRiskEvaluationMapper = new PositionRiskEvaluationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPositionRiskEvaluationSample1();
        var actual = positionRiskEvaluationMapper.toEntity(positionRiskEvaluationMapper.toDto(expected));
        assertPositionRiskEvaluationAllPropertiesEquals(expected, actual);
    }
}
