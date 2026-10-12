package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.DispatchAssignmentTestSamples.*;
import static top.btmdc.hr.domain.DispatchReviewTestSamples.*;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class DispatchAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DispatchAssignment.class);
        DispatchAssignment dispatchAssignment1 = getDispatchAssignmentSample1();
        DispatchAssignment dispatchAssignment2 = new DispatchAssignment();
        assertThat(dispatchAssignment1).isNotEqualTo(dispatchAssignment2);

        dispatchAssignment2.setId(dispatchAssignment1.getId());
        assertThat(dispatchAssignment1).isEqualTo(dispatchAssignment2);

        dispatchAssignment2 = getDispatchAssignmentSample2();
        assertThat(dispatchAssignment1).isNotEqualTo(dispatchAssignment2);
    }

    @Test
    void reviewsTest() {
        DispatchAssignment dispatchAssignment = getDispatchAssignmentRandomSampleGenerator();
        DispatchReview dispatchReviewBack = getDispatchReviewRandomSampleGenerator();

        dispatchAssignment.addReviews(dispatchReviewBack);
        assertThat(dispatchAssignment.getReviewses()).containsOnly(dispatchReviewBack);
        assertThat(dispatchReviewBack.getDispatchAssignment()).isEqualTo(dispatchAssignment);

        dispatchAssignment.removeReviews(dispatchReviewBack);
        assertThat(dispatchAssignment.getReviewses()).doesNotContain(dispatchReviewBack);
        assertThat(dispatchReviewBack.getDispatchAssignment()).isNull();

        dispatchAssignment.reviewses(new HashSet<>(Set.of(dispatchReviewBack)));
        assertThat(dispatchAssignment.getReviewses()).containsOnly(dispatchReviewBack);
        assertThat(dispatchReviewBack.getDispatchAssignment()).isEqualTo(dispatchAssignment);

        dispatchAssignment.setReviewses(new HashSet<>());
        assertThat(dispatchAssignment.getReviewses()).doesNotContain(dispatchReviewBack);
        assertThat(dispatchReviewBack.getDispatchAssignment()).isNull();
    }

    @Test
    void applicationTest() {
        DispatchAssignment dispatchAssignment = getDispatchAssignmentRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        dispatchAssignment.setApplication(recruitmentApplicationBack);
        assertThat(dispatchAssignment.getApplication()).isEqualTo(recruitmentApplicationBack);

        dispatchAssignment.application(null);
        assertThat(dispatchAssignment.getApplication()).isNull();
    }
}
