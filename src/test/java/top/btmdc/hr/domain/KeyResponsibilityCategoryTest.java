package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.KeyResponsibilityCategoryTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class KeyResponsibilityCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KeyResponsibilityCategory.class);
        KeyResponsibilityCategory keyResponsibilityCategory1 = getKeyResponsibilityCategorySample1();
        KeyResponsibilityCategory keyResponsibilityCategory2 = new KeyResponsibilityCategory();
        assertThat(keyResponsibilityCategory1).isNotEqualTo(keyResponsibilityCategory2);

        keyResponsibilityCategory2.setId(keyResponsibilityCategory1.getId());
        assertThat(keyResponsibilityCategory1).isEqualTo(keyResponsibilityCategory2);

        keyResponsibilityCategory2 = getKeyResponsibilityCategorySample2();
        assertThat(keyResponsibilityCategory1).isNotEqualTo(keyResponsibilityCategory2);
    }
}
