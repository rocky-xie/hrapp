package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.domain.TrainingRecord;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.TrainingGoalDTO;
import top.btmdc.hr.service.dto.TrainingRecordDTO;

/**
 * Mapper for the entity {@link TrainingRecord} and its DTO {@link TrainingRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainingRecordMapper extends EntityMapper<TrainingRecordDTO, TrainingRecord> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "trainingGoal", source = "trainingGoal", qualifiedByName = "trainingGoalGoalName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "mentor", source = "mentor", qualifiedByName = "personPersonName")
    TrainingRecordDTO toDto(TrainingRecord s);

    @Named("personPersonName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "personName", source = "personName")
    PersonDTO toDtoPersonPersonName(Person person);

    @Named("trainingGoalGoalName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "goalName", source = "goalName")
    TrainingGoalDTO toDtoTrainingGoalGoalName(TrainingGoal trainingGoal);

    @Named("positionPositionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "positionName", source = "positionName")
    PositionDTO toDtoPositionPositionName(Position position);
}
