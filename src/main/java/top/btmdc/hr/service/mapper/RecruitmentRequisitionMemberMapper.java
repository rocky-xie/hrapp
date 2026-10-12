package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionMember;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.service.dto.RecruitmentRequisitionMemberDTO;

/**
 * Mapper for the entity {@link RecruitmentRequisitionMember} and its DTO {@link RecruitmentRequisitionMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentRequisitionMemberMapper extends EntityMapper<RecruitmentRequisitionMemberDTO, RecruitmentRequisitionMember> {
    @Mapping(target = "requisition", source = "requisition", qualifiedByName = "recruitmentRequisitionId")
    RecruitmentRequisitionMemberDTO toDto(RecruitmentRequisitionMember s);

    @Named("recruitmentRequisitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "requisitionCode", source = "requisitionCode")
    RecruitmentRequisitionDTO toDtoRecruitmentRequisitionId(RecruitmentRequisition recruitmentRequisition);
}
