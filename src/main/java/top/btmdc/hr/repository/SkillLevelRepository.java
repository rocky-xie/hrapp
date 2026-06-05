package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.SkillLevel;

/**
 * Spring Data JPA repository for the SkillLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillLevelRepository extends JpaRepository<SkillLevel, Long>, JpaSpecificationExecutor<SkillLevel> {}
