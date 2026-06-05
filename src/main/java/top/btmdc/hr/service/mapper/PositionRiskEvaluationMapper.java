package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionRiskEvaluation;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.PositionRiskEvaluationDTO;

/**
 * Mapper for the entity {@link PositionRiskEvaluation} and its DTO {@link PositionRiskEvaluationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionRiskEvaluationMapper extends EntityMapper<PositionRiskEvaluationDTO, PositionRiskEvaluation> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    PositionRiskEvaluationDTO toDto(PositionRiskEvaluation s);

    @Named("positionPositionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "positionName", source = "positionName")
    PositionDTO toDtoPositionPositionName(Position position);
}
