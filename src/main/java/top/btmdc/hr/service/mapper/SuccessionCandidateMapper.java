package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.SuccessionCandidate;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.SuccessionCandidateDTO;

/**
 * Mapper for the entity {@link SuccessionCandidate} and its DTO {@link SuccessionCandidateDTO}.
 */
@Mapper(componentModel = "spring")
public interface SuccessionCandidateMapper extends EntityMapper<SuccessionCandidateDTO, SuccessionCandidate> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "currentOwner", source = "currentOwner", qualifiedByName = "personPersonName")
    @Mapping(target = "candidate", source = "candidate", qualifiedByName = "personPersonName")
    SuccessionCandidateDTO toDto(SuccessionCandidate s);

    @Named("positionPositionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "positionName", source = "positionName")
    PositionDTO toDtoPositionPositionName(Position position);

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);
}
