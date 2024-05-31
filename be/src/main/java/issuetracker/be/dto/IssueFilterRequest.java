package issuetracker.be.dto;

public record IssueFilterRequest(
    String assignee,
    String label,
    String milestone,
    String reporter,
    String commentReporter,
    Boolean isOpen
){}