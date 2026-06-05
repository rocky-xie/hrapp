package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.EvidenceType;
import top.btmdc.hr.domain.enumeration.SkillType;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.Skill} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.SkillResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /skills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SkillType
     */
    public static class SkillTypeFilter extends Filter<SkillType> {

        public SkillTypeFilter() {}

        public SkillTypeFilter(SkillTypeFilter filter) {
            super(filter);
        }

        @Override
        public SkillTypeFilter copy() {
            return new SkillTypeFilter(this);
        }
    }

    /**
     * Class for filtering EvidenceType
     */
    public static class EvidenceTypeFilter extends Filter<EvidenceType> {

        public EvidenceTypeFilter() {}

        public EvidenceTypeFilter(EvidenceTypeFilter filter) {
            super(filter);
        }

        @Override
        public EvidenceTypeFilter copy() {
            return new EvidenceTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter skillCode;

    private StringFilter skillName;

    private SkillTypeFilter skillType;

    private BooleanFilter measurableFlag;

    private EvidenceTypeFilter evidenceType;

    private Boolean distinct;

    public SkillCriteria() {}

    public SkillCriteria(SkillCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.skillCode = other.optionalSkillCode().map(StringFilter::copy).orElse(null);
        this.skillName = other.optionalSkillName().map(StringFilter::copy).orElse(null);
        this.skillType = other.optionalSkillType().map(SkillTypeFilter::copy).orElse(null);
        this.measurableFlag = other.optionalMeasurableFlag().map(BooleanFilter::copy).orElse(null);
        this.evidenceType = other.optionalEvidenceType().map(EvidenceTypeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SkillCriteria copy() {
        return new SkillCriteria(this);
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

    public StringFilter getSkillCode() {
        return skillCode;
    }

    public Optional<StringFilter> optionalSkillCode() {
        return Optional.ofNullable(skillCode);
    }

    public StringFilter skillCode() {
        if (skillCode == null) {
            setSkillCode(new StringFilter());
        }
        return skillCode;
    }

    public void setSkillCode(StringFilter skillCode) {
        this.skillCode = skillCode;
    }

    public StringFilter getSkillName() {
        return skillName;
    }

    public Optional<StringFilter> optionalSkillName() {
        return Optional.ofNullable(skillName);
    }

    public StringFilter skillName() {
        if (skillName == null) {
            setSkillName(new StringFilter());
        }
        return skillName;
    }

    public void setSkillName(StringFilter skillName) {
        this.skillName = skillName;
    }

    public SkillTypeFilter getSkillType() {
        return skillType;
    }

    public Optional<SkillTypeFilter> optionalSkillType() {
        return Optional.ofNullable(skillType);
    }

    public SkillTypeFilter skillType() {
        if (skillType == null) {
            setSkillType(new SkillTypeFilter());
        }
        return skillType;
    }

    public void setSkillType(SkillTypeFilter skillType) {
        this.skillType = skillType;
    }

    public BooleanFilter getMeasurableFlag() {
        return measurableFlag;
    }

    public Optional<BooleanFilter> optionalMeasurableFlag() {
        return Optional.ofNullable(measurableFlag);
    }

    public BooleanFilter measurableFlag() {
        if (measurableFlag == null) {
            setMeasurableFlag(new BooleanFilter());
        }
        return measurableFlag;
    }

    public void setMeasurableFlag(BooleanFilter measurableFlag) {
        this.measurableFlag = measurableFlag;
    }

    public EvidenceTypeFilter getEvidenceType() {
        return evidenceType;
    }

    public Optional<EvidenceTypeFilter> optionalEvidenceType() {
        return Optional.ofNullable(evidenceType);
    }

    public EvidenceTypeFilter evidenceType() {
        if (evidenceType == null) {
            setEvidenceType(new EvidenceTypeFilter());
        }
        return evidenceType;
    }

    public void setEvidenceType(EvidenceTypeFilter evidenceType) {
        this.evidenceType = evidenceType;
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
        final SkillCriteria that = (SkillCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(skillCode, that.skillCode) &&
            Objects.equals(skillName, that.skillName) &&
            Objects.equals(skillType, that.skillType) &&
            Objects.equals(measurableFlag, that.measurableFlag) &&
            Objects.equals(evidenceType, that.evidenceType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skillCode, skillName, skillType, measurableFlag, evidenceType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSkillCode().map(f -> "skillCode=" + f + ", ").orElse("") +
            optionalSkillName().map(f -> "skillName=" + f + ", ").orElse("") +
            optionalSkillType().map(f -> "skillType=" + f + ", ").orElse("") +
            optionalMeasurableFlag().map(f -> "measurableFlag=" + f + ", ").orElse("") +
            optionalEvidenceType().map(f -> "evidenceType=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
