package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.ApplicationEvent;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.service.dto.ApplicationEventDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;

/**
 * Mapper for the entity {@link ApplicationEvent} and its DTO {@link ApplicationEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationEventMapper extends EntityMapper<ApplicationEventDTO, ApplicationEvent> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    ApplicationEventDTO toDto(ApplicationEvent s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
