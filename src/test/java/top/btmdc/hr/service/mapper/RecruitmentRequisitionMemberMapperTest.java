package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.RecruitmentRequisitionMemberAsserts.*;
import static top.btmdc.hr.domain.RecruitmentRequisitionMemberTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecruitmentRequisitionMemberMapperTest {

    private RecruitmentRequisitionMemberMapper recruitmentRequisitionMemberMapper;

    @BeforeEach
    void setUp() {
        recruitmentRequisitionMemberMapper = new RecruitmentRequisitionMemberMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecruitmentRequisitionMemberSample1();
        var actual = recruitmentRequisitionMemberMapper.toEntity(recruitmentRequisitionMemberMapper.toDto(expected));
        assertRecruitmentRequisitionMemberAllPropertiesEquals(expected, actual);
    }
}
