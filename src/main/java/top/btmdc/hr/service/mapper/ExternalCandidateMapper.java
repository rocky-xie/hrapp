package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.ExternalCandidate;
import top.btmdc.hr.service.dto.ExternalCandidateDTO;

/**
 * Mapper for the entity {@link ExternalCandidate} and its DTO {@link ExternalCandidateDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExternalCandidateMapper extends EntityMapper<ExternalCandidateDTO, ExternalCandidate> {}
