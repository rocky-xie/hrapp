package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PreboardingFollowUpAsserts.*;
import static top.btmdc.hr.domain.PreboardingFollowUpTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PreboardingFollowUpMapperTest {

    private PreboardingFollowUpMapper preboardingFollowUpMapper;

    @BeforeEach
    void setUp() {
        preboardingFollowUpMapper = new PreboardingFollowUpMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPreboardingFollowUpSample1();
        var actual = preboardingFollowUpMapper.toEntity(preboardingFollowUpMapper.toDto(expected));
        assertPreboardingFollowUpAllPropertiesEquals(expected, actual);
    }
}
