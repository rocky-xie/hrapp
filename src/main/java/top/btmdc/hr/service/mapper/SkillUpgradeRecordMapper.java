package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.domain.SkillUpgradeRecord;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.SkillDTO;
import top.btmdc.hr.service.dto.SkillLevelDTO;
import top.btmdc.hr.service.dto.SkillUpgradeRecordDTO;

/**
 * Mapper for the entity {@link SkillUpgradeRecord} and its DTO {@link SkillUpgradeRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillUpgradeRecordMapper extends EntityMapper<SkillUpgradeRecordDTO, SkillUpgradeRecord> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "skill", source = "skill", qualifiedByName = "skillSkillName")
    @Mapping(target = "oldLevel", source = "oldLevel", qualifiedByName = "skillLevelCode")
    @Mapping(target = "newLevel", source = "newLevel", qualifiedByName = "skillLevelCode")
    @Mapping(target = "assessor", source = "assessor", qualifiedByName = "personPersonName")
    SkillUpgradeRecordDTO toDto(SkillUpgradeRecord s);

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

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);
}
