package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.TrainingGoal;

/**
 * Spring Data JPA repository for the TrainingGoal entity.
 */
@Repository
public interface TrainingGoalRepository extends JpaRepository<TrainingGoal, Long>, JpaSpecificationExecutor<TrainingGoal> {
    default Optional<TrainingGoal> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TrainingGoal> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TrainingGoal> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select trainingGoal from TrainingGoal trainingGoal left join fetch trainingGoal.person left join fetch trainingGoal.position left join fetch trainingGoal.skill left join fetch trainingGoal.targetLevel",
        countQuery = "select count(trainingGoal) from TrainingGoal trainingGoal"
    )
    Page<TrainingGoal> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select trainingGoal from TrainingGoal trainingGoal left join fetch trainingGoal.person left join fetch trainingGoal.position left join fetch trainingGoal.skill left join fetch trainingGoal.targetLevel"
    )
    List<TrainingGoal> findAllWithToOneRelationships();

    @Query(
        "select trainingGoal from TrainingGoal trainingGoal left join fetch trainingGoal.person left join fetch trainingGoal.position left join fetch trainingGoal.skill left join fetch trainingGoal.targetLevel where trainingGoal.id =:id"
    )
    Optional<TrainingGoal> findOneWithToOneRelationships(@Param("id") Long id);
}
