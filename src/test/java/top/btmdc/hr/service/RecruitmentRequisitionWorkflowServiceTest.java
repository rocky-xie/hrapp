package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.domain.enumeration.RequisitionEditStatus;
import top.btmdc.hr.domain.enumeration.RequisitionLifecycle;
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;
import top.btmdc.hr.repository.RecruitmentRequisitionVersionRepository;
import top.btmdc.hr.repository.RequisitionEditEventRepository;

@ExtendWith(MockitoExtension.class)
class RecruitmentRequisitionWorkflowServiceTest {

    @Mock
    private RecruitmentRequisitionRepository requisitions;

    @Mock
    private RecruitmentRequisitionVersionRepository versions;

    @Mock
    private RequisitionEditEventRepository editEvents;

    private RecruitmentRequisitionWorkflowService service;
    private RecruitmentRequisition requisition;

    @BeforeEach
    void setUp() {
        service = new RecruitmentRequisitionWorkflowService(requisitions, versions, editEvents);
        requisition = new RecruitmentRequisition()
            .id(9L)
            .requisitionCode("REQ-9")
            .title("Platform Engineer")
            .department("Engineering")
            .recruitmentType(RecruitmentType.MID_CAREER)
            .lifecycle(RequisitionLifecycle.PREPARING)
            .editStatus(RequisitionEditStatus.EDITABLE)
            .plannedHeadcount(2)
            .currentVersion(0)
            .requirementsJson("{\"skills\":[\"Java\"]}");
    }

    @Test
    void lockCreatesImmutableSnapshotAndPublishesRequisition() {
        when(requisitions.findById(9L)).thenReturn(Optional.of(requisition));
        when(requisitions.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentRequisition locked = service.lock(9L, "hr-user");

        assertThat(locked.getEditStatus()).isEqualTo(RequisitionEditStatus.LOCKED);
        assertThat(locked.getLifecycle()).isEqualTo(RequisitionLifecycle.ACTIVE);
        assertThat(locked.getCurrentVersion()).isEqualTo(1);
        assertThat(locked.getLockedBy()).isEqualTo("hr-user");
        ArgumentCaptor<RecruitmentRequisitionVersion> snapshot = ArgumentCaptor.forClass(RecruitmentRequisitionVersion.class);
        verify(versions).save(snapshot.capture());
        verify(editEvents).save(any());
        assertThat(snapshot.getValue().getVersionNumber()).isEqualTo(1);
        assertThat(snapshot.getValue().getSnapshotJson()).contains("REQ-9", "Platform Engineer", "Java");
        assertThat(snapshot.getValue().getLockedBy()).isEqualTo("hr-user");
    }

    @Test
    void lockRejectsAlreadyLockedRequisition() {
        requisition.setEditStatus(RequisitionEditStatus.LOCKED);
        when(requisitions.findById(9L)).thenReturn(Optional.of(requisition));

        assertThatThrownBy(() -> service.lock(9L, "hr-user")).isInstanceOf(IllegalStateException.class);
        verifyNoInteractions(versions, editEvents);
    }

    @Test
    void unlockRequiresReasonAndReopensForEditing() {
        requisition.setEditStatus(RequisitionEditStatus.LOCKED);
        when(requisitions.findById(9L)).thenReturn(Optional.of(requisition));
        when(requisitions.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RecruitmentRequisition unlocked = service.unlock(9L, "hr-user", "Headcount changed");

        assertThat(unlocked.getEditStatus()).isEqualTo(RequisitionEditStatus.EDITABLE);
        assertThat(unlocked.getUnlockReason()).contains("hr-user", "Headcount changed");
        assertThat(unlocked.getUnlockedAt()).isNotNull();
        verify(editEvents).save(any());
    }

    @Test
    void unlockRejectsBlankReason() {
        assertThatThrownBy(() -> service.unlock(9L, "hr-user", " ")).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(requisitions);
    }
}
