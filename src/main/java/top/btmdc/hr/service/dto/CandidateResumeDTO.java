package top.btmdc.hr.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link top.btmdc.hr.domain.CandidateResume} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidateResumeDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer versionNumber;

    @NotNull
    private Instant receivedAt;

    @NotNull
    @Size(max = 255)
    private String originalFileName;

    @Size(max = 150)
    private String mediaType;

    @NotNull
    @Min(value = 0L)
    private Long fileSize;

    @NotNull
    @Size(max = 64)
    private String sha256;

    @NotNull
    @Size(max = 120)
    private String storageKey;

    @Lob
    private String structuredSummary;

    @NotNull
    private Boolean activeVersion;

    @NotNull
    @Size(max = 100)
    private String uploadedBy;

    private ExternalCandidateDTO candidate;

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

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public String getStructuredSummary() {
        return structuredSummary;
    }

    public void setStructuredSummary(String structuredSummary) {
        this.structuredSummary = structuredSummary;
    }

    public Boolean getActiveVersion() {
        return activeVersion;
    }

    public void setActiveVersion(Boolean activeVersion) {
        this.activeVersion = activeVersion;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public ExternalCandidateDTO getCandidate() {
        return candidate;
    }

    public void setCandidate(ExternalCandidateDTO candidate) {
        this.candidate = candidate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidateResumeDTO)) {
            return false;
        }

        CandidateResumeDTO candidateResumeDTO = (CandidateResumeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidateResumeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateResumeDTO{" +
            "id=" + getId() +
            ", versionNumber=" + getVersionNumber() +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", mediaType='" + getMediaType() + "'" +
            ", fileSize=" + getFileSize() +
            ", sha256='" + getSha256() + "'" +
            ", storageKey='" + getStorageKey() + "'" +
            ", structuredSummary='" + getStructuredSummary() + "'" +
            ", activeVersion='" + getActiveVersion() + "'" +
            ", uploadedBy='" + getUploadedBy() + "'" +
            ", candidate=" + getCandidate() +
            "}";
    }
}
