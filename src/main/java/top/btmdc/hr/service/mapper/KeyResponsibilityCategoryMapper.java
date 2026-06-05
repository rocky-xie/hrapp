package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.KeyResponsibilityCategory;
import top.btmdc.hr.service.dto.KeyResponsibilityCategoryDTO;

/**
 * Mapper for the entity {@link KeyResponsibilityCategory} and its DTO {@link KeyResponsibilityCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface KeyResponsibilityCategoryMapper extends EntityMapper<KeyResponsibilityCategoryDTO, KeyResponsibilityCategory> {}
