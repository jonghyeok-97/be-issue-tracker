package issuetracker.be.domain.issueFilter.type;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.IssueFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IssueLabelFilter implements IssueFilter {
  private static final String NO_FILTER_CONDITION = "none";
  private final String labelId;

  public IssueLabelFilter(String labelId) {
    this.labelId = labelId;
  }

  @Override
  public boolean canFilter(Issue issue) {
    if (NO_FILTER_CONDITION.equals(labelId)) {
      return issue.getLabels().isEmpty();
    }
    return issue.hasLabelId(Long.parseLong(labelId));
  }
}
