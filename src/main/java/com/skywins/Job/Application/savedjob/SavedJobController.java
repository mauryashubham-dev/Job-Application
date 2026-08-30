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
/**
 * Exposes saved-job operations for the authenticated user.
 *
 * <p>The user id comes from the JWT filter request attribute; clients must not send a user id.
 */
public class SavedJobController {
  private final SavedJobService savedJobService;

  public SavedJobController(SavedJobService savedJobService) {
    this.savedJobService = savedJobService;
  }

  @GetMapping("/saved-jobs")
  /** Returns the full job-card data for jobs saved by the current user. */
  public ResponseEntity<ApiResponse<List<SavedJobResponse>>> findSavedJobs(
      HttpServletRequest request) {
    Long userId = getUserId(request);
    List<SavedJobResponse> savedJobs =
        savedJobService.findSavedJobsByUser(userId).stream().map(SavedJobResponse::from).toList();
    return ResponseEntity.ok(ApiResponse.of(savedJobs, savedJobs.size()));
  }

  @GetMapping("/saved-jobs/ids")
  /** Returns only saved job ids so listing screens can efficiently mark saved jobs. */
  public ResponseEntity<ApiResponse<List<Long>>> findSavedJobIds(HttpServletRequest request) {
    Long userId = getUserId(request);
    List<Long> savedJobIds = savedJobService.findSavedJobIdsByUser(userId);
    return ResponseEntity.ok(ApiResponse.of(savedJobIds, savedJobIds.size()));
  }

  @PostMapping("/saved-jobs/{jobId}")
  /** Saves a job for the current user. Repeating the same request is safe and returns saved=true. */
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
  /** Removes a saved job when present. Repeating the request returns saved=false. */
  public ResponseEntity<ApiResponse<SavedJobStatusResponse>> deleteSavedJob(
      @PathVariable Long jobId, HttpServletRequest request) {
    Long userId = getUserId(request);
    boolean saved = savedJobService.deleteSavedJob(userId, jobId);
    return ResponseEntity.ok(ApiResponse.of(new SavedJobStatusResponse(jobId, saved), 1));
  }

  private Long getUserId(HttpServletRequest request) {
    // JwtFilter populates this attribute after validating the bearer token.
    return (Long) request.getAttribute("userId");
  }
}
