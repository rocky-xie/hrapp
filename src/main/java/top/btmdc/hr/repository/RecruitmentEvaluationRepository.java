package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RecruitmentEvaluation;

/**
 * Spring Data JPA repository for the RecruitmentEvaluation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentEvaluationRepository
    extends JpaRepository<RecruitmentEvaluation, Long>, JpaSpecificationExecutor<RecruitmentEvaluation> {}
