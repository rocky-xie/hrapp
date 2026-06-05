package top.btmdc.hr.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import top.btmdc.hr.domain.enumeration.LevelCode;

/**
 * Criteria class for the {@link top.btmdc.hr.domain.SkillLevel} entity. This class is used
 * in {@link top.btmdc.hr.web.rest.SkillLevelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /skill-levels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillLevelCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LevelCode
     */
    public static class LevelCodeFilter extends Filter<LevelCode> {

        public LevelCodeFilter() {}

        public LevelCodeFilter(LevelCodeFilter filter) {
            super(filter);
        }

        @Override
        public LevelCodeFilter copy() {
            return new LevelCodeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LevelCodeFilter code;

    private StringFilter levelName;

    private IntegerFilter sortOrder;

    private Boolean distinct;

    public SkillLevelCriteria() {}

    public SkillLevelCriteria(SkillLevelCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(LevelCodeFilter::copy).orElse(null);
        this.levelName = other.optionalLevelName().map(StringFilter::copy).orElse(null);
        this.sortOrder = other.optionalSortOrder().map(IntegerFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SkillLevelCriteria copy() {
        return new SkillLevelCriteria(this);
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

    public LevelCodeFilter getCode() {
        return code;
    }

    public Optional<LevelCodeFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public LevelCodeFilter code() {
        if (code == null) {
            setCode(new LevelCodeFilter());
        }
        return code;
    }

    public void setCode(LevelCodeFilter code) {
        this.code = code;
    }

    public StringFilter getLevelName() {
        return levelName;
    }

    public Optional<StringFilter> optionalLevelName() {
        return Optional.ofNullable(levelName);
    }

    public StringFilter levelName() {
        if (levelName == null) {
            setLevelName(new StringFilter());
        }
        return levelName;
    }

    public void setLevelName(StringFilter levelName) {
        this.levelName = levelName;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public Optional<IntegerFilter> optionalSortOrder() {
        return Optional.ofNullable(sortOrder);
    }

    public IntegerFilter sortOrder() {
        if (sortOrder == null) {
            setSortOrder(new IntegerFilter());
        }
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
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
        final SkillLevelCriteria that = (SkillLevelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(levelName, that.levelName) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, levelName, sortOrder, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillLevelCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalLevelName().map(f -> "levelName=" + f + ", ").orElse("") +
            optionalSortOrder().map(f -> "sortOrder=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
