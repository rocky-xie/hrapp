package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.PartnerOrganization;

/**
 * Spring Data JPA repository for the PartnerOrganization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerOrganizationRepository
    extends JpaRepository<PartnerOrganization, Long>, JpaSpecificationExecutor<PartnerOrganization> {}
