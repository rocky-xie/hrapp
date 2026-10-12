package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.NotificationMessage;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;

/**
 * Mapper for the entity {@link NotificationMessage} and its DTO {@link NotificationMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMessageMapper extends EntityMapper<NotificationMessageDTO, NotificationMessage> {
    @Mapping(target = "application", source = "application", qualifiedByName = "recruitmentApplicationId")
    NotificationMessageDTO toDto(NotificationMessage s);

    @Named("recruitmentApplicationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "applicationCode", source = "applicationCode")
    RecruitmentApplicationDTO toDtoRecruitmentApplicationId(RecruitmentApplication recruitmentApplication);
}
