package issuetracker.be.dto;

import issuetracker.be.domain.Milestone;
import java.time.LocalDate;

public record MilestoneSaveRequest(
    String name,
    String description,
    LocalDate end_date
) {

  public Milestone toEntity() {
    return new Milestone(name, description, end_date);
  }
}
