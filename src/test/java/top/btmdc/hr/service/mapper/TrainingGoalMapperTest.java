package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.TrainingGoalAsserts.*;
import static top.btmdc.hr.domain.TrainingGoalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingGoalMapperTest {

    private TrainingGoalMapper trainingGoalMapper;

    @BeforeEach
    void setUp() {
        trainingGoalMapper = new TrainingGoalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrainingGoalSample1();
        var actual = trainingGoalMapper.toEntity(trainingGoalMapper.toDto(expected));
        assertTrainingGoalAllPropertiesEquals(expected, actual);
    }
}
