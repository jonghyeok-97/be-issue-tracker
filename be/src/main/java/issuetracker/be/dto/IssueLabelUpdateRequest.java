package issuetracker.be.dto;

import java.util.List;

public record IssueLabelUpdateRequest(
    Long issue_id,
    List<Long> label_id
) {

}
