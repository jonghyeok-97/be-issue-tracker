package issuetracker.be.dto;

import java.util.List;

public record IssueListResponse(
    List<IssueShowResponse> close_Issues,
    List<IssueShowResponse> open_Issues
) {

}
