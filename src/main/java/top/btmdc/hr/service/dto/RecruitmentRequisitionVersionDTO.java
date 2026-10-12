package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.RecruitmentRequisitionVersion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecruitmentRequisitionVersionDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer versionNumber;

    @Lob
    private String snapshotJson;

    @NotNull
    private Instant lockedAt;

    @NotNull
    @Size(max = 100)
    private String lockedBy;

    @Lob
    private String changeReason;

    private RecruitmentRequisitionDTO requisition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getSnapshotJson() {
        return snapshotJson;
    }

    public void setSnapshotJson(String snapshotJson) {
        this.snapshotJson = snapshotJson;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Instant lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public RecruitmentRequisitionDTO getRequisition() {
        return requisition;
    }

    public void setRequisition(RecruitmentRequisitionDTO requisition) {
        this.requisition = requisition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentRequisitionVersionDTO)) {
            return false;
        }

        RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO = (RecruitmentRequisitionVersionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recruitmentRequisitionVersionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecruitmentRequisitionVersionDTO{" +
            "id=" + getId() +
            ", versionNumber=" + getVersionNumber() +
            ", snapshotJson='" + getSnapshotJson() + "'" +
            ", lockedAt='" + getLockedAt() + "'" +
            ", lockedBy='" + getLockedBy() + "'" +
            ", changeReason='" + getChangeReason() + "'" +
            ", requisition=" + getRequisition() +
            "}";
    }
}
