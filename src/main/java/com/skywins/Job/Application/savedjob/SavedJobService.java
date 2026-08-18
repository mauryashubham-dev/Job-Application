package com.skywins.Job.Application.savedjob;

import java.util.List;

public interface SavedJobService {
  List<SavedJob> findSavedJobsByUser(Long userId);

  List<Long> findSavedJobIdsByUser(Long userId);

  boolean saveJob(Long userId, Long jobId);

  boolean deleteSavedJob(Long userId, Long jobId);
}
