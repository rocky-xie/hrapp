package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.RecruitmentEvaluationAsserts.*;
import static top.btmdc.hr.domain.RecruitmentEvaluationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruitmentEvaluationMapperTest {

    private RecruitmentEvaluationMapper recruitmentEvaluationMapper;

    @BeforeEach
    void setUp() {
        recruitmentEvaluationMapper = new RecruitmentEvaluationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecruitmentEvaluationSample1();
        var actual = recruitmentEvaluationMapper.toEntity(recruitmentEvaluationMapper.toDto(expected));
        assertRecruitmentEvaluationAllPropertiesEquals(expected, actual);
    }
}
