package com.skywins.Job.Application.savedjob;

import com.skywins.Job.Application.job.Job;
import com.skywins.Job.Application.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "saved_jobs",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"}))
@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "job"})
@EqualsAndHashCode(exclude = {"user", "job"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/**
 * Represents one job saved by one user.
 *
 * <p>The database constraint prevents the same user from saving the same job more than once.
 */
public class SavedJob {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_id", nullable = false)
  private Job job;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    // Set the save timestamp when the association is first persisted.
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}
