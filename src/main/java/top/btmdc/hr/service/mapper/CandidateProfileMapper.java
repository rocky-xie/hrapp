package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.CandidateProfile;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.service.dto.CandidateProfileDTO;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;

/**
 * Mapper for the entity {@link CandidateProfile} and its DTO {@link CandidateProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidateProfileMapper extends EntityMapper<CandidateProfileDTO, CandidateProfile> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "observer", source = "observer", qualifiedByName = "personPersonName")
    CandidateProfileDTO toDto(CandidateProfile s);

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
}
