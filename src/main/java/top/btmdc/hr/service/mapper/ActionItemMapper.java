package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.ActionItem;
import top.btmdc.hr.service.dto.ActionItemDTO;

@Mapper(componentModel = "spring")
public interface ActionItemMapper extends EntityMapper<ActionItemDTO, ActionItem> {}
