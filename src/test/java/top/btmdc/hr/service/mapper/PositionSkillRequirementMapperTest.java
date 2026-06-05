package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PositionSkillRequirementAsserts.*;
import static top.btmdc.hr.domain.PositionSkillRequirementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionSkillRequirementMapperTest {

    private PositionSkillRequirementMapper positionSkillRequirementMapper;

    @BeforeEach
    void setUp() {
        positionSkillRequirementMapper = new PositionSkillRequirementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPositionSkillRequirementSample1();
        var actual = positionSkillRequirementMapper.toEntity(positionSkillRequirementMapper.toDto(expected));
        assertPositionSkillRequirementAllPropertiesEquals(expected, actual);
    }
}
