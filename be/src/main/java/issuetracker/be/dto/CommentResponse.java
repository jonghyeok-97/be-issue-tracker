package issuetracker.be.dto;

import issuetracker.be.domain.Comment;
import issuetracker.be.utils.TimeLapseCalculator;
import java.time.LocalDateTime;

public record CommentResponse(

    Long id,
    Long issue_id,
    UserResponse reporter,
    String created_at,
    String contents
) {

  public CommentResponse(Comment comment, UserResponse user) {
    this(comment.getId(),
        comment.getIssue_id(),
        user,
        TimeLapseCalculator.between(comment.getCreated_at(), LocalDateTime.now()),
        comment.getContents()
    );
  }
}