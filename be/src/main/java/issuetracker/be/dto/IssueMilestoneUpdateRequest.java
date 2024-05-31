package issuetracker.be.dto;

public record IssueMilestoneUpdateRequest(
    Long issue_id,
    Long milestone_id
) {

}
