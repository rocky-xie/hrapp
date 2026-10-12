package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;

/**
 * Mapper for the entity {@link RecruitmentRequisition} and its DTO {@link RecruitmentRequisitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecruitmentRequisitionMapper extends EntityMapper<RecruitmentRequisitionDTO, RecruitmentRequisition> {}
