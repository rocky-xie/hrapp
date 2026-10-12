package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.ApplicationStage;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.service.dto.ApplicationStageDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;

/**
 * Mapper for the entity {@link ApplicationStage} and its DTO {@link ApplicationStageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationStageMapper extends EntityMapper<ApplicationStageDTO, ApplicationStage> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    ApplicationStageDTO toDto(ApplicationStage s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
