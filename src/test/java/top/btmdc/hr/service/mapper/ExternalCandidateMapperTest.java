package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.ExternalCandidateAsserts.*;
import static top.btmdc.hr.domain.ExternalCandidateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExternalCandidateMapperTest {

    private ExternalCandidateMapper externalCandidateMapper;

    @BeforeEach
    void setUp() {
        externalCandidateMapper = new ExternalCandidateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExternalCandidateSample1();
        var actual = externalCandidateMapper.toEntity(externalCandidateMapper.toDto(expected));
        assertExternalCandidateAllPropertiesEquals(expected, actual);
    }
}
