package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.KeyResponsibilityCategory;

/**
 * Spring Data JPA repository for the KeyResponsibilityCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyResponsibilityCategoryRepository
    extends JpaRepository<KeyResponsibilityCategory, Long>, JpaSpecificationExecutor<KeyResponsibilityCategory> {}
