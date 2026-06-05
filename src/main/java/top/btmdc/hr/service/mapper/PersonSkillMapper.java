package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PersonSkillDTO;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.dto.SkillLevelDTO;

/**
 * Mapper for the entity {@link PersonSkill} and its DTO {@link PersonSkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonSkillMapper extends EntityMapper<PersonSkillDTO, PersonSkill> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillSkillName")
    @Mapping(target = "currentLevel", source = "currentLevel", qualifiedByName = "skillLevelCode")
    @Mapping(target = "previousLevel", source = "previousLevel", qualifiedByName = "skillLevelCode")
    PersonSkillDTO toDto(PersonSkill s);

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);

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
