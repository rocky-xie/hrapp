package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.RecruitmentApplicationTestSamples.*;
import static top.btmdc.hr.domain.SelectionActivityTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class SelectionActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SelectionActivity.class);
        SelectionActivity selectionActivity1 = getSelectionActivitySample1();
        SelectionActivity selectionActivity2 = new SelectionActivity();
        assertThat(selectionActivity1).isNotEqualTo(selectionActivity2);

        selectionActivity2.setId(selectionActivity1.getId());
        assertThat(selectionActivity1).isEqualTo(selectionActivity2);

        selectionActivity2 = getSelectionActivitySample2();
        assertThat(selectionActivity1).isNotEqualTo(selectionActivity2);
    }

    @Test
    void applicationTest() {
        SelectionActivity selectionActivity = getSelectionActivityRandomSampleGenerator();
        RecruitmentApplication recruitmentApplicationBack = getRecruitmentApplicationRandomSampleGenerator();

        selectionActivity.setApplication(recruitmentApplicationBack);
        assertThat(selectionActivity.getApplication()).isEqualTo(recruitmentApplicationBack);

        selectionActivity.application(null);
        assertThat(selectionActivity.getApplication()).isNull();
    }
}
