package issuetracker.be.dto;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.Label;
import issuetracker.be.utils.TimeLapseCalculator;
import java.time.LocalDateTime;
import java.util.List;


public record IssueShowResponse(
    Long id,
    String title,
    String create_At,
    UserResponse reporter,
    List<Label> label,
    MilestoneWithIssueCountResponse milestone
) {

  public IssueShowResponse(Issue issue, List<Label> label, MilestoneWithIssueCountResponse milestone, UserResponse reporter) {
    this(
        issue.getId(),
        issue.getTitle(),
        TimeLapseCalculator.between(issue.getCreated_at(), LocalDateTime.now()),
        reporter,
        label,
        milestone
    );
  }
}
