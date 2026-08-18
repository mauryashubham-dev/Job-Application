package com.skywins.Job.Application.savedjob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SavedJobStatusResponse {
  private Long jobId;
  private boolean saved;
}
