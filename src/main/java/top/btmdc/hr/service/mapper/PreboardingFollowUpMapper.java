package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.PreboardingFollowUp;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.service.dto.PreboardingFollowUpDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;

/**
 * Mapper for the entity {@link PreboardingFollowUp} and its DTO {@link PreboardingFollowUpDTO}.
 */
@Mapper(componentModel = "spring")
public interface PreboardingFollowUpMapper extends EntityMapper<PreboardingFollowUpDTO, PreboardingFollowUp> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    PreboardingFollowUpDTO toDto(PreboardingFollowUp s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
