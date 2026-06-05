package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.SkillLevel;
import top.btmdc.hr.service.dto.SkillLevelDTO;

/**
 * Mapper for the entity {@link SkillLevel} and its DTO {@link SkillLevelDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillLevelMapper extends EntityMapper<SkillLevelDTO, SkillLevel> {}
