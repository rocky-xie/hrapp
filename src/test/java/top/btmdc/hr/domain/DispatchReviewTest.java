package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.DispatchAssignmentTestSamples.*;
import static top.btmdc.hr.domain.DispatchReviewTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class DispatchReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DispatchReview.class);
        DispatchReview dispatchReview1 = getDispatchReviewSample1();
        DispatchReview dispatchReview2 = new DispatchReview();
        assertThat(dispatchReview1).isNotEqualTo(dispatchReview2);

        dispatchReview2.setId(dispatchReview1.getId());
        assertThat(dispatchReview1).isEqualTo(dispatchReview2);

        dispatchReview2 = getDispatchReviewSample2();
        assertThat(dispatchReview1).isNotEqualTo(dispatchReview2);
    }

    @Test
    void dispatchAssignmentTest() {
        DispatchReview dispatchReview = getDispatchReviewRandomSampleGenerator();
        DispatchAssignment dispatchAssignmentBack = getDispatchAssignmentRandomSampleGenerator();

        dispatchReview.setDispatchAssignment(dispatchAssignmentBack);
        assertThat(dispatchReview.getDispatchAssignment()).isEqualTo(dispatchAssignmentBack);

        dispatchReview.dispatchAssignment(null);
        assertThat(dispatchReview.getDispatchAssignment()).isNull();
    }
}
