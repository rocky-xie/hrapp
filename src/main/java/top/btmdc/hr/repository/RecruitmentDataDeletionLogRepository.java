package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.RecruitmentDataDeletionLog;

@Repository
public interface RecruitmentDataDeletionLogRepository extends JpaRepository<RecruitmentDataDeletionLog, Long> {}
