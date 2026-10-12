package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.RecruitmentRequisitionAsserts.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionMapperTest {

    private RecruitmentRequisitionMapper recruitmentRequisitionMapper;

    @BeforeEach
    void setUp() {
        recruitmentRequisitionMapper = new RecruitmentRequisitionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecruitmentRequisitionSample1();
        var actual = recruitmentRequisitionMapper.toEntity(recruitmentRequisitionMapper.toDto(expected));
        assertRecruitmentRequisitionAllPropertiesEquals(expected, actual);
    }
}
