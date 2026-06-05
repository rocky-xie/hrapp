package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionSkillRequirement;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.PositionSkillRequirementDTO;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.dto.SkillLevelDTO;

/**
 * Mapper for the entity {@link PositionSkillRequirement} and its DTO {@link PositionSkillRequirementDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionSkillRequirementMapper extends EntityMapper<PositionSkillRequirementDTO, PositionSkillRequirement> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillSkillName")
    @Mapping(target = "requiredLevel", source = "requiredLevel", qualifiedByName = "skillLevelCode")
    @Mapping(target = "preferredLevel", source = "preferredLevel", qualifiedByName = "skillLevelCode")
    PositionSkillRequirementDTO toDto(PositionSkillRequirement s);

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

    @Named("skillLevelCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    SkillLevelDTO toDtoSkillLevelCode(SkillLevel skillLevel);
}
