package issuetracker.be.domain.issueFilter.type;

import issuetracker.be.domain.Issue;
import issuetracker.be.domain.IssueFilter;

public class IssueReporterFilter implements IssueFilter {

  private final String reporter;

  public IssueReporterFilter(String reporter) {
    this.reporter = reporter;
  }

  @Override
  public boolean canFilter(Issue issue) {
    return issue.hasReporter(reporter);
  }
}
