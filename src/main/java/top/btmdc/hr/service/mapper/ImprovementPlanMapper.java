package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.ImprovementPlan;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.service.dto.ImprovementPlanDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.SkillDTO;

/**
 * Mapper for the entity {@link ImprovementPlan} and its DTO {@link ImprovementPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImprovementPlanMapper extends EntityMapper<ImprovementPlanDTO, ImprovementPlan> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillSkillName")
    ImprovementPlanDTO toDto(ImprovementPlan s);

    @Named("positionPositionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "positionName", source = "positionName")
    PositionDTO toDtoPositionPositionName(Position position);

    @Named("skillSkillName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "skillName", source = "skillName")
    SkillDTO toDtoSkillSkillName(Skill skill);
}
