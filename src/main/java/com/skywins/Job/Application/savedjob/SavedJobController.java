package com.skywins.Job.Application.savedjob;

import com.skywins.Job.Application.common.ApiResponse;
import com.skywins.Job.Application.savedjob.dto.SavedJobResponse;
import com.skywins.Job.Application.savedjob.dto.SavedJobStatusResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class SavedJobController {
  private final SavedJobService savedJobService;

  public SavedJobController(SavedJobService savedJobService) {
    this.savedJobService = savedJobService;
  }

  @GetMapping("/saved-jobs")
  public ResponseEntity<ApiResponse<List<SavedJobResponse>>> findSavedJobs(
      HttpServletRequest request) {
    Long userId = getUserId(request);
    List<SavedJobResponse> savedJobs =
        savedJobService.findSavedJobsByUser(userId).stream().map(SavedJobResponse::from).toList();
    return ResponseEntity.ok(ApiResponse.of(savedJobs, savedJobs.size()));
  }

  @GetMapping("/saved-jobs/ids")
  public ResponseEntity<ApiResponse<List<Long>>> findSavedJobIds(HttpServletRequest request) {
    Long userId = getUserId(request);
    List<Long> savedJobIds = savedJobService.findSavedJobIdsByUser(userId);
    return ResponseEntity.ok(ApiResponse.of(savedJobIds, savedJobIds.size()));
  }

  @PostMapping("/saved-jobs/{jobId}")
  public ResponseEntity<?> saveJob(@PathVariable Long jobId, HttpServletRequest request) {
    try {
      Long userId = getUserId(request);
      boolean saved = savedJobService.saveJob(userId, jobId);
      return ResponseEntity.ok(ApiResponse.of(new SavedJobStatusResponse(jobId, saved), 1));
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
  }

  @DeleteMapping("/saved-jobs/{jobId}")
  public ResponseEntity<ApiResponse<SavedJobStatusResponse>> deleteSavedJob(
      @PathVariable Long jobId, HttpServletRequest request) {
    Long userId = getUserId(request);
    boolean saved = savedJobService.deleteSavedJob(userId, jobId);
    return ResponseEntity.ok(ApiResponse.of(new SavedJobStatusResponse(jobId, saved), 1));
  }

  private Long getUserId(HttpServletRequest request) {
    return (Long) request.getAttribute("userId");
  }
}
