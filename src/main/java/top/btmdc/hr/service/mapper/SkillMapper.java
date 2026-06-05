package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Skill;
import top.btmdc.hr.service.dto.SkillDTO;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {}
