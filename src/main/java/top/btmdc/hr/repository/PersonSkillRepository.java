package top.btmdc.hr.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PersonSkill;

/**
 * Spring Data JPA repository for the PersonSkill entity.
 */
@Repository
public interface PersonSkillRepository extends JpaRepository<PersonSkill, Long>, JpaSpecificationExecutor<PersonSkill> {
    default Optional<PersonSkill> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PersonSkill> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PersonSkill> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select personSkill from PersonSkill personSkill left join fetch personSkill.person left join fetch personSkill.skill left join fetch personSkill.currentLevel left join fetch personSkill.previousLevel",
        countQuery = "select count(personSkill) from PersonSkill personSkill"
    )
    Page<PersonSkill> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select personSkill from PersonSkill personSkill left join fetch personSkill.person left join fetch personSkill.skill left join fetch personSkill.currentLevel left join fetch personSkill.previousLevel"
    )
    List<PersonSkill> findAllWithToOneRelationships();

    @Query(
        "select personSkill from PersonSkill personSkill left join fetch personSkill.person left join fetch personSkill.skill left join fetch personSkill.currentLevel left join fetch personSkill.previousLevel where personSkill.id =:id"
    )
    Optional<PersonSkill> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select personSkill from PersonSkill personSkill left join fetch personSkill.skill left join fetch personSkill.currentLevel where personSkill.person.id =:personId"
    )
    List<PersonSkill> findByPersonIdWithSkillAndLevel(@Param("personId") Long personId);

    @Query(
        "select personSkill from PersonSkill personSkill left join fetch personSkill.currentLevel where personSkill.person.id =:personId and personSkill.skill.id =:skillId"
    )
    Optional<PersonSkill> findOneByPersonIdAndSkillIdWithLevel(@Param("personId") Long personId, @Param("skillId") Long skillId);
}
