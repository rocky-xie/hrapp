package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A RecruitmentRequisitionVersion.
 */
@Entity
@Table(name = "recruitment_requisition_version")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionVersion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Lob
    @Column(name = "snapshot_json", nullable = false)
    private String snapshotJson;

    @NotNull
    @Column(name = "locked_at", nullable = false)
    private Instant lockedAt;

    @NotNull
    @Size(max = 100)
    @Column(name = "locked_by", length = 100, nullable = false)
    private String lockedBy;

    @Lob
    @Column(name = "change_reason")
    private String changeReason;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requisitionVersion")
    @JsonIgnoreProperties(
        value = {
            "stageses",
            "eventses",
            "activitieses",
            "evaluationses",
            "decisionses",
            "messageses",
            "followUpses",
            "dispatchAssignmentses",
            "candidate",
            "requisition",
            "requisitionVersion",
        },
        allowSetters = true
    )
    private Set<RecruitmentApplication> applicationses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "versionses", "memberses", "applicationses" }, allowSetters = true)
    private RecruitmentRequisition requisition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecruitmentRequisitionVersion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersionNumber() {
        return this.versionNumber;
    }

    public RecruitmentRequisitionVersion versionNumber(Integer versionNumber) {
        this.setVersionNumber(versionNumber);
        return this;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getSnapshotJson() {
        return this.snapshotJson;
    }

    public RecruitmentRequisitionVersion snapshotJson(String snapshotJson) {
        this.setSnapshotJson(snapshotJson);
        return this;
    }

    public void setSnapshotJson(String snapshotJson) {
        this.snapshotJson = snapshotJson;
    }

    public Instant getLockedAt() {
        return this.lockedAt;
    }

    public RecruitmentRequisitionVersion lockedAt(Instant lockedAt) {
        this.setLockedAt(lockedAt);
        return this;
    }

    public void setLockedAt(Instant lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getLockedBy() {
        return this.lockedBy;
    }

    public RecruitmentRequisitionVersion lockedBy(String lockedBy) {
        this.setLockedBy(lockedBy);
        return this;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getChangeReason() {
        return this.changeReason;
    }

    public RecruitmentRequisitionVersion changeReason(String changeReason) {
        this.setChangeReason(changeReason);
        return this;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Set<RecruitmentApplication> getApplicationses() {
        return this.applicationses;
    }

    public void setApplicationses(Set<RecruitmentApplication> recruitmentApplications) {
        if (this.applicationses != null) {
            this.applicationses.forEach(i -> i.setRequisitionVersion(null));
        }
        if (recruitmentApplications != null) {
            recruitmentApplications.forEach(i -> i.setRequisitionVersion(this));
        }
        this.applicationses = recruitmentApplications;
    }

    public RecruitmentRequisitionVersion applicationses(Set<RecruitmentApplication> recruitmentApplications) {
        this.setApplicationses(recruitmentApplications);
        return this;
    }

    public RecruitmentRequisitionVersion addApplications(RecruitmentApplication recruitmentApplication) {
        this.applicationses.add(recruitmentApplication);
        recruitmentApplication.setRequisitionVersion(this);
        return this;
    }

    public RecruitmentRequisitionVersion removeApplications(RecruitmentApplication recruitmentApplication) {
        this.applicationses.remove(recruitmentApplication);
        recruitmentApplication.setRequisitionVersion(null);
        return this;
    }

    public RecruitmentRequisition getRequisition() {
        return this.requisition;
    }

    public void setRequisition(RecruitmentRequisition recruitmentRequisition) {
        this.requisition = recruitmentRequisition;
    }

    public RecruitmentRequisitionVersion requisition(RecruitmentRequisition recruitmentRequisition) {
        this.setRequisition(recruitmentRequisition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionVersion)) {
            return false;
        }
        return getId() != null && getId().equals(((RecruitmentRequisitionVersion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionVersion{" +
            "id=" + getId() +
            ", versionNumber=" + getVersionNumber() +
            ", snapshotJson='" + getSnapshotJson() + "'" +
            ", lockedAt='" + getLockedAt() + "'" +
            ", lockedBy='" + getLockedBy() + "'" +
            ", changeReason='" + getChangeReason() + "'" +
            "}";
    }
}
