package top.btmdc.hr.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import top.btmdc.hr.domain.CandidateResume;
import top.btmdc.hr.domain.RecruitmentDataDeletionLog;
import top.btmdc.hr.repository.CandidateResumeRepository;
import top.btmdc.hr.repository.RecruitmentDataDeletionLogRepository;

/** Deletes expired, unconverted candidate archives and retains only PII-free batch totals. */
@Service
public class RecruitmentRetentionCleanupService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRetentionCleanupService.class);
    private static final String ELIGIBLE_CANDIDATES = """
        select c.id from external_candidate c
        where c.converted_person_id is null
          and exists (select 1 from recruitment_application a where a.candidate_id = c.id)
          and not exists (select 1 from recruitment_application a where a.candidate_id = c.id
              and (a.closed_at is null or a.retention_until is null or a.retention_until > current_date or a.converted_person_id is not null))
        order by c.id
        """;

    private final JdbcTemplate jdbc;
    private final CandidateResumeRepository resumes;
    private final RecruitmentFileStorage storage;
    private final RecruitmentDataDeletionLogRepository logs;
    private final TransactionTemplate transactions;
    private final boolean enabled;

    public RecruitmentRetentionCleanupService(
        JdbcTemplate jdbc,
        CandidateResumeRepository resumes,
        RecruitmentFileStorage storage,
        RecruitmentDataDeletionLogRepository logs,
        PlatformTransactionManager transactionManager,
        @Value("${hr.recruitment.retention.enabled:true}") boolean enabled
    ) {
        this.jdbc = jdbc;
        this.resumes = resumes;
        this.storage = storage;
        this.logs = logs;
        this.transactions = new TransactionTemplate(transactionManager);
        this.enabled = enabled;
    }

    @Scheduled(cron = "${hr.recruitment.retention.cron:0 0 2 * * *}")
    public void scheduledCleanup() {
        if (enabled) cleanupExpiredCandidates();
    }

    public CleanupResult cleanupExpiredCandidates() {
        List<Long> candidates = jdbc.queryForList(ELIGIBLE_CANDIDATES, Long.class);
        int deletedCandidates = 0;
        int deletedResumes = 0;
        int failures = 0;
        for (Long candidateId : candidates) {
            try {
                List<CandidateResume> candidateResumes = resumes.findByCandidate_Id(candidateId);
                for (CandidateResume resume : candidateResumes) storage.delete(resume.getStorageKey());
                Integer removed = transactions.execute(status -> deleteCandidateData(candidateId));
                if (removed != null) {
                    deletedCandidates++;
                    deletedResumes += candidateResumes.size();
                }
            } catch (RuntimeException | IOException exception) {
                failures++;
                LOG.warn("Recruitment retention cleanup failed for one candidate archive; batch will retry on the next run", exception);
            }
        }
        RecruitmentDataDeletionLog log = new RecruitmentDataDeletionLog();
        log.setBatchId(UUID.randomUUID().toString());
        log.setExecutedAt(Instant.now());
        log.setExecutor("SYSTEM_SCHEDULED_TASK");
        log.setDeletedCandidates(deletedCandidates);
        log.setDeletedResumes(deletedResumes);
        log.setFailureCount(failures);
        logs.save(log);
        return new CleanupResult(candidates.size(), deletedCandidates, deletedResumes, failures);
    }

    private int deleteCandidateData(Long candidateId) {
        List<Long> applicationIds = jdbc.queryForList(
            "select id from recruitment_application where candidate_id = ?",
            Long.class,
            candidateId
        );
        if (applicationIds.isEmpty()) return 0;
        String marks = String.join(",", java.util.Collections.nCopies(applicationIds.size(), "?"));
        Object[] ids = applicationIds.toArray();
        jdbc.update(
            "delete from dispatch_review where dispatch_assignment_id in (select id from dispatch_assignment where application_id in (" +
                marks +
                "))",
            ids
        );
        for (String table : List.of(
            "notification_message",
            "preboarding_follow_up",
            "hiring_decision",
            "recruitment_evaluation",
            "selection_activity",
            "application_event",
            "application_stage",
            "dispatch_assignment"
        )) {
            jdbc.update("delete from " + table + " where application_id in (" + marks + ")", ids);
        }
        jdbc.update("delete from recruitment_application where id in (" + marks + ")", ids);
        jdbc.update("delete from candidate_resume where candidate_id = ?", candidateId);
        return jdbc.update("delete from external_candidate where id = ? and converted_person_id is null", candidateId);
    }

    public record CleanupResult(int eligibleCandidates, int deletedCandidates, int deletedResumes, int failures) {}
}
