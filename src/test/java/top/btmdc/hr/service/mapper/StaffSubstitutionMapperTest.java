package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.StaffSubstitutionAsserts.*;
import static top.btmdc.hr.domain.StaffSubstitutionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StaffSubstitutionMapperTest {

    private StaffSubstitutionMapper staffSubstitutionMapper;

    @BeforeEach
    void setUp() {
        staffSubstitutionMapper = new StaffSubstitutionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStaffSubstitutionSample1();
        var actual = staffSubstitutionMapper.toEntity(staffSubstitutionMapper.toDto(expected));
        assertStaffSubstitutionAllPropertiesEquals(expected, actual);
    }
}
