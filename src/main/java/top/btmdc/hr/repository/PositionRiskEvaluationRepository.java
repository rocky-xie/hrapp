package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PositionRiskEvaluation;

/**
 * Spring Data JPA repository for the PositionRiskEvaluation entity.
 */
@Repository
public interface PositionRiskEvaluationRepository
    extends JpaRepository<PositionRiskEvaluation, Long>, JpaSpecificationExecutor<PositionRiskEvaluation>
{
    default Optional<PositionRiskEvaluation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PositionRiskEvaluation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PositionRiskEvaluation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select positionRiskEvaluation from PositionRiskEvaluation positionRiskEvaluation left join fetch positionRiskEvaluation.position",
        countQuery = "select count(positionRiskEvaluation) from PositionRiskEvaluation positionRiskEvaluation"
    )
    Page<PositionRiskEvaluation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select positionRiskEvaluation from PositionRiskEvaluation positionRiskEvaluation left join fetch positionRiskEvaluation.position"
    )
    List<PositionRiskEvaluation> findAllWithToOneRelationships();

    @Query(
        "select positionRiskEvaluation from PositionRiskEvaluation positionRiskEvaluation left join fetch positionRiskEvaluation.position where positionRiskEvaluation.id =:id"
    )
    Optional<PositionRiskEvaluation> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<PositionRiskEvaluation> findFirstByPositionIdOrderByEvaluationDateDescIdDesc(Long positionId);
}
