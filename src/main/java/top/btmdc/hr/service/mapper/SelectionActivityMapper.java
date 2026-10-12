package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.SelectionActivity;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.service.dto.SelectionActivityDTO;

/**
 * Mapper for the entity {@link SelectionActivity} and its DTO {@link SelectionActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface SelectionActivityMapper extends EntityMapper<SelectionActivityDTO, SelectionActivity> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    SelectionActivityDTO toDto(SelectionActivity s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
