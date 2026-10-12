package top.btmdc.hr.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/** PII-free result of a scheduled recruitment retention cleanup run. */
@Entity
@Table(name = "recruitment_data_deletion_log")
public class RecruitmentDataDeletionLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "batch_id", length = 36, nullable = false, unique = true)
    private String batchId;

    @Column(name = "executed_at", nullable = false)
    private Instant executedAt;

    @Column(name = "executor", length = 40, nullable = false)
    private String executor;

    @Column(name = "deleted_candidates", nullable = false)
    private Integer deletedCandidates;

    @Column(name = "deleted_resumes", nullable = false)
    private Integer deletedResumes;

    @Column(name = "failure_count", nullable = false)
    private Integer failureCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public Integer getDeletedCandidates() {
        return deletedCandidates;
    }

    public void setDeletedCandidates(Integer deletedCandidates) {
        this.deletedCandidates = deletedCandidates;
    }

    public Integer getDeletedResumes() {
        return deletedResumes;
    }

    public void setDeletedResumes(Integer deletedResumes) {
        this.deletedResumes = deletedResumes;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }
}
