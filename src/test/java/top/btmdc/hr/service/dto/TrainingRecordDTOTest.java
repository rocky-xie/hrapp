package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class TrainingRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingRecordDTO.class);
        TrainingRecordDTO trainingRecordDTO1 = new TrainingRecordDTO();
        trainingRecordDTO1.setId(1L);
        TrainingRecordDTO trainingRecordDTO2 = new TrainingRecordDTO();
        assertThat(trainingRecordDTO1).isNotEqualTo(trainingRecordDTO2);
        trainingRecordDTO2.setId(trainingRecordDTO1.getId());
        assertThat(trainingRecordDTO1).isEqualTo(trainingRecordDTO2);
        trainingRecordDTO2.setId(2L);
        assertThat(trainingRecordDTO1).isNotEqualTo(trainingRecordDTO2);
        trainingRecordDTO1.setId(null);
        assertThat(trainingRecordDTO1).isNotEqualTo(trainingRecordDTO2);
    }
}
