package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.SuccessionCandidateAsserts.*;
import static top.btmdc.hr.domain.SuccessionCandidateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuccessionCandidateMapperTest {

    private SuccessionCandidateMapper successionCandidateMapper;

    @BeforeEach
    void setUp() {
        successionCandidateMapper = new SuccessionCandidateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSuccessionCandidateSample1();
        var actual = successionCandidateMapper.toEntity(successionCandidateMapper.toDto(expected));
        assertSuccessionCandidateAllPropertiesEquals(expected, actual);
    }
}
