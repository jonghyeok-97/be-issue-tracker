package issuetracker.be.domain.issueFilter.type;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.IssueFilter;

public class IssueMilestoneFilter implements IssueFilter {
  private static final String NO_FILTER_CONDITION = "none";
  private final String milestoneId;

  public IssueMilestoneFilter(String milestoneId) {
    this.milestoneId = milestoneId;
  }

  @Override
  public boolean canFilter(Issue issue) {
    if (NO_FILTER_CONDITION.equals(milestoneId)) {
      return issue.getMilestone_id() == null;
    }
    return issue.hasMilestoneId(Long.parseLong(milestoneId));
  }
}
