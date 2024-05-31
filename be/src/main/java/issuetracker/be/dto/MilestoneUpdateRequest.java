package issuetracker.be.dto;

import issuetracker.be.domain.Milestone;
import java.time.LocalDate;

public record MilestoneUpdateRequest(
    Long id,
    String name,
    LocalDate end_date,
    String description
) {

  public Milestone toEntity() {
    return new Milestone(id, name, description, end_date);
  }

  public MilestoneUpdateRequest withId(Long id) {
    return new MilestoneUpdateRequest(id, name, end_date, description);
  }
}
