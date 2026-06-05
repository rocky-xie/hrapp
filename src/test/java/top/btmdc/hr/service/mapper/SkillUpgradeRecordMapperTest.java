package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.SkillUpgradeRecordAsserts.*;
import static top.btmdc.hr.domain.SkillUpgradeRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SkillUpgradeRecordMapperTest {

    private SkillUpgradeRecordMapper skillUpgradeRecordMapper;

    @BeforeEach
    void setUp() {
        skillUpgradeRecordMapper = new SkillUpgradeRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSkillUpgradeRecordSample1();
        var actual = skillUpgradeRecordMapper.toEntity(skillUpgradeRecordMapper.toDto(expected));
        assertSkillUpgradeRecordAllPropertiesEquals(expected, actual);
    }
}
