package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.service.dto.RecruitmentRequisitionVersionDTO;

/**
 * Mapper for the entity {@link RecruitmentRequisitionVersion} and its DTO {@link RecruitmentRequisitionVersionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentRequisitionVersionMapper extends EntityMapper<RecruitmentRequisitionVersionDTO, RecruitmentRequisitionVersion> {
    @Mapping(target = "requisition", source = "requisition", qualifiedByName = "recruitmentRequisitionId")
    RecruitmentRequisitionVersionDTO toDto(RecruitmentRequisitionVersion s);

    @Named("recruitmentRequisitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "requisitionCode", source = "requisitionCode")
    RecruitmentRequisitionDTO toDtoRecruitmentRequisitionId(RecruitmentRequisition recruitmentRequisition);
}
