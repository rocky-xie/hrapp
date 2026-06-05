package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.ImprovementPlan;

/**
 * Spring Data JPA repository for the ImprovementPlan entity.
 */
@Repository
public interface ImprovementPlanRepository extends JpaRepository<ImprovementPlan, Long>, JpaSpecificationExecutor<ImprovementPlan> {
    default Optional<ImprovementPlan> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ImprovementPlan> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ImprovementPlan> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select improvementPlan from ImprovementPlan improvementPlan left join fetch improvementPlan.position left join fetch improvementPlan.skill",
        countQuery = "select count(improvementPlan) from ImprovementPlan improvementPlan"
    )
    Page<ImprovementPlan> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select improvementPlan from ImprovementPlan improvementPlan left join fetch improvementPlan.position left join fetch improvementPlan.skill"
    )
    List<ImprovementPlan> findAllWithToOneRelationships();

    @Query(
        "select improvementPlan from ImprovementPlan improvementPlan left join fetch improvementPlan.position left join fetch improvementPlan.skill where improvementPlan.id =:id"
    )
    Optional<ImprovementPlan> findOneWithToOneRelationships(@Param("id") Long id);
}
