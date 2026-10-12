package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.RecruitmentApplicationAsserts.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruitmentApplicationMapperTest {

    private RecruitmentApplicationMapper recruitmentApplicationMapper;

    @BeforeEach
    void setUp() {
        recruitmentApplicationMapper = new RecruitmentApplicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecruitmentApplicationSample1();
        var actual = recruitmentApplicationMapper.toEntity(recruitmentApplicationMapper.toDto(expected));
        assertRecruitmentApplicationAllPropertiesEquals(expected, actual);
    }
}
