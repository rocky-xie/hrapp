package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.TrustObservationAsserts.*;
import static top.btmdc.hr.domain.TrustObservationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrustObservationMapperTest {

    private TrustObservationMapper trustObservationMapper;

    @BeforeEach
    void setUp() {
        trustObservationMapper = new TrustObservationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrustObservationSample1();
        var actual = trustObservationMapper.toEntity(trustObservationMapper.toDto(expected));
        assertTrustObservationAllPropertiesEquals(expected, actual);
    }
}
