package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.CandidateResumeAsserts.*;
import static top.btmdc.hr.domain.CandidateResumeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CandidateResumeMapperTest {

    private CandidateResumeMapper candidateResumeMapper;

    @BeforeEach
    void setUp() {
        candidateResumeMapper = new CandidateResumeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCandidateResumeSample1();
        var actual = candidateResumeMapper.toEntity(candidateResumeMapper.toDto(expected));
        assertCandidateResumeAllPropertiesEquals(expected, actual);
    }
}
