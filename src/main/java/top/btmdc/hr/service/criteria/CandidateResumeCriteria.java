package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.CandidateResume} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.CandidateResumeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /candidate-resumes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidateResumeCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter versionNumber;

    private InstantFilter receivedAt;

    private StringFilter originalFileName;

    private StringFilter mediaType;

    private LongFilter fileSize;

    private StringFilter sha256;

    private StringFilter storageKey;

    private BooleanFilter activeVersion;

    private StringFilter uploadedBy;

    private LongFilter candidateId;

    private Boolean distinct;

    public CandidateResumeCriteria() {}

    public CandidateResumeCriteria(CandidateResumeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.versionNumber = other.optionalVersionNumber().map(IntegerFilter::copy).orElse(null);
        this.receivedAt = other.optionalReceivedAt().map(InstantFilter::copy).orElse(null);
        this.originalFileName = other.optionalOriginalFileName().map(StringFilter::copy).orElse(null);
        this.mediaType = other.optionalMediaType().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.sha256 = other.optionalSha256().map(StringFilter::copy).orElse(null);
        this.storageKey = other.optionalStorageKey().map(StringFilter::copy).orElse(null);
        this.activeVersion = other.optionalActiveVersion().map(BooleanFilter::copy).orElse(null);
        this.uploadedBy = other.optionalUploadedBy().map(StringFilter::copy).orElse(null);
        this.candidateId = other.optionalCandidateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CandidateResumeCriteria copy() {
        return new CandidateResumeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getVersionNumber() {
        return versionNumber;
    }

    public Optional<IntegerFilter> optionalVersionNumber() {
        return Optional.ofNullable(versionNumber);
    }

    public IntegerFilter versionNumber() {
        if (versionNumber == null) {
            setVersionNumber(new IntegerFilter());
        }
        return versionNumber;
    }

    public void setVersionNumber(IntegerFilter versionNumber) {
        this.versionNumber = versionNumber;
    }

    public InstantFilter getReceivedAt() {
        return receivedAt;
    }

    public Optional<InstantFilter> optionalReceivedAt() {
        return Optional.ofNullable(receivedAt);
    }

    public InstantFilter receivedAt() {
        if (receivedAt == null) {
            setReceivedAt(new InstantFilter());
        }
        return receivedAt;
    }

    public void setReceivedAt(InstantFilter receivedAt) {
        this.receivedAt = receivedAt;
    }

    public StringFilter getOriginalFileName() {
        return originalFileName;
    }

    public Optional<StringFilter> optionalOriginalFileName() {
        return Optional.ofNullable(originalFileName);
    }

    public StringFilter originalFileName() {
        if (originalFileName == null) {
            setOriginalFileName(new StringFilter());
        }
        return originalFileName;
    }

    public void setOriginalFileName(StringFilter originalFileName) {
        this.originalFileName = originalFileName;
    }

    public StringFilter getMediaType() {
        return mediaType;
    }

    public Optional<StringFilter> optionalMediaType() {
        return Optional.ofNullable(mediaType);
    }

    public StringFilter mediaType() {
        if (mediaType == null) {
            setMediaType(new StringFilter());
        }
        return mediaType;
    }

    public void setMediaType(StringFilter mediaType) {
        this.mediaType = mediaType;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public StringFilter getSha256() {
        return sha256;
    }

    public Optional<StringFilter> optionalSha256() {
        return Optional.ofNullable(sha256);
    }

    public StringFilter sha256() {
        if (sha256 == null) {
            setSha256(new StringFilter());
        }
        return sha256;
    }

    public void setSha256(StringFilter sha256) {
        this.sha256 = sha256;
    }

    public StringFilter getStorageKey() {
        return storageKey;
    }

    public Optional<StringFilter> optionalStorageKey() {
        return Optional.ofNullable(storageKey);
    }

    public StringFilter storageKey() {
        if (storageKey == null) {
            setStorageKey(new StringFilter());
        }
        return storageKey;
    }

    public void setStorageKey(StringFilter storageKey) {
        this.storageKey = storageKey;
    }

    public BooleanFilter getActiveVersion() {
        return activeVersion;
    }

    public Optional<BooleanFilter> optionalActiveVersion() {
        return Optional.ofNullable(activeVersion);
    }

    public BooleanFilter activeVersion() {
        if (activeVersion == null) {
            setActiveVersion(new BooleanFilter());
        }
        return activeVersion;
    }

    public void setActiveVersion(BooleanFilter activeVersion) {
        this.activeVersion = activeVersion;
    }

    public StringFilter getUploadedBy() {
        return uploadedBy;
    }

    public Optional<StringFilter> optionalUploadedBy() {
        return Optional.ofNullable(uploadedBy);
    }

    public StringFilter uploadedBy() {
        if (uploadedBy == null) {
            setUploadedBy(new StringFilter());
        }
        return uploadedBy;
    }

    public void setUploadedBy(StringFilter uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LongFilter getCandidateId() {
        return candidateId;
    }

    public Optional<LongFilter> optionalCandidateId() {
        return Optional.ofNullable(candidateId);
    }

    public LongFilter candidateId() {
        if (candidateId == null) {
            setCandidateId(new LongFilter());
        }
        return candidateId;
    }

    public void setCandidateId(LongFilter candidateId) {
        this.candidateId = candidateId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CandidateResumeCriteria that = (CandidateResumeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(versionNumber, that.versionNumber) &&
            Objects.equals(receivedAt, that.receivedAt) &&
            Objects.equals(originalFileName, that.originalFileName) &&
            Objects.equals(mediaType, that.mediaType) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(sha256, that.sha256) &&
            Objects.equals(storageKey, that.storageKey) &&
            Objects.equals(activeVersion, that.activeVersion) &&
            Objects.equals(uploadedBy, that.uploadedBy) &&
            Objects.equals(candidateId, that.candidateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            versionNumber,
            receivedAt,
            originalFileName,
            mediaType,
            fileSize,
            sha256,
            storageKey,
            activeVersion,
            uploadedBy,
            candidateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidateResumeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalVersionNumber().map(f -> "versionNumber=" + f + ", ").orElse("") +
            optionalReceivedAt().map(f -> "receivedAt=" + f + ", ").orElse("") +
            optionalOriginalFileName().map(f -> "originalFileName=" + f + ", ").orElse("") +
            optionalMediaType().map(f -> "mediaType=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalSha256().map(f -> "sha256=" + f + ", ").orElse("") +
            optionalStorageKey().map(f -> "storageKey=" + f + ", ").orElse("") +
            optionalActiveVersion().map(f -> "activeVersion=" + f + ", ").orElse("") +
            optionalUploadedBy().map(f -> "uploadedBy=" + f + ", ").orElse("") +
            optionalCandidateId().map(f -> "candidateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
