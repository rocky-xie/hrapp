package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.domain.RecruitmentDataDeletionLog;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.repository.RecruitmentDataDeletionLogRepository;

class RecruitmentRetentionCleanupServiceTest {

    @Mock
    private JdbcTemplate jdbc;

    @Mock
    private CandidateResumeRepository resumes;

    @Mock
    private RecruitmentFileStorage storage;

    @Mock
    private RecruitmentDataDeletionLogRepository logs;

    private RecruitmentRetentionCleanupService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PlatformTransactionManager transactions = new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) {}

            @Override
            public void rollback(TransactionStatus status) {}
        };
        service = new RecruitmentRetentionCleanupService(jdbc, resumes, storage, logs, transactions, true);
    }

    @Test
    void deletesExpiredCandidateArchiveAndRecordsOnlyBatchTotals() throws Exception {
        when(jdbc.queryForList(contains("external_candidate"), eq(Long.class))).thenReturn(List.of(17L));
        when(jdbc.queryForList(contains("select id from recruitment_application"), eq(Long.class), eq(17L))).thenReturn(List.of(27L));
        CandidateResume resume = new CandidateResume().storageKey("a8d22ae1-3355-4cf4-a471-fcba5877f1f4");
        when(resumes.findByCandidate_Id(17L)).thenReturn(List.of(resume));
        when(jdbc.update(anyString(), any(Object[].class))).thenReturn(1);

        RecruitmentRetentionCleanupService.CleanupResult result = service.cleanupExpiredCandidates();

        assertThat(result).isEqualTo(new RecruitmentRetentionCleanupService.CleanupResult(1, 1, 1, 0));
        verify(storage).delete(resume.getStorageKey());
        verify(logs).save(
            argThat(
                log ->
                    log.getExecutor().equals("SYSTEM_SCHEDULED_TASK") &&
                    log.getDeletedCandidates() == 1 &&
                    log.getDeletedResumes() == 1 &&
                    log.getFailureCount() == 0
            )
        );
        verify(jdbc).update(startsWith("delete from external_candidate"), eq(17L));
    }

    @Test
    void keepsConvertedOrNotYetEligibleCandidatesOutOfTheDeletionPath() {
        when(jdbc.queryForList(anyString(), eq(Long.class))).thenReturn(List.of());

        RecruitmentRetentionCleanupService.CleanupResult result = service.cleanupExpiredCandidates();

        assertThat(result.eligibleCandidates()).isZero();
        assertThat(result.deletedCandidates()).isZero();
        verifyNoInteractions(resumes, storage);
        verify(logs).save(any(RecruitmentDataDeletionLog.class));
    }
}
