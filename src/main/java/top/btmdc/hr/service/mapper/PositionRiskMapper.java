package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.KeyResponsibilityCategory;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionRisk;
import top.btmdc.hr.service.dto.KeyResponsibilityCategoryDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.PositionRiskDTO;

/**
 * Mapper for the entity {@link PositionRisk} and its DTO {@link PositionRiskDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionRiskMapper extends EntityMapper<PositionRiskDTO, PositionRisk> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "category", source = "category", qualifiedByName = "keyResponsibilityCategoryCategoryName")
    PositionRiskDTO toDto(PositionRisk s);

    @Named("positionPositionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "positionName", source = "positionName")
    PositionDTO toDtoPositionPositionName(Position position);

    @Named("keyResponsibilityCategoryCategoryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoryName", source = "categoryName")
    KeyResponsibilityCategoryDTO toDtoKeyResponsibilityCategoryCategoryName(KeyResponsibilityCategory keyResponsibilityCategory);
}
