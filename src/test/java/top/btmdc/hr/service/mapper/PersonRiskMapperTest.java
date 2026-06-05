package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PersonRiskAsserts.*;
import static top.btmdc.hr.domain.PersonRiskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonRiskMapperTest {

    private PersonRiskMapper personRiskMapper;

    @BeforeEach
    void setUp() {
        personRiskMapper = new PersonRiskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonRiskSample1();
        var actual = personRiskMapper.toEntity(personRiskMapper.toDto(expected));
        assertPersonRiskAllPropertiesEquals(expected, actual);
    }
}
