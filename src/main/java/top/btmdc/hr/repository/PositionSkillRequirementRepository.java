package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PositionSkillRequirement;

/**
 * Spring Data JPA repository for the PositionSkillRequirement entity.
 */
@Repository
public interface PositionSkillRequirementRepository
    extends JpaRepository<PositionSkillRequirement, Long>, JpaSpecificationExecutor<PositionSkillRequirement>
{
    default Optional<PositionSkillRequirement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PositionSkillRequirement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PositionSkillRequirement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select positionSkillRequirement from PositionSkillRequirement positionSkillRequirement left join fetch positionSkillRequirement.position left join fetch positionSkillRequirement.skill left join fetch positionSkillRequirement.requiredLevel left join fetch positionSkillRequirement.preferredLevel",
        countQuery = "select count(positionSkillRequirement) from PositionSkillRequirement positionSkillRequirement"
    )
    Page<PositionSkillRequirement> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select positionSkillRequirement from PositionSkillRequirement positionSkillRequirement left join fetch positionSkillRequirement.position left join fetch positionSkillRequirement.skill left join fetch positionSkillRequirement.requiredLevel left join fetch positionSkillRequirement.preferredLevel"
    )
    List<PositionSkillRequirement> findAllWithToOneRelationships();

    @Query(
        "select positionSkillRequirement from PositionSkillRequirement positionSkillRequirement left join fetch positionSkillRequirement.position left join fetch positionSkillRequirement.skill left join fetch positionSkillRequirement.requiredLevel left join fetch positionSkillRequirement.preferredLevel where positionSkillRequirement.id =:id"
    )
    Optional<PositionSkillRequirement> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select positionSkillRequirement from PositionSkillRequirement positionSkillRequirement left join fetch positionSkillRequirement.skill left join fetch positionSkillRequirement.requiredLevel where positionSkillRequirement.position.id =:positionId"
    )
    List<PositionSkillRequirement> findByPositionIdWithSkillAndRequiredLevel(@Param("positionId") Long positionId);
}
