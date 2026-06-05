package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.Evaluation;

/**
 * Spring Data JPA repository for the Evaluation entity.
 */
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, JpaSpecificationExecutor<Evaluation> {
    default Optional<Evaluation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Evaluation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Evaluation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select evaluation from Evaluation evaluation left join fetch evaluation.person left join fetch evaluation.position left join fetch evaluation.trainingGoal left join fetch evaluation.evaluator",
        countQuery = "select count(evaluation) from Evaluation evaluation"
    )
    Page<Evaluation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select evaluation from Evaluation evaluation left join fetch evaluation.person left join fetch evaluation.position left join fetch evaluation.trainingGoal left join fetch evaluation.evaluator"
    )
    List<Evaluation> findAllWithToOneRelationships();

    @Query(
        "select evaluation from Evaluation evaluation left join fetch evaluation.person left join fetch evaluation.position left join fetch evaluation.trainingGoal left join fetch evaluation.evaluator where evaluation.id =:id"
    )
    Optional<Evaluation> findOneWithToOneRelationships(@Param("id") Long id);
}
