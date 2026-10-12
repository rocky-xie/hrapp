package top.btmdc.hr.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class DispatchReviewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DispatchReviewDTO.class);
        DispatchReviewDTO dispatchReviewDTO1 = new DispatchReviewDTO();
        dispatchReviewDTO1.setId(1L);
        DispatchReviewDTO dispatchReviewDTO2 = new DispatchReviewDTO();
        assertThat(dispatchReviewDTO1).isNotEqualTo(dispatchReviewDTO2);
        dispatchReviewDTO2.setId(dispatchReviewDTO1.getId());
        assertThat(dispatchReviewDTO1).isEqualTo(dispatchReviewDTO2);
        dispatchReviewDTO2.setId(2L);
        assertThat(dispatchReviewDTO1).isNotEqualTo(dispatchReviewDTO2);
        dispatchReviewDTO1.setId(null);
        assertThat(dispatchReviewDTO1).isNotEqualTo(dispatchReviewDTO2);
    }
}
