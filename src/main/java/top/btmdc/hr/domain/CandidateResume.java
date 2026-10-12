package top.btmdc.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A CandidateResume.
 */
@Entity
@Table(name = "candidate_resume")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidateResume implements Serializable {

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

    @NotNull
    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @NotNull
    @Size(max = 255)
    @Column(name = "original_file_name", length = 255, nullable = false)
    private String originalFileName;

    @Size(max = 150)
    @Column(name = "media_type", length = 150)
    private String mediaType;

    @NotNull
    @Min(value = 0L)
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @NotNull
    @Size(max = 64)
    @Column(name = "sha_256", length = 64, nullable = false)
    private String sha256;

    @NotNull
    @Size(max = 120)
    @Column(name = "storage_key", length = 120, nullable = false, unique = true)
    private String storageKey;

    @Lob
    @Column(name = "structured_summary")
    private String structuredSummary;

    @NotNull
    @Column(name = "active_version", nullable = false)
    private Boolean activeVersion;

    @NotNull
    @Size(max = 100)
    @Column(name = "uploaded_by", length = 100, nullable = false)
    private String uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "resumeses", "applicationses" }, allowSetters = true)
    private ExternalCandidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CandidateResume id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersionNumber() {
        return this.versionNumber;
    }

    public CandidateResume versionNumber(Integer versionNumber) {
        this.setVersionNumber(versionNumber);
        return this;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Instant getReceivedAt() {
        return this.receivedAt;
    }

    public CandidateResume receivedAt(Instant receivedAt) {
        this.setReceivedAt(receivedAt);
        return this;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public CandidateResume originalFileName(String originalFileName) {
        this.setOriginalFileName(originalFileName);
        return this;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public CandidateResume mediaType(String mediaType) {
        this.setMediaType(mediaType);
        return this;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public CandidateResume fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSha256() {
        return this.sha256;
    }

    public CandidateResume sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getStorageKey() {
        return this.storageKey;
    }

    public CandidateResume storageKey(String storageKey) {
        this.setStorageKey(storageKey);
        return this;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public String getStructuredSummary() {
        return this.structuredSummary;
    }

    public CandidateResume structuredSummary(String structuredSummary) {
        this.setStructuredSummary(structuredSummary);
        return this;
    }

    public void setStructuredSummary(String structuredSummary) {
        this.structuredSummary = structuredSummary;
    }

    public Boolean getActiveVersion() {
        return this.activeVersion;
    }

    public CandidateResume activeVersion(Boolean activeVersion) {
        this.setActiveVersion(activeVersion);
        return this;
    }

    public void setActiveVersion(Boolean activeVersion) {
        this.activeVersion = activeVersion;
    }

    public String getUploadedBy() {
        return this.uploadedBy;
    }

    public CandidateResume uploadedBy(String uploadedBy) {
        this.setUploadedBy(uploadedBy);
        return this;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public ExternalCandidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(ExternalCandidate externalCandidate) {
        this.candidate = externalCandidate;
    }

    public CandidateResume candidate(ExternalCandidate externalCandidate) {
        this.setCandidate(externalCandidate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidateResume)) {
            return false;
        }
        return getId() != null && getId().equals(((CandidateResume) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateResume{" +
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
            "}";
    }
}
