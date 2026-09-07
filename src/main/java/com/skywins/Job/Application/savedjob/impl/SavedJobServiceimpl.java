package com.skywins.Job.Application.savedjob.impl;

import com.skywins.Job.Application.job.Job;
import com.skywins.Job.Application.job.JobRepository;
import com.skywins.Job.Application.savedjob.SavedJob;
import com.skywins.Job.Application.savedjob.SavedJobRepository;
import com.skywins.Job.Application.savedjob.SavedJobService;
import com.skywins.Job.Application.user.User;
import com.skywins.Job.Application.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/** Implements saved-job business rules and database operations. */
public class SavedJobServiceimpl implements SavedJobService {
  private final SavedJobRepository savedJobRepository;
  private final JobRepository jobRepository;
  private final UserRepository userRepository;

  public SavedJobServiceimpl(
      SavedJobRepository savedJobRepository,
      JobRepository jobRepository,
      UserRepository userRepository) {
    this.savedJobRepository = savedJobRepository;
    this.jobRepository = jobRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<SavedJob> findSavedJobsByUser(Long userId) {
    return savedJobRepository.findByUserIdOrderByCreatedAtDesc(userId);
  }

  @Override
  public List<Long> findSavedJobIdsByUser(Long userId) {
    return savedJobRepository.findJobIdsByUserId(userId);
  }

  @Override
  @Transactional
  public boolean saveJob(Long userId, Long jobId) {
    // A repeat save request is successful without adding a duplicate row.
    if (savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
      return true;
    }

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    Job job =
        jobRepository
            .findById(jobId)
            .orElseThrow(() -> new EntityNotFoundException("Job not found"));

    // Both related records must exist before the saved-job association is created.
    SavedJob savedJob = SavedJob.builder().user(user).job(job).build();
    savedJobRepository.save(savedJob);
    return true;
  }

  @Override
  @Transactional
  public boolean deleteSavedJob(Long userId, Long jobId) {
    // Delete is idempotent: deleting an absent association still leaves it unsaved.
    savedJobRepository.deleteByUserIdAndJobId(userId, jobId);
    return false;
  }
}
