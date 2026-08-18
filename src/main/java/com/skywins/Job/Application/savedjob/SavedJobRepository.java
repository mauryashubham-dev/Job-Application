package com.skywins.Job.Application.savedjob;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
  List<SavedJob> findByUserIdOrderByCreatedAtDesc(Long userId);

  @Query("select savedJob.job.id from SavedJob savedJob where savedJob.user.id = :userId")
  List<Long> findJobIdsByUserId(@Param("userId") Long userId);

  Optional<SavedJob> findByUserIdAndJobId(Long userId, Long jobId);

  boolean existsByUserIdAndJobId(Long userId, Long jobId);

  void deleteByUserIdAndJobId(Long userId, Long jobId);
}
