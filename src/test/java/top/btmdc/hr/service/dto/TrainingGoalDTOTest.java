package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class TrainingGoalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingGoalDTO.class);
        TrainingGoalDTO trainingGoalDTO1 = new TrainingGoalDTO();
        trainingGoalDTO1.setId(1L);
        TrainingGoalDTO trainingGoalDTO2 = new TrainingGoalDTO();
        assertThat(trainingGoalDTO1).isNotEqualTo(trainingGoalDTO2);
        trainingGoalDTO2.setId(trainingGoalDTO1.getId());
        assertThat(trainingGoalDTO1).isEqualTo(trainingGoalDTO2);
        trainingGoalDTO2.setId(2L);
        assertThat(trainingGoalDTO1).isNotEqualTo(trainingGoalDTO2);
        trainingGoalDTO1.setId(null);
        assertThat(trainingGoalDTO1).isNotEqualTo(trainingGoalDTO2);
    }
}
