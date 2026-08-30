package com.skywins.Job.Application.savedjob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/** Lightweight response describing the saved state after a save or unsave action. */
public class SavedJobStatusResponse {
  private Long jobId;
  private boolean saved;
}
