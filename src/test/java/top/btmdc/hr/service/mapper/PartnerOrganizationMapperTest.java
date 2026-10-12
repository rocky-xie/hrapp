package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.PartnerOrganizationAsserts.*;
import static top.btmdc.hr.domain.PartnerOrganizationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartnerOrganizationMapperTest {

    private PartnerOrganizationMapper partnerOrganizationMapper;

    @BeforeEach
    void setUp() {
        partnerOrganizationMapper = new PartnerOrganizationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPartnerOrganizationSample1();
        var actual = partnerOrganizationMapper.toEntity(partnerOrganizationMapper.toDto(expected));
        assertPartnerOrganizationAllPropertiesEquals(expected, actual);
    }
}
