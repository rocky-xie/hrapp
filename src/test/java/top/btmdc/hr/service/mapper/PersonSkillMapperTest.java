package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PersonSkillAsserts.*;
import static top.btmdc.hr.domain.PersonSkillTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonSkillMapperTest {

    private PersonSkillMapper personSkillMapper;

    @BeforeEach
    void setUp() {
        personSkillMapper = new PersonSkillMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonSkillSample1();
        var actual = personSkillMapper.toEntity(personSkillMapper.toDto(expected));
        assertPersonSkillAllPropertiesEquals(expected, actual);
    }
}
