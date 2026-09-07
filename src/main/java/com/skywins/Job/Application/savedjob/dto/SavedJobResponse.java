package com.skywins.Job.Application.savedjob.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skywins.Job.Application.company.dto.CompanySummaryResponse;
import com.skywins.Job.Application.job.dto.ExperienceResponse;
import com.skywins.Job.Application.job.dto.JobResponse;
import com.skywins.Job.Application.job.dto.SalaryResponse;
import com.skywins.Job.Application.savedjob.SavedJob;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Job-card response for a saved job, including when the current user saved it.
 *
 * <p>The job fields reuse the existing {@link JobResponse} mapping to keep normal and saved job
 * cards consistent.
 */
public class SavedJobResponse {
  private Long id;
  private String title;
  private String description;
  private String minSalary;
  private String maxSalary;
  private String location;
  private String jobType;
  private String employmentType;
  private ExperienceResponse experience;
  private List<String> skills;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime postedAt;

  private boolean isNew;
  private SalaryResponse salary;
  private CompanySummaryResponse company;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private LocalDateTime savedAt;

  public static SavedJobResponse from(SavedJob savedJob) {
    // Reuse the standard job response so clients receive the same job-card fields in both APIs.
    JobResponse job = JobResponse.from(savedJob.getJob());
    return new SavedJobResponse(
        job.getId(),
        job.getTitle(),
        job.getDescription(),
        job.getMinSalary(),
        job.getMaxSalary(),
        job.getLocation(),
        job.getJobType(),
        job.getEmploymentType(),
        job.getExperience(),
        job.getSkills(),
        job.getPostedAt(),
        job.isNew(),
        job.getSalary(),
        job.getCompany(),
        savedJob.getCreatedAt());
  }
}
