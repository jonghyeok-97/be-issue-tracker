package issuetracker.be.domain.issueFilter;

import issuetracker.be.domain.Comment;
import issuetracker.be.domain.IssueFilter;
import issuetracker.be.domain.IssueFilters;
import issuetracker.be.domain.issueFilter.type.IssueAssigneeFilter;
import issuetracker.be.domain.issueFilter.type.IssueCommentFilter;
import issuetracker.be.domain.issueFilter.type.IssueLabelFilter;
import issuetracker.be.domain.issueFilter.type.IssueMilestoneFilter;
import issuetracker.be.domain.issueFilter.type.IssueReporterFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class IssueFilterFactory {

  public IssueFilters createIssueFilters(String assignee, String label, String milestone,
      String reporter, List<Comment> comments) {
    List<IssueFilter> issueFilters = new ArrayList<>();
    addFilter(assignee, IssueAssigneeFilter::new, issueFilters);
    addFilter(label, IssueLabelFilter::new, issueFilters);
    addFilter(milestone, IssueMilestoneFilter::new, issueFilters);
    addFilter(reporter, IssueReporterFilter::new, issueFilters);
    addFilter(comments, IssueCommentFilter::new, issueFilters);
    return new IssueFilters(issueFilters);
  }

  private void addFilter(String value, Function<String, IssueFilter> valueToIssueFilter,
      List<IssueFilter> issueFilters) {
    Optional.ofNullable(value)
        .map(valueToIssueFilter)
        .ifPresent(issueFilters::add);
  }

  private void addFilter(List<Comment> comments, Function<List<Comment>, IssueFilter> commentToIssueFilter,
      List<IssueFilter> issueFilters) {
    if (!comments.isEmpty()) {
      issueFilters.add(commentToIssueFilter.apply(comments));
    }
  }
}
