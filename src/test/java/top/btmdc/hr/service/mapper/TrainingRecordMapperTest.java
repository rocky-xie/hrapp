package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.TrainingRecordAsserts.*;
import static top.btmdc.hr.domain.TrainingRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingRecordMapperTest {

    private TrainingRecordMapper trainingRecordMapper;

    @BeforeEach
    void setUp() {
        trainingRecordMapper = new TrainingRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrainingRecordSample1();
        var actual = trainingRecordMapper.toEntity(trainingRecordMapper.toDto(expected));
        assertTrainingRecordAllPropertiesEquals(expected, actual);
    }
}
