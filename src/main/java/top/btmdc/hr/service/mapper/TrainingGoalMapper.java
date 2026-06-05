package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.dto.SkillLevelDTO;
import top.btmdc.hr.service.dto.TrainingGoalDTO;

/**
 * Mapper for the entity {@link TrainingGoal} and its DTO {@link TrainingGoalDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainingGoalMapper extends EntityMapper<TrainingGoalDTO, TrainingGoal> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillSkillName")
    @Mapping(target = "targetLevel", source = "targetLevel", qualifiedByName = "skillLevelCode")
    TrainingGoalDTO toDto(TrainingGoal s);

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);

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
