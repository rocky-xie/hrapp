package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.service.dto.RecruitmentRequisitionVersionDTO;

/**
 * Mapper for the entity {@link RecruitmentApplication} and its DTO {@link RecruitmentApplicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentApplicationMapper extends EntityMapper<RecruitmentApplicationDTO, RecruitmentApplication> {
    @Mapping(target = "candidate", source = "candidate", qualifiedByName = "externalCandidateId")
    @Mapping(target = "requisition", source = "requisition", qualifiedByName = "recruitmentRequisitionId")
    @Mapping(target = "requisitionVersion", source = "requisitionVersion", qualifiedByName = "recruitmentRequisitionVersionId")
    RecruitmentApplicationDTO toDto(RecruitmentApplication s);

    @Named("externalCandidateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    ExternalCandidateDTO toDtoExternalCandidateId(ExternalCandidate externalCandidate);

    @Named("recruitmentRequisitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "requisitionCode", source = "requisitionCode")
    RecruitmentRequisitionDTO toDtoRecruitmentRequisitionId(RecruitmentRequisition recruitmentRequisition);

    @Named("recruitmentRequisitionVersionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "versionNumber", source = "versionNumber")
    RecruitmentRequisitionVersionDTO toDtoRecruitmentRequisitionVersionId(RecruitmentRequisitionVersion recruitmentRequisitionVersion);
}
