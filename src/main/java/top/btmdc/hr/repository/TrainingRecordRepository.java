package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.TrainingRecord;

/**
 * Spring Data JPA repository for the TrainingRecord entity.
 */
@Repository
public interface TrainingRecordRepository extends JpaRepository<TrainingRecord, Long>, JpaSpecificationExecutor<TrainingRecord> {
    default Optional<TrainingRecord> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TrainingRecord> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TrainingRecord> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select trainingRecord from TrainingRecord trainingRecord left join fetch trainingRecord.person left join fetch trainingRecord.trainingGoal left join fetch trainingRecord.position left join fetch trainingRecord.mentor",
        countQuery = "select count(trainingRecord) from TrainingRecord trainingRecord"
    )
    Page<TrainingRecord> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select trainingRecord from TrainingRecord trainingRecord left join fetch trainingRecord.person left join fetch trainingRecord.trainingGoal left join fetch trainingRecord.position left join fetch trainingRecord.mentor"
    )
    List<TrainingRecord> findAllWithToOneRelationships();

    @Query(
        "select trainingRecord from TrainingRecord trainingRecord left join fetch trainingRecord.person left join fetch trainingRecord.trainingGoal left join fetch trainingRecord.position left join fetch trainingRecord.mentor where trainingRecord.id =:id"
    )
    Optional<TrainingRecord> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select trainingRecord from TrainingRecord trainingRecord left join fetch trainingRecord.person left join fetch trainingRecord.trainingGoal left join fetch trainingRecord.position left join fetch trainingRecord.mentor where trainingRecord.person.id =:personId order by trainingRecord.trainingDate desc"
    )
    List<TrainingRecord> findByPersonIdWithEagerRelationships(@Param("personId") Long personId);
}
