package top.btmdc.hr.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static top.btmdc.hr.domain.ActionItemTestSamples.*;

import org.junit.jupiter.api.Test;
import top.btmdc.hr.web.rest.TestUtil;

class ActionItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActionItem.class);
        ActionItem actionItem1 = getActionItemSample1();
        ActionItem actionItem2 = new ActionItem();
        assertThat(actionItem1).isNotEqualTo(actionItem2);

        actionItem2.setId(actionItem1.getId());
        assertThat(actionItem1).isEqualTo(actionItem2);

        actionItem2 = getActionItemSample2();
        assertThat(actionItem1).isNotEqualTo(actionItem2);
    }
}
