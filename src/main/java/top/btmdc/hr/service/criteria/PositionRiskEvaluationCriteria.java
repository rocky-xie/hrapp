package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.DocumentStatus;
import top.btmdc.hr.domain.enumeration.ImportanceLevel;
import top.btmdc.hr.domain.enumeration.ReadinessLevel;
import top.btmdc.hr.domain.enumeration.RiskLevel;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.PositionRiskEvaluation} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.PositionRiskEvaluationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /position-risk-evaluations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PositionRiskEvaluationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentStatus
     */
    public static class DocumentStatusFilter extends Filter<DocumentStatus> {

        public DocumentStatusFilter() {}

        public DocumentStatusFilter(DocumentStatusFilter filter) {
            super(filter);
        }

        @Override
        public DocumentStatusFilter copy() {
            return new DocumentStatusFilter(this);
        }
    }

    /**
     * Class for filtering ImportanceLevel
     */
    public static class ImportanceLevelFilter extends Filter<ImportanceLevel> {

        public ImportanceLevelFilter() {}

        public ImportanceLevelFilter(ImportanceLevelFilter filter) {
            super(filter);
        }

        @Override
        public ImportanceLevelFilter copy() {
            return new ImportanceLevelFilter(this);
        }
    }

    /**
     * Class for filtering ReadinessLevel
     */
    public static class ReadinessLevelFilter extends Filter<ReadinessLevel> {

        public ReadinessLevelFilter() {}

        public ReadinessLevelFilter(ReadinessLevelFilter filter) {
            super(filter);
        }

        @Override
        public ReadinessLevelFilter copy() {
            return new ReadinessLevelFilter(this);
        }
    }

    /**
     * Class for filtering RiskLevel
     */
    public static class RiskLevelFilter extends Filter<RiskLevel> {

        public RiskLevelFilter() {}

        public RiskLevelFilter(RiskLevelFilter filter) {
            super(filter);
        }

        @Override
        public RiskLevelFilter copy() {
            return new RiskLevelFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter evaluationDate;

    private IntegerFilter ownerCount;

    private IntegerFilter substitutableOwnerCount;

    private BooleanFilter hasSubstitute;

    private DocumentStatusFilter documentStatus;

    private ImportanceLevelFilter customerOrSystemDependency;

    private ReadinessLevelFilter successionReadiness;

    private RiskLevelFilter riskLevel;

    private LongFilter positionId;

    private Boolean distinct;

    public PositionRiskEvaluationCriteria() {}

    public PositionRiskEvaluationCriteria(PositionRiskEvaluationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.evaluationDate = other.optionalEvaluationDate().map(LocalDateFilter::copy).orElse(null);
        this.ownerCount = other.optionalOwnerCount().map(IntegerFilter::copy).orElse(null);
        this.substitutableOwnerCount = other.optionalSubstitutableOwnerCount().map(IntegerFilter::copy).orElse(null);
        this.hasSubstitute = other.optionalHasSubstitute().map(BooleanFilter::copy).orElse(null);
        this.documentStatus = other.optionalDocumentStatus().map(DocumentStatusFilter::copy).orElse(null);
        this.customerOrSystemDependency = other.optionalCustomerOrSystemDependency().map(ImportanceLevelFilter::copy).orElse(null);
        this.successionReadiness = other.optionalSuccessionReadiness().map(ReadinessLevelFilter::copy).orElse(null);
        this.riskLevel = other.optionalRiskLevel().map(RiskLevelFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PositionRiskEvaluationCriteria copy() {
        return new PositionRiskEvaluationCriteria(this);
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

    public LocalDateFilter getEvaluationDate() {
        return evaluationDate;
    }

    public Optional<LocalDateFilter> optionalEvaluationDate() {
        return Optional.ofNullable(evaluationDate);
    }

    public LocalDateFilter evaluationDate() {
        if (evaluationDate == null) {
            setEvaluationDate(new LocalDateFilter());
        }
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateFilter evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public IntegerFilter getOwnerCount() {
        return ownerCount;
    }

    public Optional<IntegerFilter> optionalOwnerCount() {
        return Optional.ofNullable(ownerCount);
    }

    public IntegerFilter ownerCount() {
        if (ownerCount == null) {
            setOwnerCount(new IntegerFilter());
        }
        return ownerCount;
    }

    public void setOwnerCount(IntegerFilter ownerCount) {
        this.ownerCount = ownerCount;
    }

    public IntegerFilter getSubstitutableOwnerCount() {
        return substitutableOwnerCount;
    }

    public Optional<IntegerFilter> optionalSubstitutableOwnerCount() {
        return Optional.ofNullable(substitutableOwnerCount);
    }

    public IntegerFilter substitutableOwnerCount() {
        if (substitutableOwnerCount == null) {
            setSubstitutableOwnerCount(new IntegerFilter());
        }
        return substitutableOwnerCount;
    }

    public void setSubstitutableOwnerCount(IntegerFilter substitutableOwnerCount) {
        this.substitutableOwnerCount = substitutableOwnerCount;
    }

    public BooleanFilter getHasSubstitute() {
        return hasSubstitute;
    }

    public Optional<BooleanFilter> optionalHasSubstitute() {
        return Optional.ofNullable(hasSubstitute);
    }

    public BooleanFilter hasSubstitute() {
        if (hasSubstitute == null) {
            setHasSubstitute(new BooleanFilter());
        }
        return hasSubstitute;
    }

    public void setHasSubstitute(BooleanFilter hasSubstitute) {
        this.hasSubstitute = hasSubstitute;
    }

    public DocumentStatusFilter getDocumentStatus() {
        return documentStatus;
    }

    public Optional<DocumentStatusFilter> optionalDocumentStatus() {
        return Optional.ofNullable(documentStatus);
    }

    public DocumentStatusFilter documentStatus() {
        if (documentStatus == null) {
            setDocumentStatus(new DocumentStatusFilter());
        }
        return documentStatus;
    }

    public void setDocumentStatus(DocumentStatusFilter documentStatus) {
        this.documentStatus = documentStatus;
    }

    public ImportanceLevelFilter getCustomerOrSystemDependency() {
        return customerOrSystemDependency;
    }

    public Optional<ImportanceLevelFilter> optionalCustomerOrSystemDependency() {
        return Optional.ofNullable(customerOrSystemDependency);
    }

    public ImportanceLevelFilter customerOrSystemDependency() {
        if (customerOrSystemDependency == null) {
            setCustomerOrSystemDependency(new ImportanceLevelFilter());
        }
        return customerOrSystemDependency;
    }

    public void setCustomerOrSystemDependency(ImportanceLevelFilter customerOrSystemDependency) {
        this.customerOrSystemDependency = customerOrSystemDependency;
    }

    public ReadinessLevelFilter getSuccessionReadiness() {
        return successionReadiness;
    }

    public Optional<ReadinessLevelFilter> optionalSuccessionReadiness() {
        return Optional.ofNullable(successionReadiness);
    }

    public ReadinessLevelFilter successionReadiness() {
        if (successionReadiness == null) {
            setSuccessionReadiness(new ReadinessLevelFilter());
        }
        return successionReadiness;
    }

    public void setSuccessionReadiness(ReadinessLevelFilter successionReadiness) {
        this.successionReadiness = successionReadiness;
    }

    public RiskLevelFilter getRiskLevel() {
        return riskLevel;
    }

    public Optional<RiskLevelFilter> optionalRiskLevel() {
        return Optional.ofNullable(riskLevel);
    }

    public RiskLevelFilter riskLevel() {
        if (riskLevel == null) {
            setRiskLevel(new RiskLevelFilter());
        }
        return riskLevel;
    }

    public void setRiskLevel(RiskLevelFilter riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LongFilter getPositionId() {
        return positionId;
    }

    public Optional<LongFilter> optionalPositionId() {
        return Optional.ofNullable(positionId);
    }

    public LongFilter positionId() {
        if (positionId == null) {
            setPositionId(new LongFilter());
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
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
        final PositionRiskEvaluationCriteria that = (PositionRiskEvaluationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(evaluationDate, that.evaluationDate) &&
            Objects.equals(ownerCount, that.ownerCount) &&
            Objects.equals(substitutableOwnerCount, that.substitutableOwnerCount) &&
            Objects.equals(hasSubstitute, that.hasSubstitute) &&
            Objects.equals(documentStatus, that.documentStatus) &&
            Objects.equals(customerOrSystemDependency, that.customerOrSystemDependency) &&
            Objects.equals(successionReadiness, that.successionReadiness) &&
            Objects.equals(riskLevel, that.riskLevel) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            evaluationDate,
            ownerCount,
            substitutableOwnerCount,
            hasSubstitute,
            documentStatus,
            customerOrSystemDependency,
            successionReadiness,
            riskLevel,
            positionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PositionRiskEvaluationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEvaluationDate().map(f -> "evaluationDate=" + f + ", ").orElse("") +
            optionalOwnerCount().map(f -> "ownerCount=" + f + ", ").orElse("") +
            optionalSubstitutableOwnerCount().map(f -> "substitutableOwnerCount=" + f + ", ").orElse("") +
            optionalHasSubstitute().map(f -> "hasSubstitute=" + f + ", ").orElse("") +
            optionalDocumentStatus().map(f -> "documentStatus=" + f + ", ").orElse("") +
            optionalCustomerOrSystemDependency().map(f -> "customerOrSystemDependency=" + f + ", ").orElse("") +
            optionalSuccessionReadiness().map(f -> "successionReadiness=" + f + ", ").orElse("") +
            optionalRiskLevel().map(f -> "riskLevel=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
