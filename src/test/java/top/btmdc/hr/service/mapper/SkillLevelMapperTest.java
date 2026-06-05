package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.SkillLevelAsserts.*;
import static top.btmdc.hr.domain.SkillLevelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillLevelMapperTest {

    private SkillLevelMapper skillLevelMapper;

    @BeforeEach
    void setUp() {
        skillLevelMapper = new SkillLevelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSkillLevelSample1();
        var actual = skillLevelMapper.toEntity(skillLevelMapper.toDto(expected));
        assertSkillLevelAllPropertiesEquals(expected, actual);
    }
}
