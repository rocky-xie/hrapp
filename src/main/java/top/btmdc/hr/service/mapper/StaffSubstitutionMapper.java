package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.StaffSubstitution;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.StaffSubstitutionDTO;

/**
 * Mapper for the entity {@link StaffSubstitution} and its DTO {@link StaffSubstitutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface StaffSubstitutionMapper extends EntityMapper<StaffSubstitutionDTO, StaffSubstitution> {
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "candidatePerson", source = "candidatePerson", qualifiedByName = "personPersonName")
    StaffSubstitutionDTO toDto(StaffSubstitution s);

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
