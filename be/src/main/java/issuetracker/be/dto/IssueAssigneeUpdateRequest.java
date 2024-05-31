package issuetracker.be.dto;

import java.util.List;

public record IssueAssigneeUpdateRequest(
    Long issue_id,
    List<String> name
) {

}
