package top.btmdc.hr.service.mapper;

import org.mapstruct.*;
import top.btmdc.hr.domain.Evaluation;
import top.btmdc.hr.domain.Person;
import top.btmdc.hr.domain.Position;
import top.btmdc.hr.domain.TrainingGoal;
import top.btmdc.hr.service.dto.EvaluationDTO;
import top.btmdc.hr.service.dto.PersonDTO;
import top.btmdc.hr.service.dto.PositionDTO;
import top.btmdc.hr.service.dto.TrainingGoalDTO;

/**
 * Mapper for the entity {@link Evaluation} and its DTO {@link EvaluationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvaluationMapper extends EntityMapper<EvaluationDTO, Evaluation> {
    @Mapping(target = "person", source = "person", qualifiedByName = "personPersonName")
    @Mapping(target = "position", source = "position", qualifiedByName = "positionPositionName")
    @Mapping(target = "trainingGoal", source = "trainingGoal", qualifiedByName = "trainingGoalGoalName")
    @Mapping(target = "evaluator", source = "evaluator", qualifiedByName = "personPersonName")
    EvaluationDTO toDto(Evaluation s);

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

    @Named("trainingGoalGoalName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "goalName", source = "goalName")
    TrainingGoalDTO toDtoTrainingGoalGoalName(TrainingGoal trainingGoal);
}
