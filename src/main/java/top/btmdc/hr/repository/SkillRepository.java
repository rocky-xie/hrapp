package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.Skill;

/**
 * Spring Data JPA repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {}
