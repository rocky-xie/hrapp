package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionMatch;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.PositionMatchDTO;

/**
 * Mapper for the entity {@link PositionMatch} and its DTO {@link PositionMatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionMatchMapper extends EntityMapper<PositionMatchDTO, PositionMatch> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    PositionMatchDTO toDto(PositionMatch s);

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
