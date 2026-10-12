package top.btmdc.hr.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CandidateResumeCriteriaTest {

    @Test
    void newCandidateResumeCriteriaHasAllFiltersNullTest() {
        var candidateResumeCriteria = new CandidateResumeCriteria();
        assertThat(candidateResumeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void candidateResumeCriteriaFluentMethodsCreatesFiltersTest() {
        var candidateResumeCriteria = new CandidateResumeCriteria();

        setAllFilters(candidateResumeCriteria);

        assertThat(candidateResumeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void candidateResumeCriteriaCopyCreatesNullFilterTest() {
        var candidateResumeCriteria = new CandidateResumeCriteria();
        var copy = candidateResumeCriteria.copy();

        assertThat(candidateResumeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(candidateResumeCriteria)
        );
    }

    @Test
    void candidateResumeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var candidateResumeCriteria = new CandidateResumeCriteria();
        setAllFilters(candidateResumeCriteria);

        var copy = candidateResumeCriteria.copy();

        assertThat(candidateResumeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(candidateResumeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var candidateResumeCriteria = new CandidateResumeCriteria();

        assertThat(candidateResumeCriteria).hasToString("CandidateResumeCriteria{}");
    }

    private static void setAllFilters(CandidateResumeCriteria candidateResumeCriteria) {
        candidateResumeCriteria.id();
        candidateResumeCriteria.versionNumber();
        candidateResumeCriteria.receivedAt();
        candidateResumeCriteria.originalFileName();
        candidateResumeCriteria.mediaType();
        candidateResumeCriteria.fileSize();
        candidateResumeCriteria.sha256();
        candidateResumeCriteria.storageKey();
        candidateResumeCriteria.activeVersion();
        candidateResumeCriteria.uploadedBy();
        candidateResumeCriteria.candidateId();
        candidateResumeCriteria.distinct();
    }

    private static Condition<CandidateResumeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVersionNumber()) &&
                condition.apply(criteria.getReceivedAt()) &&
                condition.apply(criteria.getOriginalFileName()) &&
                condition.apply(criteria.getMediaType()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getSha256()) &&
                condition.apply(criteria.getStorageKey()) &&
                condition.apply(criteria.getActiveVersion()) &&
                condition.apply(criteria.getUploadedBy()) &&
                condition.apply(criteria.getCandidateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CandidateResumeCriteria> copyFiltersAre(
        CandidateResumeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVersionNumber(), copy.getVersionNumber()) &&
                condition.apply(criteria.getReceivedAt(), copy.getReceivedAt()) &&
                condition.apply(criteria.getOriginalFileName(), copy.getOriginalFileName()) &&
                condition.apply(criteria.getMediaType(), copy.getMediaType()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getSha256(), copy.getSha256()) &&
                condition.apply(criteria.getStorageKey(), copy.getStorageKey()) &&
                condition.apply(criteria.getActiveVersion(), copy.getActiveVersion()) &&
                condition.apply(criteria.getUploadedBy(), copy.getUploadedBy()) &&
                condition.apply(criteria.getCandidateId(), copy.getCandidateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
