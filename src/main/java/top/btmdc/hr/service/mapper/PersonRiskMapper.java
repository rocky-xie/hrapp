package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.PersonRisk;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PersonRiskDTO;
import top.btmdc.hr.service.dto.PositionDTO;

/**
 * Mapper for the entity {@link PersonRisk} and its DTO {@link PersonRiskDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonRiskMapper extends EntityMapper<PersonRiskDTO, PersonRisk> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    PersonRiskDTO toDto(PersonRisk s);

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
