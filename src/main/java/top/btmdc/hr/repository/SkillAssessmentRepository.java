package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.SkillAssessment;

/**
 * Spring Data JPA repository for the SkillAssessment entity.
 */
@Repository
public interface SkillAssessmentRepository extends JpaRepository<SkillAssessment, Long>, JpaSpecificationExecutor<SkillAssessment> {
    default Optional<SkillAssessment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SkillAssessment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SkillAssessment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select skillAssessment from SkillAssessment skillAssessment left join fetch skillAssessment.person left join fetch skillAssessment.skill left join fetch skillAssessment.assessor left join fetch skillAssessment.newLevel",
        countQuery = "select count(skillAssessment) from SkillAssessment skillAssessment"
    )
    Page<SkillAssessment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select skillAssessment from SkillAssessment skillAssessment left join fetch skillAssessment.person left join fetch skillAssessment.skill left join fetch skillAssessment.assessor left join fetch skillAssessment.newLevel"
    )
    List<SkillAssessment> findAllWithToOneRelationships();

    @Query(
        "select skillAssessment from SkillAssessment skillAssessment left join fetch skillAssessment.person left join fetch skillAssessment.skill left join fetch skillAssessment.assessor left join fetch skillAssessment.newLevel where skillAssessment.id =:id"
    )
    Optional<SkillAssessment> findOneWithToOneRelationships(@Param("id") Long id);
}
