package issuetracker.be.dto;

import java.util.Date;

public record MilestoneWithIssueCountResponse(
    long id,
    String name,
    String description,
    Date end_date,
    Boolean is_open,
    int open_issue,
    int close_issue
) {

}
