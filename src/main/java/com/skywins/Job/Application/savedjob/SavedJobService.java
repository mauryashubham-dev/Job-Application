package com.skywins.Job.Application.savedjob;

import java.util.List;

public interface SavedJobService {
  /** Returns all jobs saved by one user. */
  List<SavedJob> findSavedJobsByUser(Long userId);

  /** Returns only the ids of jobs saved by one user. */
  List<Long> findSavedJobIdsByUser(Long userId);

  /** Saves a job idempotently and returns the resulting saved state. */
  boolean saveJob(Long userId, Long jobId);

  /** Removes a saved job idempotently and returns the resulting saved state. */
  boolean deleteSavedJob(Long userId, Long jobId);
}
