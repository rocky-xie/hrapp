package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.service.dto.CandidateResumeDTO;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;

/**
 * Mapper for the entity {@link CandidateResume} and its DTO {@link CandidateResumeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidateResumeMapper extends EntityMapper<CandidateResumeDTO, CandidateResume> {
    @Mapping(target = "candidate", source = "candidate", qualifiedByName = "externalCandidateId")
    CandidateResumeDTO toDto(CandidateResume s);

    @Named("externalCandidateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "email", source = "email")
    ExternalCandidateDTO toDtoExternalCandidateId(ExternalCandidate externalCandidate);
}
