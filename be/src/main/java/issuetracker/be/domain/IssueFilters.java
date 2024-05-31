package issuetracker.be.domain;

import java.util.List;
import java.util.stream.Collectors;

public class IssueFilters {
  private final List<IssueFilter> filters;

  public IssueFilters(List<IssueFilter> filters) {
    this.filters = filters;
  }

  public List<Issue> doFilter(List<Issue> issues) {
    return issues.stream()
        .filter(issue ->
            filters.stream().allMatch(filter -> filter.canFilter(issue)))
        .collect(Collectors.toList());
  }
}
