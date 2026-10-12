package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.RecruitmentRequisitionVersionAsserts.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionVersionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionVersionMapperTest {

    private RecruitmentRequisitionVersionMapper recruitmentRequisitionVersionMapper;

    @BeforeEach
    void setUp() {
        recruitmentRequisitionVersionMapper = new RecruitmentRequisitionVersionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecruitmentRequisitionVersionSample1();
        var actual = recruitmentRequisitionVersionMapper.toEntity(recruitmentRequisitionVersionMapper.toDto(expected));
        assertRecruitmentRequisitionVersionAllPropertiesEquals(expected, actual);
    }
}
