package issuetracker.be.domain.issueFilter.type;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.IssueFilter;

public class IssueAssigneeFilter implements IssueFilter {
  private static final String NO_FILTER_CONDITION = "none";
  private final String assignee;

  public IssueAssigneeFilter(String assignee) {
    this.assignee = assignee;
  }

  @Override
  public boolean canFilter(Issue issue) {
    if (NO_FILTER_CONDITION.equals(assignee)) {
      return issue.getAssignees().isEmpty();
    }
    return issue.hasAssignee(assignee);
  }
}
