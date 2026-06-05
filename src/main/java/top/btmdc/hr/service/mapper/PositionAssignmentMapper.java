package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.PositionAssignment;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionAssignmentDTO;
import top.btmdc.hr.service.dto.PositionDTO;

/**
 * Mapper for the entity {@link PositionAssignment} and its DTO {@link PositionAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PositionAssignmentMapper extends EntityMapper<PositionAssignmentDTO, PositionAssignment> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    PositionAssignmentDTO toDto(PositionAssignment s);

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
