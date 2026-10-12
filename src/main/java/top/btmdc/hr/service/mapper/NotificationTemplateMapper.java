package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.NotificationTemplate;
import top.btmdc.hr.service.dto.NotificationTemplateDTO;

/**
 * Mapper for the entity {@link NotificationTemplate} and its DTO {@link NotificationTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationTemplateMapper extends EntityMapper<NotificationTemplateDTO, NotificationTemplate> {}
