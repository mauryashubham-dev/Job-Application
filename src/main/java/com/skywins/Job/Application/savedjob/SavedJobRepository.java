package com.skywins.Job.Application.savedjob;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
  /** Loads saved jobs newest first for the saved-jobs page. */
  List<SavedJob> findByUserIdOrderByCreatedAtDesc(Long userId);

  /** Selects only job ids to avoid loading full job data for job-listing saved-state checks. */
  @Query("select savedJob.job.id from SavedJob savedJob where savedJob.user.id = :userId")
  List<Long> findJobIdsByUserId(@Param("userId") Long userId);

  /** Finds a saved-job association for an individual user and job. */
  Optional<SavedJob> findByUserIdAndJobId(Long userId, Long jobId);

  /** Checks whether a user has already saved a job before creating an association. */
  boolean existsByUserIdAndJobId(Long userId, Long jobId);

  /** Removes the association without deleting the user or job records. */
  void deleteByUserIdAndJobId(Long userId, Long jobId);
}
