package issuetracker.be.domain;

public interface IssueFilter {

  boolean canFilter(Issue issue);
}
