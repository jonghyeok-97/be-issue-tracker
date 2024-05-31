package issuetracker.be.dto;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.Label;
import issuetracker.be.utils.TimeLapseCalculator;
import java.time.LocalDateTime;
import java.util.List;

public record IssueDetailResponse(
    Long id,
    String title,
    String created_At,
    UserResponse reporter,
    Boolean is_open,
    List<UserResponse> assignee,
    List<Label> label,
    MilestoneWithIssueCountResponse milestone,
    List<CommentResponse> comment

) {

  public IssueDetailResponse(Issue issue, List<UserResponse> assignee ,List<Label> label, MilestoneWithIssueCountResponse milestone, UserResponse reporter, List<CommentResponse> comment) {
    this(
        issue.getId(),
        issue.getTitle(),
        TimeLapseCalculator.between(issue.getCreated_at(), LocalDateTime.now()),
        reporter,
        issue.getIs_open(),
        assignee,
        label,
        milestone,
        comment
    );
  }
}