package issuetracker.be.exception;

public class MilestoneHasAssociatedIssuesException extends RuntimeException{
  public MilestoneHasAssociatedIssuesException(String message) {
    super(message);
  }
}
