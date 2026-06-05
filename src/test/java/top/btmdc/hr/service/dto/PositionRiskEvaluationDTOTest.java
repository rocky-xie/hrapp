package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class PositionRiskEvaluationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionRiskEvaluationDTO.class);
        PositionRiskEvaluationDTO positionRiskEvaluationDTO1 = new PositionRiskEvaluationDTO();
        positionRiskEvaluationDTO1.setId(1L);
        PositionRiskEvaluationDTO positionRiskEvaluationDTO2 = new PositionRiskEvaluationDTO();
        assertThat(positionRiskEvaluationDTO1).isNotEqualTo(positionRiskEvaluationDTO2);
        positionRiskEvaluationDTO2.setId(positionRiskEvaluationDTO1.getId());
        assertThat(positionRiskEvaluationDTO1).isEqualTo(positionRiskEvaluationDTO2);
        positionRiskEvaluationDTO2.setId(2L);
        assertThat(positionRiskEvaluationDTO1).isNotEqualTo(positionRiskEvaluationDTO2);
        positionRiskEvaluationDTO1.setId(null);
        assertThat(positionRiskEvaluationDTO1).isNotEqualTo(positionRiskEvaluationDTO2);
    }
}
