package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.DispatchReview;

/**
 * Spring Data JPA repository for the DispatchReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DispatchReviewRepository extends JpaRepository<DispatchReview, Long>, JpaSpecificationExecutor<DispatchReview> {}
