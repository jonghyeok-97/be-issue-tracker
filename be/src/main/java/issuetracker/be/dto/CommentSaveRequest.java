package issuetracker.be.dto;

import issuetracker.be.domain.Comment;
import java.time.LocalDateTime;

public record CommentSaveRequest(
    Long issue_id,
    String reporter,
    String contents
) {

  public Comment toEntity(LocalDateTime now) {
    return new Comment(issue_id, reporter, now, contents);
  }

}