package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.HiringDecision;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.service.dto.HiringDecisionDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;

/**
 * Mapper for the entity {@link HiringDecision} and its DTO {@link HiringDecisionDTO}.
 */
@Mapper(componentModel = "spring")
public interface HiringDecisionMapper extends EntityMapper<HiringDecisionDTO, HiringDecision> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    HiringDecisionDTO toDto(HiringDecision s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
