package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.CandidateProfileAsserts.*;
import static top.btmdc.hr.domain.CandidateProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CandidateProfileMapperTest {

    private CandidateProfileMapper candidateProfileMapper;

    @BeforeEach
    void setUp() {
        candidateProfileMapper = new CandidateProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCandidateProfileSample1();
        var actual = candidateProfileMapper.toEntity(candidateProfileMapper.toDto(expected));
        assertCandidateProfileAllPropertiesEquals(expected, actual);
    }
}
