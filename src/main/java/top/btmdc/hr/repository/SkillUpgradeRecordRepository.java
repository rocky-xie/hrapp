package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.SkillUpgradeRecord;

/**
 * Spring Data JPA repository for the SkillUpgradeRecord entity.
 */
@Repository
public interface SkillUpgradeRecordRepository
    extends JpaRepository<SkillUpgradeRecord, Long>, JpaSpecificationExecutor<SkillUpgradeRecord>
{
    default Optional<SkillUpgradeRecord> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SkillUpgradeRecord> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SkillUpgradeRecord> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select skillUpgradeRecord from SkillUpgradeRecord skillUpgradeRecord left join fetch skillUpgradeRecord.person left join fetch skillUpgradeRecord.skill left join fetch skillUpgradeRecord.oldLevel left join fetch skillUpgradeRecord.newLevel left join fetch skillUpgradeRecord.assessor",
        countQuery = "select count(skillUpgradeRecord) from SkillUpgradeRecord skillUpgradeRecord"
    )
    Page<SkillUpgradeRecord> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select skillUpgradeRecord from SkillUpgradeRecord skillUpgradeRecord left join fetch skillUpgradeRecord.person left join fetch skillUpgradeRecord.skill left join fetch skillUpgradeRecord.oldLevel left join fetch skillUpgradeRecord.newLevel left join fetch skillUpgradeRecord.assessor"
    )
    List<SkillUpgradeRecord> findAllWithToOneRelationships();

    @Query(
        "select skillUpgradeRecord from SkillUpgradeRecord skillUpgradeRecord left join fetch skillUpgradeRecord.person left join fetch skillUpgradeRecord.skill left join fetch skillUpgradeRecord.oldLevel left join fetch skillUpgradeRecord.newLevel left join fetch skillUpgradeRecord.assessor where skillUpgradeRecord.id =:id"
    )
    Optional<SkillUpgradeRecord> findOneWithToOneRelationships(@Param("id") Long id);
}
