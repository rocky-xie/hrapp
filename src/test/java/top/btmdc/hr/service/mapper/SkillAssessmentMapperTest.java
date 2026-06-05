package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.SkillAssessmentAsserts.*;
import static top.btmdc.hr.domain.SkillAssessmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillAssessmentMapperTest {

    private SkillAssessmentMapper skillAssessmentMapper;

    @BeforeEach
    void setUp() {
        skillAssessmentMapper = new SkillAssessmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSkillAssessmentSample1();
        var actual = skillAssessmentMapper.toEntity(skillAssessmentMapper.toDto(expected));
        assertSkillAssessmentAllPropertiesEquals(expected, actual);
    }
}
