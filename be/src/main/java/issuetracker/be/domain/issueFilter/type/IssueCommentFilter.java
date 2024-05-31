package issuetracker.be.domain.issueFilter.type;

import issuetracker.be.domain.Comment;
import issuetracker.be.domain.Issue;
import issuetracker.be.domain.IssueFilter;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IssueCommentFilter implements IssueFilter {

  private final List<Comment> comments;

  public IssueCommentFilter(List<Comment> comments) {
    this.comments = comments;
  }

  @Override
  public boolean canFilter(Issue issue) {
    return comments.stream()
        .anyMatch(comment -> Objects.equals(comment.getIssue_id(), issue.getId()));
  }
}
