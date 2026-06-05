package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillAssessment;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.SkillAssessmentDTO;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.dto.SkillLevelDTO;

/**
 * Mapper for the entity {@link SkillAssessment} and its DTO {@link SkillAssessmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillAssessmentMapper extends EntityMapper<SkillAssessmentDTO, SkillAssessment> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillSkillName")
    @Mapping(target = "assessor", source = "assessor", qualifiedByName = "personPersonName")
    @Mapping(target = "newLevel", source = "newLevel", qualifiedByName = "skillLevelCode")
    SkillAssessmentDTO toDto(SkillAssessment s);

    @Named("skillSkillName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "skillName", source = "skillName")
    SkillDTO toDtoSkillSkillName(Skill skill);

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);

    @Named("skillLevelCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    SkillLevelDTO toDtoSkillLevelCode(SkillLevel skillLevel);
}
