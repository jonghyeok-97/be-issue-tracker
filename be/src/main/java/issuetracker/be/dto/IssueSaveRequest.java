package issuetracker.be.dto;

import issuetracker.be.domain.Issue;
import java.time.LocalDateTime;
import java.util.List;

public record IssueSaveRequest(
    String title,
    String reporter,
    Long milestone_id,
    List<Long> label_id,
    List<String> assignee,
    String comment
) {

  public Issue toEntity(LocalDateTime now) {
    return new Issue(title, reporter, milestone_id, now, label_id, assignee);
  }
}
